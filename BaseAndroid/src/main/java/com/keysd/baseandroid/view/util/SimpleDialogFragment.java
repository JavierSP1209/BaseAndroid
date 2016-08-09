/**
 * File: SimpleDialogFragment
 * CreationDate: 06/11/14
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description:
 */

package com.keysd.baseandroid.view.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.keysd.baseandroid.R;
import com.keysd.baseandroid.util.CMUtils;

/**
 * Dialog Fragment with simple characteristics. This dialog is fully customizable, see
 * {@link com.keysd.baseandroid.view.util.SimpleDialogBuilder} class to modify this dialog's
 * behaviour and
 * appearance
 *
 * @author "Ing. Arturo Ayala Tello"
 * @version 1.0
 * @since 06/11/14
 */
public class SimpleDialogFragment extends DialogFragment {

  private SimpleDialogBuilder dialogBuilder;

  private View getDefaultDialogView() {
    LayoutInflater inflater = getActivity().getLayoutInflater();
    View v = inflater.inflate(R.layout.fragment_simple_dialog, null);
    TextView txtTitle = (TextView) v.findViewById(R.id.txtTitle);
    TextView txtContent = (TextView) v.findViewById(R.id.txtContent);

    //Check if title is null or empty, if not use the string
    if (!CMUtils.isEmptyText(dialogBuilder.getTitleStr())) {
      txtTitle.setText(dialogBuilder.getTitleStr());
    } else {
      txtTitle.setText(dialogBuilder.getTitle());
    }

    //Check if title is null or empty, if not use the string
    if (!CMUtils.isEmptyText(dialogBuilder.getContentStr())) {
      txtContent.setText(dialogBuilder.getContentStr());
    } else {
      txtContent.setText(dialogBuilder.getContent());
    }

    if (dialogBuilder.getContentTextColor() != 0) {
      //The context text color represents the resolved color, so it may be < 0 even if set
      txtContent.setTextColor(dialogBuilder.getContentTextColor());
    }
    return v;
  }

  public static SimpleDialogFragment newInstance(SimpleDialogBuilder dialogBuilder) {
    SimpleDialogFragment dialogFragment = new SimpleDialogFragment();
    dialogFragment.setDialogBuilder(dialogBuilder);
    return dialogFragment;
  }

  public void setDialogBuilder(SimpleDialogBuilder dialogBuilder) {
    this.dialogBuilder = dialogBuilder;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    if (dialogBuilder == null) {
      dialogBuilder = new SimpleDialogBuilder();
    }
    final DialogInterface.OnClickListener positiveClickListener = dialogBuilder
        .getPositiveClickListener();
    final DialogInterface.OnClickListener negativeClickListener = dialogBuilder
        .getNegativeClickListener();
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    //Set the default if no custom view is defined
    if (dialogBuilder.getView() == null) {
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        builder.setView(getDefaultDialogView());
      } else {
        //Check if title is null or empty, if not use the string
        if (!CMUtils.isEmptyText(dialogBuilder.getTitleStr())) {
          builder.setTitle(dialogBuilder.getTitleStr());
        } else {
          builder.setTitle(dialogBuilder.getTitle());
        }

        //Check if title is null or empty, if not use the string
        if (!CMUtils.isEmptyText(dialogBuilder.getContentStr())) {
          builder.setTitle(dialogBuilder.getContentStr());
        } else {
          builder.setMessage(dialogBuilder.getContent());
        }
      }
    } else {
      builder.setView(dialogBuilder.getView());
    }
    builder.setInverseBackgroundForced(dialogBuilder.isInverseBackground());
    if (positiveClickListener != null) {
      builder.setPositiveButton(dialogBuilder.getPositiveText(), positiveClickListener);
    }
    if (negativeClickListener != null) {
      builder.setNegativeButton(dialogBuilder.getNegativeText(), negativeClickListener);
    }
    AlertDialog alert = builder.create();
    final int positiveBack = dialogBuilder.getPositiveBackground();
    final int negativeBack = dialogBuilder.getNegativeBackground();
    alert.setOnShowListener(new DialogInterface.OnShowListener() {
      @Override
      public void onShow(DialogInterface dialog) {
        if (positiveClickListener != null && positiveBack > 0) {
          ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE)
                                .setBackgroundResource(positiveBack);
        }
        if (negativeClickListener != null && negativeBack > 0) {
          ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE)
                                .setBackgroundResource(negativeBack);
        }
      }
    });
    return alert;
  }

  @Override
  public void onDismiss(DialogInterface dialog) {
    super.onDismiss(dialog);
    if (dialogBuilder.getOnDismissListener() != null) {
      dialogBuilder.getOnDismissListener().onDismiss(dialog);
    }
  }
}
