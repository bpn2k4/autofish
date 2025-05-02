package com.bpn.auto.core;

public class MeanStd {
  
  private final long mean;
  private final long std;

  public MeanStd(long mean, long std) {
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
