import time

from app.capture import Capture
from app.config import Config
from app.const import Const
from app.control import Control
from app.image import Image
from app.logger import Logger
from app.matcher import Matcher


class Fishing:
  def __init__(self):
    self.capture = Capture()
    self.control = Control()
    self.matcher = Matcher()
    self.logger = Logger()
    self.mark_value = (-1, -1)
    self.is_jerked = False
    self.is_open_card = False
    self.number_fail = 0

  def start(self):

    while True:
      if self.number_fail > 10:
        break

      screenshot = self.capture.take_screenshot()

      if self.matcher.is_match_confirm_dialog(screenshot):
        self.control.touch(Const.button_confirm_dialog)
        time.sleep(0.5)
        continue

      if self.matcher.is_match_bag(screenshot):
        self.control.touch(Const.button_throw_rod)
        time.sleep(3)

        screenshot = self.capture.take_screenshot()
        if self.matcher.is_match_confirm_dialog(screenshot):
          self.control.touch(Const.button_confirm_dialog)
          time.sleep(1)
          self.control.touch(Const.button_confirm_dialog)
          time.sleep(0.5)
          continue
        elif not self.matcher.is_match_bag(screenshot):
          self.is_jerked = False
          self.is_open_card = False
          self.number_fail = 0
          time.sleep(12)
          self.mark_value = self.compute_mark_value(screenshot)
        else:
          self.number_fail += 1

      if self.matcher.is_match_confirm_sell(screenshot):
        self.control.touch(Const.button_confirm_sell)
        time.sleep(0.5)
        continue

      if self.matcher.is_match_fish(screenshot):
        time.sleep(0.2)
        screenshot = self.capture.take_screenshot()
        level = self.get_fish_level(screenshot)
        is_crowned = self.matcher.is_match_crowned_fish(screenshot)
        is_new_fish = self.matcher.is_match_new_fish(screenshot)
        is_mutant_fish = self.matcher.is_match_mutant_fish(screenshot)
        is_mini_fish = self.matcher.is_match_mini_fish(screenshot)
        self.logger.i(f"type=fish level={level} is_crowned={is_crowned} is_new={is_new_fish} is_mutant={is_mutant_fish} is_mini={is_mini_fish}")

        if Config.HAS_MEMBERSHIP:
          if self.should_keep_fish(level, is_crowned, is_new_fish, is_mutant_fish, is_mini_fish):
            self.control.touch(Const.button_store2)
            time.sleep(0.5)
            continue
          else:
            self.control.touch(Const.button_sell)
            time.sleep(0.5)
            continue

        else:
          self.control.touch(Const.button_store1)
          time.sleep(0.5)
          continue

      if self.matcher.is_match_trash(screenshot):
        self.logger.i("type=trash")
        self.control.touch(Const.button_store1)
        time.sleep(0.5)
        continue

      if self.matcher.is_match_card(screenshot):
        self.logger.i("type=card")
        self.is_open_card = True
        self.control.touch(Const.button_open_card)
        time.sleep(0.5)
        continue

      if self.matcher.is_match_open_all_card(screenshot):
        self.control.touch(Const.button_open_all_card)
        time.sleep(0.5)
        continue

      if self.matcher.is_match_confirm_card(screenshot):
        self.control.touch(Const.button_confirm_card)
        time.sleep(0.5)
        continue

      if self.is_jerked and self.is_open_card:
        self.control.touch(Const.button_confirm_card)
        time.sleep(0.5)
        continue

      if self.matcher.is_match_confirm_item(screenshot):
        self.control.touch(Const.button_confirm_card)
        time.sleep(0.5)
        continue

      mark = self.compute_mark_value(screenshot)
      if self.mark_value == (-1, -1):
        self.mark_value = mark
        continue

      dental = abs((mark[1] - self.mark_value[1]) / (self.mark_value[1] + Config.STD_EPSILON))
      # if not self.is_jerked:
      #   print(f"old_mark={self.mark_value[1]} new_mark={mark[1]} dental={round(dental, 2)}")

      if not self.is_jerked and dental > Config.STD_CHANGE_THRESHOLD:
        self.logger.i(f"dental={round(dental, 2)}")
        self.control.touch(Const.button_jerk)
        self.is_jerked = True
        time.sleep(2)
        continue

      self.mark_value = mark

  def compute_mark_value(self, screenshot: Image) -> tuple[float, float]:
    total_pixel_value = 0
    offset_x = Config.MARK_POSITION_X - Config.MARK_WIDTH // 2
    offset_y = Config.MARK_POSITION_Y - Config.MARK_HEIGHT // 2
    n = 0
    for i in range(0, Config.MARK_WIDTH, 2):
      for j in range(0, Config.MARK_HEIGHT, 2):
        pixel = screenshot.get_pixel(offset_x + i, offset_y + j)
        total_pixel_value += pixel
        n += 1
    mean = total_pixel_value / n
    dental = 0
    for i in range(0, Config.MARK_WIDTH, 2):
      for j in range(0, Config.MARK_HEIGHT, 2):
        pixel = screenshot.get_pixel(offset_x + i, offset_y + j)
        difference = pixel - mean
        dental += difference * difference
    std = dental ** 0.5
    return (mean, std)

  def get_fish_level(self, screenshot: Image) -> int:
    x, y = 1090, 315
    if screenshot.get_pixel(x, y) == -1777467:
      return 0
    elif screenshot.get_pixel(x, y) == -6036377:
      return 1
    elif screenshot.get_pixel(x, y) == -10893607:
      return 2
    elif screenshot.get_pixel(x, y) == -1600536:
      return 3
    else:
      return 4

  def should_keep_fish(self, level: int, is_crowned: bool, is_new_fish: bool, is_mutant_fish: bool, is_mini_fish: bool) -> bool:
    if is_new_fish:
      return True
    if is_mini_fish:
      return False
    if level > 3:
      return True
    if level == 3 and is_crowned:
      return True
    return False
