/**
 * File: FloatingHintControl
 * CreationDate: 31/07/14
 * Author: "M. en C. Javier Silva Perez (JSP)"
 * Description:
 * Layout which an {@link android.widget.EditText} to show a floating label when the hint is
 * hidden due to the user
 * inputting text.
 * <p>
 * Copyright (C) 2014 Chris Banes
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.keysd.baseandroid.view.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.keysd.baseandroid.R;

/**
 * Layout which uses an {@link android.widget.EditText} to show a floating label when the hint is
 * hidden due to user
 * inputting text.
 *
 * @author "M. en C. Javier Silva Perez (JSP)"
 * @version 1.0
 * @see <a href="https://dribbble.com/shots/1254439--GIF-Mobile-Form-Interaction">Matt D. Smith
 * on Dribble</a>
 * @see <a href="http://bradfrostweb.com/blog/post/float-label-pattern/">Brad Frost's blog post</a>
 * @see
 * <a href="https://gist.github.com/chrisbanes/11247418#file-floatlabellayout-java">Chrisbanes
 * Gist</a>
 * @since 31/07/14
 */
public class FloatingHintControl extends LinearLayout {

  private static final int DEFAULT_ANIMATION_DURATION = 150;
  private static final float DEFAULT_PADDING_LEFT_RIGHT_DP = 12f;

  private Spinner mSpinner;
  private String mSpinnerHint;

  /**
   * Sets the custom listener for the spinner if necessary
   *
   * @param spinnerItemSelectedListener
   */
  public void setSpinnerItemSelectedListener(
      AdapterView.OnItemSelectedListener spinnerItemSelectedListener) {
    this.mSpinnerItemSelectedListener = spinnerItemSelectedListener;
  }

  private AdapterView.OnItemSelectedListener mSpinnerItemSelectedListener;
  private EditText mEditText;
  private TextView mLabel;

  private long animationDuration;

  public FloatingHintControl(Context context) {
    this(context, null);
  }

