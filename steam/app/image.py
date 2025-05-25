import numpy as np


class Image:
  def __init__(self, data: np.ndarray):
    self.data = data

  def get_pixel(self, x: int, y: int) -> int:
    b, g, r = self.data[y, x]
    b, g, r, a = int(b), int(g), int(r), 255
    color = ((a & 0xff) << 24) | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff)
    if color >= (1 << 31):
      color -= (1 << 32)
    return color
