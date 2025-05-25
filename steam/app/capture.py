import win32gui
import win32ui
import win32con
import numpy as np
import dxcam
from app.image import Image
from app.const import Const


class Capture:
  def __init__(self):
    self.hwnd = win32gui.FindWindow(None, Const.window_title)
    self.position = win32gui.GetWindowRect(self.hwnd)
    self.width = 1280
    self.height = 720
    self.left = self.position[0] + 9
    self.top = self.position[1] + 38
    self.right = self.left + self.width
    self.bottom = self.top + self.height
    self.camera = dxcam.create(output_color="BGR")
    self.camera.start(region=(self.left, self.top, self.right, self.bottom))

  def take_screenshot(self) -> Image:
    image = self.camera.get_latest_frame()
    return Image(image)
