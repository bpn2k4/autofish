from app.button import Button
from app.capture import Capture
from app.control import Control
from app.image import Image
import time
import random


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
    time.sleep(1)
