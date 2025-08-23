import time

from app.button import Button
from app.capture import Capture
from app.control import Control
from app.image import Image

button_craft = Button(860, 555)
button_confirm = Button(640, 555)
button_slot_1 = Button(80, 650)


class BaseCraft:
  def __init__(self):
    self.capture = Capture()
    self.control = Control()

  def start(self):
    pass

  def is_match_craft(self, screenshot: Image):
    return screenshot.get_pixel(500, 500) == -1 and screenshot.get_pixel(700, 500) == -922396

  def is_match_confirm(self, screenshot: Image):
    return screenshot.get_pixel(1200, 100) in [-3949925, -11358125, -11103001, -4361762]

  def is_match_done_slot_1(self, screenshot: Image):
    return screenshot.get_pixel(116, 685) == -16728548


class MonsterBalt(BaseCraft):
  def __init__(self):
    super(MonsterBalt, self).__init__()
    self.button_monster_balt = Button(875, 200)

  def start(self):
    while True:
      screenshot = self.capture.take_screenshot()

      if self.is_match_confirm(screenshot):
        self.control.touch(button_confirm)
        time.sleep(0.2)
        continue

      if self.is_match_craft(screenshot):
        self.control.touch(button_craft)
        time.sleep(0.2)
        continue

      if self.is_match_done_slot_1(screenshot):
        self.control.touch(button_slot_1)
        time.sleep(0.2)
        continue

      if self.is_match_monster_balt(screenshot):
        self.control.touch(self.button_monster_balt)
        time.sleep(0.2)
        continue

  def is_match_monster_balt(self, screenshot: Image):
    return screenshot.get_pixel(875, 175) == -4945158
