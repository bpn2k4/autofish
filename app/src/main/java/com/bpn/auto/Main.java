package com.bpn.auto;

import com.bpn.auto.core.Fishing;

public class Main {
  public static void main(String[] args) {

    try {
      Fishing fishing = new Fishing();
      fishing.start();
    } catch (Exception e) {
      e.printStackTrace(System.out);
    }
  }

}