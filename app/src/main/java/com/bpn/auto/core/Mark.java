package com.bpn.auto.core;


public class Mark {
  private final long mean;
  private final long std;

  public Mark(long mean, long std) {
    this.mean = mean;
    this.std = std;
  }

  public long getMean() {
    return mean;
  }

  public long getStd() {
    return std;
  }
}
