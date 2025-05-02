package com.bpn.auto.core;

public class Matcher {

  private static final int PIXEL_COLOR_BAG = -1818302;
  private static final int X_BAG = 1217, Y_BAG = 391;

  private static final int PIXEL_COLOR_CONFIRM_DIALOG_1 = -12466701;
  private static final int PIXEL_COLOR_CONFIRM_DIALOG_2 = -1;
  private static final int X_CONFIRM_DIALOG_1 = 640, Y_CONFIRM_DIALOG_1 = 570;
  private static final int X_CONFIRM_DIALOG_2 = 400, Y_CONFIRM_DIALOG_2 = 570;

  private static final int PIXEL_COLOR_CONFIRM_SELL_1 = -12466701;
  private static final int PIXEL_COLOR_CONFIRM_SELL_2 = -1;
  private static final int X_CONFIRM_SELL_1 = 710, Y_CONFIRM_SELL_1 = 570;
  private static final int X_CONFIRM_SELL_2 = 640, Y_CONFIRM_SELL_2 = 570;

  private static final int PIXEL_COLOR_FISH_1 = -462618;
  private static final int PIXEL_COLOR_FISH_2 = -1582389;
  private static final int X_FISH_1 = 850, Y_FISH_1 = 370;
  private static final int X_FISH_2 = 850, Y_FISH_2 = 430;

  private static final int PIXEL_COLOR_TRASH_1 = -1;
  private static final int PIXEL_COLOR_TRASH_2 = -12466701;
  private static final int X_TRASH_1 = 850, Y_TRASH_1 = 370;
  private static final int X_TRASH_2 = 850, Y_TRASH_2 = 583;

  private static final int PIXEL_COLOR_CARD_1 = -1;
  private static final int PIXEL_COLOR_CARD_2 = -12466701;
  private static final int X_CARD_1 = 870, Y_CARD_1 = 595;
  private static final int X_CARD_2 = 1032, Y_CARD_2 = 595;

  private static final int PIXEL_COLOR_OPEN_ALL_CARD_1 = -12466701;
  private static final int PIXEL_COLOR_OPEN_ALL_CARD_2 = -15031331;
  private static final int X_OPEN_ALL_CARD_1 = 145, Y_OPEN_ALL_CARD_1 = 683;
  private static final int X_OPEN_ALL_CARD_2 = 145, Y_OPEN_ALL_CARD_2 = 694;

  private static final int PIXEL_COLOR_CONFIRM_CARD_1 = -12466701;
  private static final int PIXEL_COLOR_CONFIRM_CARD_2 = -15031331;
  private static final int X_CONFIRM_CARD_1 = 640, Y_CONFIRM_CARD_1 = 640;
  private static final int X_CONFIRM_CARD_2 = 640, Y_CONFIRM_CARD_2 = 657;

  private static boolean isMatchPixel(Image screenshot, int x, int y, int color) {
    return screenshot.getPixel(x, y) == color;
  }

  public static boolean isMatchBag(Image screenshot) {
    return isMatchPixel(screenshot, X_BAG, Y_BAG, PIXEL_COLOR_BAG);
  }

  public static boolean isMatchConfirmDialog(Image screenshot) {
    return isMatchPixel(screenshot, X_CONFIRM_DIALOG_1, Y_CONFIRM_DIALOG_1, PIXEL_COLOR_CONFIRM_DIALOG_1) &&
           isMatchPixel(screenshot, X_CONFIRM_DIALOG_2, Y_CONFIRM_DIALOG_2, PIXEL_COLOR_CONFIRM_DIALOG_2);
  }

  public static boolean isMatchConfirmSell(Image screenshot) {
    return isMatchPixel(screenshot, X_CONFIRM_SELL_1, Y_CONFIRM_SELL_1, PIXEL_COLOR_CONFIRM_SELL_1) &&
           isMatchPixel(screenshot, X_CONFIRM_SELL_2, Y_CONFIRM_SELL_2, PIXEL_COLOR_CONFIRM_SELL_2);
  }

  public static boolean isMatchFish(Image screenshot) {
    return isMatchPixel(screenshot, X_FISH_1, Y_FISH_1, PIXEL_COLOR_FISH_1) &&
           isMatchPixel(screenshot, X_FISH_2, Y_FISH_2, PIXEL_COLOR_FISH_2);
  }

  public static boolean isMatchTrash(Image screenshot) {
    return isMatchPixel(screenshot, X_TRASH_1, Y_TRASH_1, PIXEL_COLOR_TRASH_1) &&
           isMatchPixel(screenshot, X_TRASH_2, Y_TRASH_2, PIXEL_COLOR_TRASH_2);
  }

  public static boolean isMatchCard(Image screenshot) {
    return isMatchPixel(screenshot, X_CARD_1, Y_CARD_1, PIXEL_COLOR_CARD_1) &&
           isMatchPixel(screenshot, X_CARD_2, Y_CARD_2, PIXEL_COLOR_CARD_2);
  }

  public static boolean isMatchOpenAllCard(Image screenshot) {
    return isMatchPixel(screenshot, X_OPEN_ALL_CARD_1, Y_OPEN_ALL_CARD_1, PIXEL_COLOR_OPEN_ALL_CARD_1) &&
           isMatchPixel(screenshot, X_OPEN_ALL_CARD_2, Y_OPEN_ALL_CARD_2, PIXEL_COLOR_OPEN_ALL_CARD_2);
  }

  public static boolean isMatchConfirmCard(Image screenshot) {
    return isMatchPixel(screenshot, X_CONFIRM_CARD_1, Y_CONFIRM_CARD_1, PIXEL_COLOR_CONFIRM_CARD_1) &&
           isMatchPixel(screenshot, X_CONFIRM_CARD_2, Y_CONFIRM_CARD_2, PIXEL_COLOR_CONFIRM_CARD_2);
  }
}
