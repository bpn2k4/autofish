import win32gui
import pyautogui

from app.button import Button
from app.const import Const


class Control:
  def __init__(self):
    self.hwnd = win32gui.FindWindow(None, Const.window_title)
    self.position = win32gui.GetWindowRect(self.hwnd)
    self.dental_left = 9
    self.dental_top = 38
    self.dental_right = 9
    self.dental_bottom = 38
    self.left = self.position[0] + self.dental_left
    self.top = self.position[1] + self.dental_top
    self.right = self.position[2] - self.dental_right
    self.bottom = self.position[3] - self.dental_bottom
    self.width = self.right - self.left
    self.height = self.bottom - self.top

  def touch(self, button: Button):
    return self._touch(button.x, button.y)

  def _touch(self, x: int, y: int):
    _x, _y = self.left + x, self.top + y
    pyautogui.click(_x, _y)
