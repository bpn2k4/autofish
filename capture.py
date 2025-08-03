import win32gui
import win32ui
import numpy as np
from ctypes import windll
import cv2


hwnd = win32gui.FindWindow(None, "PLAY TOGETHER")
position = win32gui.GetWindowRect(hwnd)
width = 1280
if width == 1600:
  height = 900
else:
  height = 720
left = position[0] + 8
top = position[1] + 31
right = left + width
bottom = top + height


def capture():
  hwnd_dc = win32gui.GetWindowDC(hwnd)
  mfc_dc = win32ui.CreateDCFromHandle(hwnd_dc)
  save_dc = mfc_dc.CreateCompatibleDC()
  saveBitMap = win32ui.CreateBitmap()
  saveBitMap.CreateCompatibleBitmap(mfc_dc, width, height)
  save_dc.SelectObject(saveBitMap)
  windll.user32.PrintWindow(hwnd, save_dc.GetSafeHdc(), 3)
  bitmap_buffer = saveBitMap.GetBitmapBits(True)
  image = np.frombuffer(bitmap_buffer, dtype=np.uint8).reshape((height, width, 4))
  win32gui.DeleteObject(saveBitMap.GetHandle())
  save_dc.DeleteDC()
  mfc_dc.DeleteDC()
  win32gui.ReleaseDC(hwnd, hwnd_dc)
  return image


image = capture()
image = cv2.cvtColor(image, cv2.COLOR_BGRA2BGR)
cv2.imwrite("test.png", image)
