package com.bpn.auto.core;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class Logger {

  public enum LogLevel {
    DEBUG,
    INFO,
    WARN,
    ERROR,
    NONE
  }

  private static final String GREEN = "\033[92m";
  private static final String RED = "\033[91m";
  private static final String BLUE = "\033[94m";
  private static final String YELLOW = "\033[93m";
  private static final String RESET = "\033[0m";
  private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

  private final LogLevel logLevel;

  public Logger() {
    this.logLevel = LogLevel.INFO;
  }

  public Logger(LogLevel level) {
    this.logLevel = level;
  }

  private static String getCurrentTimestamp() {
    return formatter.format(new Date());
  }

  public void d(String msg) {
    if (logLevel.ordinal() <= LogLevel.DEBUG.ordinal()) {
      String time = getCurrentTimestamp();
      String message = String.format("[%s] %s[DEBUG]%s %s", time, BLUE, RESET, msg);
      System.out.println(message);
    }
  }

  public void i(String msg) {
    if (logLevel.ordinal() <= LogLevel.INFO.ordinal()) {
      String time = getCurrentTimestamp();
      String message = String.format("[%s] %s[INFO]%s %s", time, GREEN, RESET, msg);
      System.out.println(message);
    }
  }

  public void w(String msg) {
    if (logLevel.ordinal() <= LogLevel.WARN.ordinal()) {
      String time = getCurrentTimestamp();
      String message = String.format("[%s] %s[WARN]%s %s", time, YELLOW, RESET, msg);
      System.out.println(message);
    }
  }

  public void e(String msg) {
    if (logLevel.ordinal() <= LogLevel.ERROR.ordinal()) {
      String time = getCurrentTimestamp();
      String message = String.format("[%s] %s[ERROR]%s %s", time, RED, RESET, msg);
      System.out.println(message);
    }
  }
}
