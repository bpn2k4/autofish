package com.bpn.auto.core;

import android.graphics.Bitmap;

public class Image {

  private final int width;
  private final int height;
  private final Bitmap bitmapData;

  public Image(Bitmap bitmapData) {
    this.bitmapData = bitmapData.copy(Bitmap.Config.ARGB_8888, true);
    this.width = bitmapData.getWidth();
    this.height = bitmapData.getHeight();
  }

  public int getPixel(int x, int y) {
    return this.bitmapData.getPixel(x, y);
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

  public void recycle() {
    this.bitmapData.recycle();
  }

  @Override
  protected void finalize() throws Throwable {
    this.bitmapData.recycle();
    super.finalize();
  }
}