package com.bpn.auto;

import com.bpn.auto.core.Fishing;

public class Main {
  public static void main(String[] args) {

    try {
      Fishing fishing = new Fishing();
      fishing.start(Fishing.FISH_AND_PRESERVE_MODE);
    } catch (Exception e) {
      e.printStackTrace(System.out);
    }
  }
}
