package com.bpn.auto;

import android.graphics.Bitmap;

import com.bpn.auto.core.Capture;
import com.bpn.auto.core.Image;

public class Test {

  public static void main(String[] args) {

    try {
      for (int c = 0; c < 500; c++) {
        long start = System.currentTimeMillis();
        Image screenshot = Capture.takeScreenshot();
        Bitmap bitmap = screenshot.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
//        int pixel = bitmap.getPixel(0, 0);
        long take = System.currentTimeMillis() - start;
        System.out.println("Pixel: " + 1 + " Take: " + take);
      }
    }
    catch (Exception e) {
      e.printStackTrace(System.out);
    }
  }
}
