package com.bpn.auto.core;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class Logger {

  private static final String GREEN = "\033[92m";
  private static final String RED = "\033[91m";
  private static final String BLUE = "\033[94m";
  private static final String YELLOW = "\033[93m";
  private static final String RESET = "\033[0m";
  private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

  private static String getCurrentTimestamp() {
    return formatter.format(new Date());
  }

  public static void i(String msg) {
    String time = getCurrentTimestamp();
    String message = String.format("[%s] %s[INFO]%s %s", time, GREEN, RESET, msg);
    System.out.println(message);
  }

  public static void e(String msg) {
    String time = getCurrentTimestamp();
    String message = String.format("[%s] %s[ERROR]%s %s", time, RED, RESET, msg);
    System.out.println(message);
  }

  public static void d(String msg) {
    String time = getCurrentTimestamp();
    String message = String.format("[%s] %s[DEBUG]%s %s", time, BLUE, RESET, msg);
    System.out.println(message);
  }

  public static void w(String msg) {
    String time = getCurrentTimestamp();
    String message = String.format("[%s] %s[WARN]%s %s", time, YELLOW, RESET, msg);
    System.out.println(message);
  }
}
