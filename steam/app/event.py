import time
import random

from app.button import Button
from app.capture import Capture
from app.control import Control
from app.image import Image


button_go = Button(1165, 605)
button_ok = Button(640, 555)


class VoyageEvent:
  def __init__(self):
    self.capture = Capture()
    self.control = Control()

  def start(self):

    while True:
      screenshot = self.capture.take_screenshot()

      if self.is_match_go(screenshot):
        self.control.touch(button_go)
        time.sleep(1)
        continue

      if self.is_match_ok(screenshot):
        self.control.touch(button_ok)
        time.sleep(1)
        continue

      self.random_touch()

  def is_match_go(self, screenshot: Image):
    return screenshot.get_pixel(1105, 600) == -40623

  def is_match_ok(self, screenshot: Image):
    return screenshot.get_pixel(640, 570) == -12466701

  def random_touch(self):
    num = random.randint(0, 15)
    i, j = num // 4, num % 4
    x = 440 + i * 130
    y = 240 + j * 130
    self.control.touch(Button(x, y))
    time.sleep(0.2)


class MysteryTownEvent:
  def __init__(self):
    self.capture = Capture()
    self.control = Control()

  def start(self):
    while True:
      screenshot = self.capture.take_screenshot()

      if self.is_match_go(screenshot):
        self.control.touch(Button(1165, 605))
        time.sleep(1)
        continue

      if self.is_match_ok(screenshot):
        self.control.touch(Button(640, 555))
        time.sleep(0.2)
        continue

      self.random_touch()

  def random_touch(self):
    screenshot = self.capture.take_screenshot()
    for y in [635, 555, 475, 395, 315, 235, 155, 75]:
      if screenshot.get_pixel(455, y) in [-9114590]:
        num = random.randint(0, 3)
        x = 455 + num * 120
        self.control.touch(Button(x, y))
        time.sleep(0.2)
        return

  def is_match_go(self, screenshot: Image):
    return screenshot.get_pixel(1105, 600) == -40623

  def is_match_ok(self, screenshot: Image):
    return screenshot.get_pixel(620, 395) == -1791676
