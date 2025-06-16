import win32gui
import win32ui
import numpy as np
from app.const import Const
from app.image import Image
from ctypes import windll


class Capture:
  def __init__(self):
    self.hwnd = win32gui.FindWindow(None, Const.window_title)
    self.position = win32gui.GetWindowRect(self.hwnd)
    self.width = 1280
    self.height = 720
    self.left = self.position[0] + 8
    self.top = self.position[1] + 31
    self.right = self.left + self.width
    self.bottom = self.top + self.height

  def take_screenshot(self) -> Image:
    hwnd_dc = win32gui.GetWindowDC(self.hwnd)
    mfc_dc = win32ui.CreateDCFromHandle(hwnd_dc)
    save_dc = mfc_dc.CreateCompatibleDC()
    saveBitMap = win32ui.CreateBitmap()
    saveBitMap.CreateCompatibleBitmap(mfc_dc, self.width, self.height)
    save_dc.SelectObject(saveBitMap)
    windll.user32.PrintWindow(self.hwnd, save_dc.GetSafeHdc(), 3)
    bitmap_buffer = saveBitMap.GetBitmapBits(True)
    image = np.frombuffer(bitmap_buffer, dtype=np.uint8).reshape((self.height, self.width, 4))
    win32gui.DeleteObject(saveBitMap.GetHandle())
    save_dc.DeleteDC()
    mfc_dc.DeleteDC()
    win32gui.ReleaseDC(self.hwnd, hwnd_dc)
    return Image(image)
