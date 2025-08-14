from app.image import Image


class Matcher:
  def __init__(self):
    return

  def is_match_bag(self, screenshot: Image) -> bool:
    return screenshot.get_pixel(1232, 385) == -1818302

  def is_match_confirm_dialog(self, screenshot: Image) -> bool:
    return screenshot.get_pixel(640, 515) == -12466701 and screenshot.get_pixel(470, 515) == -1

  def is_match_confirm_sell(self, screenshot: Image) -> bool:
    return screenshot.get_pixel(700, 518) == -12466701 and screenshot.get_pixel(640, 500) == -1

  def is_match_fish(self, screenshot: Image) -> bool:
    return screenshot.get_pixel(950, 370) == -462618 and screenshot.get_pixel(950, 410) == -1582389

  def is_match_card(self, screenshot: Image) -> bool:
    return screenshot.get_pixel(1090, 540) == -12466701 and screenshot.get_pixel(950, 340) == -1

  def is_match_trash(self, screenshot: Image) -> bool:
    return screenshot.get_pixel(960, 540) == -12466701 and screenshot.get_pixel(960, 420) == -1

  def is_match_open_all_card(self, screenshot: Image) -> bool:
    return screenshot.get_pixel(110, 690) == -12466701

  def is_match_confirm_card(self, screenshot: Image) -> bool:
    return screenshot.get_pixel(640, 570) == -12466701

  def is_match_crowned_fish(self, screenshot: Image) -> bool:
    return screenshot.get_pixel(1087, 226) == -469448

  def is_match_new_fish(self, screenshot: Image) -> bool:
    return screenshot.get_pixel(1195, 324) == -1349534

  def is_match_mutant_fish(self, screenshot: Image) -> bool:
    return screenshot.get_pixel(1115, 297) == -10063626

  def is_match_mini_fish(self, screenshot: Image) -> bool:
    return screenshot.get_pixel(1115, 297) == -9473

  def is_match_confirm_item(self, screenshot: Image) -> bool:
    return screenshot.get_pixel(1200, 100) in [-3949925, -11358125, -11103001, -4361762]
