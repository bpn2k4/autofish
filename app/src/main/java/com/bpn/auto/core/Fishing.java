package com.bpn.auto.core;

@SuppressWarnings("BusyWait")
public class Fishing {
  private int numberFailThrowRod = 0;
  private MeanStd mark = new MeanStd(-1, -1);
  private boolean isJerk = false;
  private boolean isOpenCard = false;
  private final Logger logger;

  public Fishing() {
    if (Config.DEBUG) {
      this.logger = new Logger(Logger.LogLevel.DEBUG);
    }
    else {
      this.logger = new Logger();
    }
  }

  public void start() throws InterruptedException {

    while (true) {
      if (this.numberFailThrowRod > 10) {
        logger.e("Something went wrong. Exit!");
        break;
      }

      Image screenshot = Capture.takeScreenshot();
      if (screenshot.getWidth() == 0) {
        logger.e("Something went wrong. Exit!");
        break;
      }

      if (Matcher.isMatchConfirmDialog(screenshot)) {
        logger.d("Match confirm dialog");
        logger.i("Touch confirm dialog button");
        Control.touch(Const.buttonConfirmDialog);
        logger.i("Sleep 1000ms");
        Thread.sleep(1000);
        continue;
      }

      if (Matcher.isMatchConfirmSell(screenshot)) {
        logger.d("Match confirm sell");
        logger.i("Touch confirm sell button");
        Control.touch(Const.buttonConfirmSell);
        logger.i("Sleep 1000ms");
        Thread.sleep(1000);
        continue;
      }

      if (Matcher.isMatchCard(screenshot)) {
        isOpenCard = true;
        logger.d("Match open card");
        logger.i("Touch open card button");
        Control.touch(Const.buttonOpenCard);
        logger.i("Sleep 1000ms");
        Thread.sleep(1000);
        continue;
      }

      if (Matcher.isMatchOpenAllCard(screenshot)) {
        logger.d("Match open all card");
        logger.i("Touch open all card button");
        Control.touch(Const.buttonOpenAllCard);
        logger.i("Sleep 1000ms");
        Thread.sleep(1000);
        continue;
      }

      if (Matcher.isMatchConfirmCard(screenshot)) {
        logger.d("Match confirm card");
        logger.i("Touch confirm card button");
        Control.touch(Const.buttonConfirmCard);
        logger.i("Sleep 1000ms");
        Thread.sleep(1000);
        continue;
      }

      if (Matcher.isMatchFish(screenshot)) {
        Thread.sleep(200);
        screenshot = Capture.takeScreenshot();
        int level = this.getFishLevel(screenshot);
        boolean isNewFish = this.isNewFish(screenshot);
        boolean isCrownFish = this.isCrownFish(screenshot);
        logger.i("Type=Fish Level=" + level + " New=" + isNewFish + " Crown=" + isCrownFish);
        if (Config.HAS_MEMBERSHIP) {
          if (this.shouldKeepFish(level, isCrownFish, isNewFish)) {
            logger.d("Should keep fish");
            logger.i("Touch store button");
            Control.touch(Const.buttonStore2);
            logger.i("Sleep 1000ms");
            Thread.sleep(1000);
            continue;
          }
          else {
            logger.i("Touch sell button");
            Control.touch(Const.buttonSell);
            logger.i("Sleep 1000ms");
            Thread.sleep(1000);
            continue;
          }
        }
        else {
          logger.i("Touch store button");
          Control.touch(Const.buttonStore1);
          logger.i("Sleep 1000ms");
          Thread.sleep(1000);
          continue;
        }
      }

      if (Matcher.isMatchTrash(screenshot)) {
        logger.i("Type=Trash");
        logger.i("Touch store button");
        Control.touch(Const.buttonStore1);
        logger.i("Sleep 1000ms");
        Thread.sleep(1000);
        continue;
      }

      if (Matcher.isMatchBag(screenshot)) {

        logger.d("Match bag");
        logger.i("Touch throw button");
        Control.touch(Const.buttonThrowRod);
        logger.i("Sleep 3000ms");
        Thread.sleep(3000);

        screenshot = Capture.takeScreenshot();
        if (Matcher.isMatchConfirmDialog(screenshot)) {
          logger.d("Match repair dialog");
          logger.i("Touch confirm button");
          Control.touch(Const.buttonConfirmDialog);
          logger.i("Sleep 2000ms");
          Thread.sleep(2000);
          logger.i("Touch confirm button");
          Control.touch(Const.buttonConfirmDialog);
          logger.i("Sleep 1000ms");
          Thread.sleep(1000);
          continue;
        }

        if (!Matcher.isMatchBag(screenshot)) {
          logger.i("Throw rod successfully");
          isJerk = false;
          isOpenCard = false;
          numberFailThrowRod = 0;
          logger.i("Sleep 13000ms");
          Thread.sleep(13000);
          logger.i("Wait for fish eat bait");
          mark = computeCurrentMarkValue(screenshot);
          continue;
        }
        else {
          numberFailThrowRod += 1;
        }
      }

      MeanStd currentMark = computeCurrentMarkValue(screenshot);
      if (mark.getMean() == -1) {
        mark = currentMark;
        continue;
      }

      double dental = Math.abs(mark.getStd() - currentMark.getStd()) * 1.0 / (mark.getStd() + (Config.STD_EPSILON + mark.getStd()));
      if (!isJerk) {
        logger.d("Old std=" + mark.getStd() + " Current std=" + currentMark.getStd() + " Dental=" + dental);
      }

      if (isJerk && isOpenCard) {
        logger.i("Touch confirm button");
        Control.touch(Const.buttonConfirmCard);
        logger.i("Sleep 2000ms");
        Thread.sleep(2000);
        continue;
      }

      if (!isJerk && (dental > Config.STD_CHANGE_THRESHOLD) ) {
        logger.i("Dental=" + dental);
        logger.i("Jerk rod");
        logger.i("Touch jerk button");
        Control.touch(Const.buttonJerk);
        isJerk = true;
        logger.i("Sleep 2000ms");
        Thread.sleep(2000);
      }
    }
  }

