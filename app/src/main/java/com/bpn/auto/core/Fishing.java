package com.bpn.auto.core;

public class Fishing {


  private Button btnThrow;
  private Button btnPreserve;
  private Button btnJerk;
  private Button btnRepair;
  private Button btnConfirm;
  private Button btnOpenCard;
  private Button btnConfirmOpenCard;
  private Button btnConfirmCard;

  private Template bagTemplate;
  private Template preserveTemplate;
  private Template repairTemplate;
  private Template newFishTemplate;
  private Template templateConfirmDialog;
  private Template templateOpenCard;
  private Template templateConfirmOpenCard;
  private Template templateConfirmCard;

  private final int MARK_X = 100;
  private final int MARK_Y = 505;
  private int mark = -1;

  private boolean isJerk = false;

  public Fishing() {
    init();
  }


  public void start() throws InterruptedException {
    Logger.i("Start fishing");

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
          Thread.sleep(1000);
          Control.touch(btnConfirmOpenCard);
          screenshot = Capture.takeScreenshot();
          isMatchConfirmCard = Match.matchTemplate(screenshot, templateConfirmCard);
        }
        Control.touch(btnConfirmCard);
        continue;
      }

      boolean isMatchConfirmCard = Match.matchTemplate(screenshot, templateConfirmCard);
      if (isMatchConfirmCard) {
        Logger.i("Touch confirm card button");
        Control.touch(btnConfirmCard);
        Logger.i("Sleep 1000ms");
        Thread.sleep(1000);
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
          MarkValue markValue = getCurrentMarkValue(screenshot);
          while (markValue.std > 1000) {
            screenshot = Capture.takeScreenshot();
            markValue = getCurrentMarkValue(screenshot);
          }
          mark = markValue.mean;
          Logger.i("Mark value = " + mark);
          continue;
        }
      }

      MarkValue markValue = getCurrentMarkValue(screenshot);
      int dental = Math.abs(markValue.mean - mark);
      if (!isJerk) {
        Logger.i("Dental mark = " + dental);
      }
      if (mark != -1 && dental > 5000000 && !isJerk) {
        Logger.i("Jerk rod");
        Control.touch(btnJerk);
        isJerk = true;
        Logger.i("Sleep 2000ms");
        Thread.sleep(2000);
        continue;
      }
      if (markValue.std < 1000) {
        if (mark != -1 && dental > 2500000 && !isJerk) {
          Logger.i("Jerk rod");
          Control.touch(btnJerk);
          isJerk = true;
          Logger.i("Sleep 2000ms");
          Thread.sleep(2000);
          continue;
        }
        mark = markValue.mean;
      }
    }
  }

  private MarkValue getCurrentMarkValue(Image screenshot) {
    int[] pixels = screenshot.getPixels(MARK_X - 10, MARK_Y - 10, 20, 20);
    long total = 0;
    for (int pixel : pixels) {
      total += pixel;
    }
    int mean = (int) (total * 1.0 / pixels.length);
    int dental = 0;
    for (int pixel : pixels) {
      dental += (pixel - mean) * (pixel - mean);
    }
    int std = (int) Math.sqrt(dental * 1.0 / pixels.length);
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
    repairTemplate = new Template("repair", 875, 590, "/data/local/tmp/assets/template_repair.png");
    newFishTemplate = new Template("new_fish", 1170, 300, "/data/local/tmp/assets/template_new_fish.png");
    templateConfirmDialog = new Template("confirm_dialog", 400, 250, "/data/local/tmp/assets/template_confirm_dialog.png");
    templateOpenCard = new Template("open_card", 1055, 600, "/data/local/tmp/assets/template_open_card.png");
    templateConfirmOpenCard = new Template("confirm_open_card", 210, 650, "/data/local/tmp/assets/template_confirm_open_card.png");
    templateConfirmCard = new Template("confirm_card", 690, 600, "/data/local/tmp/assets/template_confirm_card.png");

  }

  private static class MarkValue {
    private final int mean;
    private final int std;
    public MarkValue(int mean, int std) {
      this.mean = mean;
      this.std = std;
    }
  }
}
