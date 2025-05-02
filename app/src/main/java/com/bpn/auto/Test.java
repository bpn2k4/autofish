package com.bpn.auto;

import com.bpn.auto.core.Capture;
import com.bpn.auto.core.Image;

public class Test {
  public static void main(String[] args) {
    try {
      Image screenshot = Capture.takeScreenshot();
      System.out.println("Pixel: " + screenshot.getPixel(640, 576));
      System.out.println("Pixel: " + screenshot.getPixel(640, 592));
    }
    catch (Exception e) {
      e.printStackTrace(System.out);
    }
  }
}