  public FloatingHintControl(Context context, AttributeSet attrs) {
    super(context, attrs);
    initFloatingHintControl(context, attrs);
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public FloatingHintControl(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    initFloatingHintControl(context, attrs);
  }

  public void initFloatingHintControl(Context context, AttributeSet attrs) {
    final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FloatLabelLayout);

    final int sidePadding = a
        .getDimensionPixelSize(R.styleable.FloatLabelLayout_floatLabelSidePadding,
                               dipsToPix(DEFAULT_PADDING_LEFT_RIGHT_DP));
    mLabel = new TextView(context);

    //Hide label by default
    mLabel.setPadding(sidePadding, 0, sidePadding, 0);
    mLabel.setVisibility(INVISIBLE);
    mLabel.setMaxLines(1);

    //Set label text format
    mLabel.setTextAppearance(context,
                             a.getResourceId(R.styleable.FloatLabelLayout_floatLabelTextAppearance,
                                             android.R.style.TextAppearance_Small));

    //Get animation duration or set the default one
    animationDuration = a
        .getInteger(R.styleable.FloatLabelLayout_animationDuration, DEFAULT_ANIMATION_DURATION);
    mSpinnerHint = a.getString(R.styleable.FloatLabelLayout_spinnerHint);

    setOrientation(LinearLayout.VERTICAL);
    addView(mLabel, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

    a.recycle();
  }

  @Override
  public final void addView(@NonNull View child, int index, ViewGroup.LayoutParams params) {
    if (child instanceof EditText) {
      // If we already have an EditText, throw an exception
      if (mEditText != null || mSpinner != null) {
        throw new IllegalArgumentException(
            "We already have an EditText/Spinner, can only have one");
      }

      setEditText((EditText) child);
    } else if (child instanceof Spinner) {
      // If we already have an EditText, throw an exception
      if (mSpinner != null || mEditText != null) {
        throw new IllegalArgumentException(
            "We already have an EditText/Spinner, can only have one");
      }

      setSpinner((Spinner) child);
    }

    // Carry on adding the View...
    super.addView(child, index, params);
  }

  /**
   * Sets the Spinner to watch
   *
   * @param spinner
   * 	Spinner to which the floating hint control will be attached
   */
  private void setSpinner(@NonNull Spinner spinner) {
    mSpinner = spinner;

    // Add a TextWatcher so that we know when the text input has changed
    mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
          // The text is empty, so hide the label if it is visible
          if (mLabel.getVisibility() == View.VISIBLE) {
            cancelAnimation();
            hideLabel();
          }
        } else {
          // The text is not empty, so show the label if it is not visible
          if (mLabel.getVisibility() != View.VISIBLE) {
            cancelAnimation();
            showLabel();
          }
        }
        if (mSpinnerItemSelectedListener != null) {
          //Call the custom spinner item selected listener
          mSpinnerItemSelectedListener.onItemSelected(parent, view, position, id);
        }

      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
        if (mSpinnerItemSelectedListener != null) {
          mSpinnerItemSelectedListener.onNothingSelected(parent);
        }
      }
    });

    // Add focus listener to the EditText so that we can notify the label that it is activated.
    // Allows the use of a ColorStateList for the text color on the label
    mSpinner.setOnFocusChangeListener(new OnFocusChangeListener() {
      @Override
      public void onFocusChange(View view, boolean focused) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
          mLabel.setActivated(focused); // only available after API 11
        }
      }
    });

    mLabel.setText(mSpinnerHint);
  }

  /**
   * Updates floating hint label text
   *
   * @param labelText
   * 	Text to show on the floating hint
   */
  public void updateLabel(CharSequence labelText) {
    if (mLabel != null) {
      mLabel.setText(labelText);
    }
  }

  /**
   * Updates floating hint label text
   *
   * @param labelText
   * 	Text to show on the floating hint
   */
  public void updateLabel(@StringRes int labelText) {
    if (mLabel != null) {
      mLabel.setText(labelText);
    }
  }

  /**
   * Sets the EditText to watch
   *
   * @param editText
   * 	Edit text to which the floating hint control will be attached
   */
  private void setEditText(@NonNull EditText editText) {
    mEditText = editText;

    // Add a TextWatcher so that we know when the text input has changed
    mEditText.addTextChangedListener(new TextWatcher() {

      @Override
      public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s)) {
          // The text is empty, so hide the label if it is visible
          if (mLabel.getVisibility() == View.VISIBLE) {
            cancelAnimation();
            hideLabel();
          }
        } else {
          // The text is not empty, so show the label if it is not visible
          if (mLabel.getVisibility() != View.VISIBLE) {
            cancelAnimation();
            showLabel();
          }
        }
      }

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

    });

    // Add focus listener to the EditText so that we can notify the label that it is activated.
    // Allows the use of a ColorStateList for the text color on the label
    mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
      @Override
      public void onFocusChange(View view, boolean focused) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
          mLabel.setActivated(focused); // only available after API 11
        }
      }
    });

    mLabel.setText(mEditText.getHint());
  }

  /**
   * @return the {@link android.widget.EditText} text input
   */
  public EditText getEditText() {
    return mEditText;
  }

  /**
   * @return the {@link android.widget.TextView} label
   */
  public TextView getLabel() {
    return mLabel;
  }

  /**
   * Cancels the current animation for the label
   */
  private void cancelAnimation() {
    Animation animation = mLabel.getAnimation();
    if (animation != null) {
      animation.cancel();
    }
    mLabel.clearAnimation();
  }

  /**
   * Show the label using an animation
   */
  private void showLabel() {
    mLabel.setVisibility(View.VISIBLE);
    //For backward compatibility
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
      mLabel.setAlpha(0f);
      mLabel.setTranslationY(mLabel.getHeight());
      mLabel.animate().alpha(1f).translationY(0f).setDuration(animationDuration).setListener(null)
            .start();
    } else {
      AnimationSet animationSet = new AnimationSet(true);

      TranslateAnimation translateAnimation =
          new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                                 Animation.ABSOLUTE,
                                 mLabel.getHeight(), Animation.ABSOLUTE, 0);
      translateAnimation.setDuration(animationDuration);
      animationSet.addAnimation(translateAnimation);

      AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
      alphaAnimation.setDuration(animationDuration);
      animationSet.addAnimation(alphaAnimation);

      mLabel.startAnimation(animationSet);
    }
  }

  /**
   * Hide the label using an animation
   */
  private void hideLabel() {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
      mLabel.setAlpha(1f);
      mLabel.setTranslationY(0f);
      mLabel.animate().alpha(0f).translationY(mLabel.getHeight()).setDuration(animationDuration)
            .setListener(new AnimatorListenerAdapter() {
              @Override
              public void onAnimationEnd(Animator animation) {
                mLabel.setVisibility(INVISIBLE);
              }
            }).start();
    } else {
      AnimationSet animationSet = new AnimationSet(true);

      TranslateAnimation translateAnimation =
          new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                                 Animation.ABSOLUTE,
                                 0, Animation.ABSOLUTE, mLabel.getHeight());
      translateAnimation.setDuration(animationDuration);
      animationSet.addAnimation(translateAnimation);

      AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
      alphaAnimation.setDuration(animationDuration);
      animationSet.addAnimation(alphaAnimation);


      animationSet.setAnimationListener(new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
          mLabel.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
      });


      mLabel.startAnimation(animationSet);
    }
  }

  /**
   * Helper method to convert dips to pixels.
   */
  private int dipsToPix(float dps) {
    return (int) TypedValue
        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, dps, getResources().getDisplayMetrics());
  }
}