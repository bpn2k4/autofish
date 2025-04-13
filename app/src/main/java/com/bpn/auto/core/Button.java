package com.bpn.auto.core;


public class Button {
  private final String name;
  private final int x;
  private final int y;
  public Button(String name, int x, int y) {
    this.name = name;
    this.x = x;
    this.y = y;
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
}
