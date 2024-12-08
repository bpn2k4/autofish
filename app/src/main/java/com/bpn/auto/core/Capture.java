package com.bpn.auto.core;


import android.graphics.Bitmap;
import android.graphics.Rect;
import java.lang.reflect.Method;


public class Capture {

  private static final int screenWidth = 1280;
  private static final int screenHeight = 720;
  private static Method screenshotMethod;
  private static final Rect rect = new Rect();

  static {
    try {
      Class<?> surfaceControlClass = Class.forName("android.view.SurfaceControl");
      String screenshotMethodName = getScreenshotMethodName();
      screenshotMethod = surfaceControlClass.getMethod(screenshotMethodName, Rect.class, Integer.TYPE, Integer.TYPE, Integer.TYPE);
    } catch (Exception e) {
      e.printStackTrace(System.out);
    }
  }

  public static Image takeScreenshot() {
    Bitmap bitmap = null;
    try {
      bitmap = (Bitmap) screenshotMethod.invoke(null, rect, screenWidth, screenHeight, 0);
    } catch (Exception e) {
      e.printStackTrace(System.out);
    }
    assert bitmap != null;
    return new Image(bitmap);
  }

  private static String getScreenshotMethodName() {
    return "screenshot";
  }
}