  private MeanStd computeCurrentMarkValue(Image screenshot) {
    long total_pixels_value = 0;
    int offsetX = Config.MARK_POSITION_X - Config.MARK_WIDTH / 2;
    int offsetY = Config.MARK_POSITION_Y - Config.MARK_HEIGHT / 2;
    for (int x = 0; x < Config.MARK_WIDTH; x++) {
      for (int y = 0; y < Config.MARK_HEIGHT; y++) {
        int pixel = screenshot.getPixel(offsetX + x, offsetY + y);
        total_pixels_value += pixel;
      }
    }
    long mean = total_pixels_value / Config.MARK_SQUARE;
    long dental = 0;
    for (int x = 0; x < Config.MARK_WIDTH; x++) {
      for (int y = 0; y < Config.MARK_HEIGHT; y++) {
        int pixel = screenshot.getPixel(offsetX + x, offsetY + y);
        long difference = pixel - mean;
        dental += difference * difference;
      }
    }
    long std = (long) Math.sqrt(dental * 1.0 / Config.MARK_SQUARE);
    return new MeanStd(mean, std);
  }

  private int getFishLevel(Image screenshot) {
    int x = 1030, y = 300;
    logger.d("[getFishLevel] Pixel=" + screenshot.getPixel(x, y));
    switch (screenshot.getPixel(x, y)) {
      case -1777467: return 0;
      case -6036377: return 1;
      case -10893607: return 2;
      case -1600536: return 3;
    }
    return 4;
  }

  private boolean isCrownFish(Image screenshot) {
    int pixelColor = -469448, x = 1020, y = 188;
    logger.d("[isCrownFish] Pixel=" + screenshot.getPixel(x, y));
    return screenshot.getPixel(x, y) == pixelColor;
  }

  private boolean isNewFish(Image screenshot) {
    int pixelColor = -1349534, x = 1168, y = 321;
    logger.d("[isNewFish] Pixel=" + screenshot.getPixel(x, y));
    return screenshot.getPixel(x, y) == pixelColor;
  }

  private boolean shouldKeepFish(int fishLevel, boolean isCrownFish, boolean isNewFish) {
    if (Config.SELL_ALL_FISH) {
      return false;
    }

    if (isNewFish) {
      return true;
    }

    if (fishLevel >= 3) {
      return true;
    }

    if (fishLevel == 2 && isCrownFish) {
      return true;
    }

    return false;
  }
}
