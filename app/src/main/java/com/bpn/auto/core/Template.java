package com.bpn.auto.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.File;

public class Template {
  private final String name;
  private final int x;
  private final int y;
  private final int width;
  private final int height;
  private final String path;
  private final Bitmap bitmap;

  public Template(String name, int x, int y, String path) {
    this.name = name;
    this.x = x;
    this.y = y;
    this.path = path;
    File file = new File(path);
    this.bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
    this.width = this.bitmap.getWidth();
    this.height = this.bitmap.getHeight();
  }

  public String getName() {
    return this.name;
  }
  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  public int getWidth() {
    return this.width;
  }

  public int getHeight() {
    return this.height;
  }

  public String getPath() {
    return this.path;
  }

  public Bitmap getBitmap() {
    return this.bitmap;
  }

  public int getPixel(int x, int y) {
    return this.bitmap.getPixel(x, y);
  }
}
