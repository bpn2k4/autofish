package com.bpn.auto.core;

public class Match {

  public static boolean matchTemplate(Image region, Template template) {
    int offsetX = template.getX();
    int offsetY = template.getY();
    int width = template.getWidth();
    int height = template.getHeight();
    for (int x = 0; x < width; x = x + 2) {
      for (int y = 0; y < height; y = y + 2) {
        if (template.getPixel(x, y) != region.getPixel(offsetX + x, offsetY + y)) {
          return false;
        }
      }
    }

    return true;
  }
}
