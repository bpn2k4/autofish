package com.bpn.auto.core;


@SuppressWarnings("BusyWait")
public class Fishing {

  public static final int FISH_AND_PRESERVE_MODE = 0;
  public static final int FISH_AND_SELL_MODE = 1;
  public static final int THROW_ROD_ONLY_MODE = 2;


  private Button btnThrow;
  private Button btnPreserve;
  private Button btnJerk;
  private Button btnConfirm;
  private Button btnOpenCard;
  private Button btnConfirmOpenCard;
  private Button btnConfirmCard;

  private Template bagTemplate;
  private Template preserveTemplate;
  private Template newFishTemplate;
  private Template templateConfirmDialog;
  private Template templateOpenCard;
  private Template templateConfirmCard;

  private final Mark mark = new Mark(100, 505, 20);
  private MarkValue markValue = new MarkValue(-1, -1);
  private static final int MARK_CHANGE_THRESHOLD_DIRECTLY = 5000000;
  private static final int MARK_CHANGE_THRESHOLD = 2000000;
  private static final int MARK_STD_THRESHOLD = 1000;
  private boolean isJerk = false;
  private static final boolean isPreserveNewFish = false;

  public Fishing() {
    init();
  }

  public void start(int mode) throws Exception {
    if (mode == FISH_AND_PRESERVE_MODE) {
      startFishAndPreserve();
      return;
    }

    if (mode == FISH_AND_SELL_MODE) {
      startFishAndSell();
      return;
    }

    if (mode == THROW_ROD_ONLY_MODE) {
      startThrowRodOnly();
      return;
    }

    throw new Exception("Unknown mode");
  }

  private void startFishAndPreserve() throws InterruptedException {
    Logger.i("Start fish and preserve mode!");
    Logger.i("Using mark position x=" + mark.x + ", y=" + mark.y + ", size=" + mark.size);

    while (true) {
      Image screenshot = Capture.takeScreenshot();
      boolean isMatchConfirmDialog = Match.matchTemplate(screenshot, templateConfirmDialog);
      if (isMatchConfirmDialog) {
        Logger.i("Match confirm dialog");
        Control.touch(btnConfirm);
        Logger.i("Touch confirm button");
        continue;
      }

      boolean isMatchPreserve = Match.matchTemplate(screenshot, preserveTemplate);
      if (isMatchPreserve) {
        Logger.i("Match preserve");
        Control.touch(btnPreserve);
        Logger.i("Touch preserve button");
        Logger.i("Sleep 1000ms");
        Thread.sleep(1000);
        continue;
      }

      boolean isMatchOpenCard = Match.matchTemplate(screenshot, templateOpenCard);
      if (isMatchOpenCard) {
        Logger.i("Match open card");
        Control.touch(btnOpenCard);
        Logger.i("Touch open card button");
        Logger.i("Sleep 1000ms");
        Thread.sleep(1000);
        screenshot = Capture.takeScreenshot();
        boolean isMatchConfirmCard = Match.matchTemplate(screenshot, templateConfirmCard);
        while (!isMatchConfirmCard) {
          Control.touch(btnConfirmOpenCard);
          Thread.sleep(1000);
          screenshot = Capture.takeScreenshot();
          isMatchConfirmCard = Match.matchTemplate(screenshot, templateConfirmCard);
        }
        Control.touch(btnConfirmCard);
        continue;
      }

      boolean isMatchBag = Match.matchTemplate(screenshot, bagTemplate);
      if (isMatchBag) {
        Logger.i("Touch throw button");
        Control.touch(btnThrow);
        Logger.i("Sleep 3000ms");
        Thread.sleep(3000);
        screenshot = Capture.takeScreenshot();
        isMatchConfirmDialog = Match.matchTemplate(screenshot, templateConfirmDialog);
        if (isMatchConfirmDialog) {
          Logger.i("Match repair dialog");
          Control.touch(btnConfirm);
          Logger.i("Touch confirm button");
          Logger.i("Sleep 1000ms");
          Thread.sleep(2000);
          Logger.i("Touch confirm button");
          Control.touch(btnConfirm);
          Logger.i("Sleep 1000ms");
          Thread.sleep(1000);
          continue;
        }
        isMatchBag = Match.matchTemplate(screenshot, bagTemplate);
        if (!isMatchBag) {
          Logger.i("Throw rod successfully");
          isJerk = false;
          Logger.i("Sleep 13000ms");
          Thread.sleep(13000);
          MarkValue currentMarkValue = getCurrentMarkValue(screenshot);
          while (currentMarkValue.std > MARK_STD_THRESHOLD) {
            screenshot = Capture.takeScreenshot();
            currentMarkValue = getCurrentMarkValue(screenshot);
          }
          markValue = currentMarkValue;
          Logger.i(markValue.toLogString());
          continue;
        }
      }

      MarkValue currentMarkValue = getCurrentMarkValue(screenshot);
      int dental = Math.abs(currentMarkValue.mean - markValue.mean);
//      if (!isJerk) {
//        Logger.i("Old" + markValue.toLogString() + "new: " + currentMarkValue.toLogString() + "Dental mark=" + dental + " std=" + currentMarkValue.std);
//      }

      // Directly jerk rod
      if (markValue.mean != -1 && dental > MARK_CHANGE_THRESHOLD_DIRECTLY && !isJerk) {
        Logger.i("Dental mark=" + dental + " std=" + currentMarkValue.std);
        Logger.i("Jerk rod");
        Control.touch(btnJerk);
        isJerk = true;
        Logger.i("Sleep 2000ms");
        Thread.sleep(2000);
        continue;
      }

      // Improve change mark with weather condition like snow, rain
      if (currentMarkValue.std < MARK_STD_THRESHOLD) {
        if (markValue.mean != -1 && dental > MARK_CHANGE_THRESHOLD && !isJerk) {
          Logger.i("Dental mark=" + dental + " std=" + currentMarkValue.std);
          Logger.i("Jerk rod");
          Control.touch(btnJerk);
          isJerk = true;
          Logger.i("Sleep 2000ms");
          Thread.sleep(2000);
          continue;
        }
        markValue = currentMarkValue;
      }
    }
  }

