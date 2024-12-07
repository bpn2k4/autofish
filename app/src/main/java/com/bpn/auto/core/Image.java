package com.bpn.auto.core;

import android.graphics.Bitmap;

import java.lang.reflect.Method;

public class Image {

  private final int width;
  private final int height;
  private final Bitmap bitmapData;
  private long bitmapInstance;
  private static Method getNativeInstanceMethod;
  private static Method  nativeGetPixelMethod;
  private static Method  nativeGetPixelsMethod;

  static {
    try {
      Class<?> bitmapClass = Class.forName("android.graphics.Bitmap");
      getNativeInstanceMethod = bitmapClass.getDeclaredMethod(getGetNativeInstanceMethodName());
      nativeGetPixelMethod = bitmapClass.getDeclaredMethod(getNativeGetPixelMethodName(), Long.TYPE, Integer.TYPE, Integer.TYPE);
      nativeGetPixelMethod.setAccessible(true);
      nativeGetPixelsMethod = bitmapClass.getDeclaredMethod(getNativeGetPixelsMethod(), Long.TYPE, int[].class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
      nativeGetPixelsMethod.setAccessible(true);
    }
    catch (Exception e) {
      e.printStackTrace(System.out);
    }
  }

  public Image(Bitmap bitmapData) {
    this.bitmapData = bitmapData;
    this.width = bitmapData.getWidth();
    this.height = bitmapData.getHeight();
    try {
      Object instance = getNativeInstanceMethod.invoke(bitmapData);
      if (instance != null) {
        this.bitmapInstance = (long) instance;
      }
    }
    catch (Exception e) {
      e.printStackTrace(System.out);
    }
  }

  public int getPixel(int x, int y) {
    try {
      Object pixel = nativeGetPixelMethod.invoke(null, this.bitmapInstance, x, y);
      if (pixel != null) {
        return (int) pixel;
      }
      return 0;
    }
    catch (Exception e) {
      e.printStackTrace(System.out);
    }
    return 0;
  }

  public int[] getPixels(int x, int y, int width, int height) {
    int[] pixels = new int[width * height];
    try {
      nativeGetPixelsMethod.invoke(null, this.bitmapInstance, pixels, 0, width, x, y, width, height);
    }
    catch (Exception e) {
      e.printStackTrace(System.out);
    }
    return pixels;
  }

  public Bitmap getBitmap() {
    return this.bitmapData;
  }

  public int getWidth() {
    return this.width;
  }

  public int getHeight() {
    return this.height;
  }

  private static String getGetNativeInstanceMethodName() {
    return "getNativeInstance";
  }

  private static String getNativeGetPixelMethodName() {
    return "nativeGetPixel";
  }

  private static String getNativeGetPixelsMethod() {
    return "nativeGetPixels";
  }

  public void recycle() {
    this.bitmapData.recycle();
  }
}