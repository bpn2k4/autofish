package com.bpn.auto.core;


public class Mark {
  private final int mean;
  private final int std;

  public Mark(int mean, int std) {
    this.mean = mean;
    this.std = std;
  }

  public int getMean() {
    return mean;
  }

  public int getStd() {
    return std;
  }
}