  private void startFishAndSell() throws Exception {
    Logger.i("Is preserve new fish" + isPreserveNewFish);
    Logger.i(newFishTemplate.getName());
    throw new Exception("This will be implemented soon");
  }

  private void startThrowRodOnly() throws Exception {

    while (true) {
      Image screenshot = Capture.takeScreenshot();
      boolean isMatchConfirmDialog = Match.matchTemplate(screenshot, templateConfirmDialog);
      if (isMatchConfirmDialog) {
        Logger.i("Match confirm dialog");
        Control.touch(btnConfirm);
        Logger.i("Touch confirm button");
        continue;
      }

      boolean isMatchBag = Match.matchTemplate(screenshot, bagTemplate);
      if (isMatchBag) {
        Logger.i("Touch throw button");
        Control.touch(btnThrow);
        Logger.i("Sleep 3000ms");
        Thread.sleep(3000);
        screenshot = Capture.takeScreenshot();
        isMatchConfirmDialog = Match.matchTemplate(screenshot, templateConfirmDialog);
        if (isMatchConfirmDialog) {
          Logger.i("Match repair dialog");
          Control.touch(btnConfirm);
          Logger.i("Touch confirm button");
          Logger.i("Sleep 1000ms");
          Thread.sleep(2000);
          Logger.i("Touch confirm button");
          Control.touch(btnConfirm);
          Logger.i("Sleep 1000ms");
          Thread.sleep(1000);
          continue;
        }
        isMatchBag = Match.matchTemplate(screenshot, bagTemplate);
        if (!isMatchBag) {
          Logger.i("Throw rod successfully");
        }
      }
    }
  }

  /*
  * Get current mark value
  * Return mean and std of mark value in square
  * */
  private MarkValue getCurrentMarkValue(Image screenshot) {
    long total_pixels_value = 0;
    int offsetX = mark.x - mark.size / 2;
    int offsetY = mark.y - mark.size / 2;
    int markSize = mark.size;
    for (int x = 0; x < markSize; x++) {
      for (int y = 0; y < markSize; y++) {
        int pixel = screenshot.getPixel(offsetX + x, offsetY + y);
        total_pixels_value += pixel;
      }
    }
    int mean = (int) (total_pixels_value * 1.0 / (markSize * markSize));
    int dental = 0;
    for (int x = 0; x < markSize; x++) {
      for (int y = 0; y < markSize; y++) {
        int pixel = screenshot.getPixel(offsetX + x, offsetY + y);
        dental += (pixel - mean) * (pixel - mean);
      }
    }
    int std = (int) Math.sqrt(dental * 1.0 / markSize);
    return new MarkValue(mean, std);
  }

  private void init() {

    btnThrow = new Button("ThrowRod", 1015, 440);
    btnPreserve = new Button("Preserve", 920, 565);
    btnJerk = new Button("Jerk", 1130, 560);
    btnConfirm = new Button("Confirm", 640, 540);
    btnOpenCard = new Button("OpenCard", 1030, 570);
    btnConfirmOpenCard = new Button("ConfirmOpenCard", 140, 660);
    btnConfirmCard = new Button("ConfirmCard", 645, 615);

    bagTemplate = new Template("bag", 1210, 400, "/data/local/tmp/assets/template_bag.png");
    preserveTemplate = new Template("preserve", 875, 590, "/data/local/tmp/assets/template_preserve2.png");
//    preserveTemplate = new Template("preserve", 850, 200, "/data/local/tmp/assets/template_preserve.png");
    newFishTemplate = new Template("new_fish", 1170, 300, "/data/local/tmp/assets/template_new_fish.png");
    templateConfirmDialog = new Template("confirm_dialog", 400, 250, "/data/local/tmp/assets/template_confirm_dialog.png");
    templateOpenCard = new Template("open_card", 1055, 600, "/data/local/tmp/assets/template_open_card.png");
    templateConfirmCard = new Template("confirm_card", 690, 600, "/data/local/tmp/assets/template_confirm_card.png");

  }

  private static class MarkValue {
    private final int mean;
    private final int std;

    public MarkValue(int mean, int std) {
      this.mean = mean;
      this.std = std;
    }

    public String toLogString() {
      return "MarkValue{" +
          "mean=" + mean +
          ", std=" + std +
          '}';
    }
  }

  private static class Mark {
    private final int x;
    private final int y;
    private final int size;

    public Mark(int x, int y, int size) {
      this.x = x;
      this.y = y;
      this.size = size;
    }
  }
}
