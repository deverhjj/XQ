package com.biu.modulebase.binfenjiari.util;

import android.animation.PropertyValuesHolder;

public class AnimatorUtils {

  public static final String ALPHA = "alpha";
  public static final String ROTATION = "rotation";
  public static final String ROTATION_X = "rotationX";
  public static final String ROTATION_Y = "rotationY";
  public static final String SCALE_X = "scaleX";
  public static final String SCALE_Y = "scaleY";
  public static final String PIVOT_X = "pivotX";
  public static final String PIVOT_Y = "pivotY";
  public static final String TRANSLATION_X = "translationX";
  public static final String TRANSLATION_Y = "translationY";
  public static final String X = "x";
  public static final String Y = "y";

  private AnimatorUtils() {
    //No instances.
  }

  public static PropertyValuesHolder alpha(float... values) {
    return PropertyValuesHolder.ofFloat(ALPHA, values);
  }

  public static PropertyValuesHolder rotation(float... values) {
    return PropertyValuesHolder.ofFloat(ROTATION, values);
  }

  public static PropertyValuesHolder rotationX(float... values) {
    return PropertyValuesHolder.ofFloat(ROTATION_X, values);
  }

  public static PropertyValuesHolder rotationY(float... values) {
    return PropertyValuesHolder.ofFloat(ROTATION_Y, values);
  }

  public static PropertyValuesHolder translationX(float... values) {
    return PropertyValuesHolder.ofFloat(TRANSLATION_X, values);
  }

  public static PropertyValuesHolder translationY(float... values) {
    return PropertyValuesHolder.ofFloat(TRANSLATION_Y, values);
  }
//
//  public static PropertyValuesHolder x(float... values) {
//    return PropertyValuesHolder.ofFloat(X, values);
//  }
//
//  public static PropertyValuesHolder y(float... values) {
//    return PropertyValuesHolder.ofFloat(Y, values);
//  }

  public static PropertyValuesHolder scaleX(float... values) {
    return PropertyValuesHolder.ofFloat(SCALE_X, values);
  }

  public static PropertyValuesHolder scaleY(float... values) {
    return PropertyValuesHolder.ofFloat(SCALE_Y, values);
  }
}
