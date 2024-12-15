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

  private Mark mark = new Mark(-1, -1);
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
          Logger.i("Wait for fish eat bait");
          mark = computeCurrentMarkValue(screenshot);
          continue;
        }
      }

      Mark currentMark = computeCurrentMarkValue(screenshot);
      if (mark.getMean() == -1) {
        mark = currentMark;
        continue;
      }

      double cohenD = (mark.getMean() - currentMark.getMean()) * 1.0 / Math.sqrt((Math.pow(mark.getStd(), 2) + Math.pow(currentMark.getStd(), 2)) / 2);
      double cohenDNormalized =  1 / (1 + Math.exp(-cohenD));

      if (!isJerk) {
        // Logger.i("Old mean=" + mark.getMean() + " ,std=" + mark.getStd() + " Current mean=" + currentMark.getMean() + " ,std=" + currentMark.getStd() + " dental=" + cohenDNormalized);
      }
      if (!isJerk && (cohenDNormalized <= Config.MARK_LOWER_THRESHOLD || cohenDNormalized >= Config.MARK_UPPER_THRESHOLD) ) {
        Logger.i("Dental=" + cohenDNormalized);
        Logger.i("Jerk rod");
        Control.touch(btnJerk);
        isJerk = true;
        Logger.i("Sleep 2000ms");
        Thread.sleep(2000);
      }
      mark = currentMark;
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
  private Mark computeCurrentMarkValue(Image screenshot) {
    long total_pixels_value = 0;
    int offsetX = Config.MARK_POSITION_X - Config.MARK_WIDTH / 2;
    int offsetY = Config.MARK_POSITION_Y - Config.MARK_HEIGHT / 2;
    for (int x = 0; x < Config.MARK_WIDTH; x++) {
      for (int y = 0; y < Config.MARK_HEIGHT; y++) {
        int pixel = screenshot.getPixel(offsetX + x, offsetY + y);
        total_pixels_value += pixel;
      }
    }
    int mean = (int) (total_pixels_value / Config.MARK_SQUARE);
    long dental = 0;
    for (int x = 0; x < Config.MARK_WIDTH; x++) {
      for (int y = 0; y < Config.MARK_HEIGHT; y++) {
        int pixel = screenshot.getPixel(offsetX + x, offsetY + y);
        long difference = (long) (pixel - mean);
        dental += difference * difference;
      }
    }
    int std = (int) Math.sqrt(dental * 1.0 / Config.MARK_SQUARE);
    return new Mark(mean, std);
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
}
