package com.bpn.auto.core;

import android.os.SystemClock;
import android.view.InputEvent;
import android.view.MotionEvent;
import java.lang.reflect.Method;

public class Control {

  private static Method injectInputEventMethod;
  private static Object inputManagerInstance;
  private static MotionEvent.PointerProperties[] pointerProperties;
  private static MotionEvent.PointerCoords[] pointerCoords;

  static {
    try {
      Class<?> inputManagerClass = Class.forName(getInputManagerClassName());
      Method getInstanceMethod = inputManagerClass.getDeclaredMethod("getInstance");
      inputManagerInstance = getInstanceMethod.invoke(null);
      assert inputManagerInstance != null;
      injectInputEventMethod = inputManagerInstance.getClass().getMethod("injectInputEvent", InputEvent.class, int.class);

      pointerProperties = new MotionEvent.PointerProperties[1];
      pointerProperties[0] = new MotionEvent.PointerProperties();
      pointerProperties[0].id = 0;
      pointerProperties[0].toolType = MotionEvent.TOOL_TYPE_FINGER;

      pointerCoords = new MotionEvent.PointerCoords[1];
      pointerCoords[0] = new MotionEvent.PointerCoords();
      pointerCoords[0].orientation = 0.0F;
      pointerCoords[0].size = 0.0F;
      pointerCoords[0].pressure = 1.0F;
    }
    catch (Exception e) {
      e.printStackTrace(System.out);
    }
  }

  public static void touch(int x, int y) {
    try {
      pointerCoords[0].x = x;
      pointerCoords[0].y = y;

      long downTime = SystemClock.uptimeMillis();
      MotionEvent motionEventDown = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_DOWN, 1, pointerProperties, pointerCoords, 0, 0, 1f, 1f, 0, 0, 4098, 0);
      injectInputEventMethod.invoke(inputManagerInstance, motionEventDown, 0);

      long eventTime = SystemClock.uptimeMillis();
      MotionEvent motionEventUp = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, 1, pointerProperties, pointerCoords, 0, 0, 1f, 1f, 0, 0, 4098, 0);
      injectInputEventMethod.invoke(inputManagerInstance, motionEventUp, 0);
    }
    catch (Exception e) {
      e.printStackTrace(System.out);
    }
  }

  public static void touch(Button button) {
    touch(button.getX(), button.getY());
  }

  private static String getInputManagerClassName() {
    return "android.hardware.input.InputManager";
  }
}
