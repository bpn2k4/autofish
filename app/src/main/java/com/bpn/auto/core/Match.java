package com.bpn.auto.core;

public class Match {

  public static boolean matchTemplate(Image region, Template template) {
    int offsetX = template.getX();
    int offsetY = template.getY();
    int width = template.getWidth();
    int height = template.getHeight();
    int[] pixels = region.getPixels(offsetX, offsetY, width, height);
    for (int x = 0; x < template.getWidth(); x = x + 2) {
      for (int y = 0; y < template.getHeight(); y = y + 2) {
        if (template.getPixel(x, y) != pixels[x + y * width]) {
          return false;
        }
      }
    }

    return true;
  }
}
