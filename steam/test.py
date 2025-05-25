import cv2
from app.capture import Capture
from app.matcher import Matcher
from app.control import Control

capture = Capture()
matcher = Matcher()
control = Control()
image = capture.take_screenshot()
print(image.get_pixel(1087, 226))
print("is_match_bag", matcher.is_match_bag(image))
print("is_match_fish", matcher.is_match_fish(image))
print("is_match_confirm_dialog", matcher.is_match_confirm_dialog(image))
print("is_match_confirm_sell", matcher.is_match_confirm_sell(image))
print("is_match_card", matcher.is_match_card(image))
print("is_match_crown_fish", matcher.is_match_crowned_fish(image))
print("is_match_new_fish", matcher.is_match_new_fish(image))
print("is_match_open_all_card", matcher.is_match_open_all_card(image))
print("is_match_confirm_card", matcher.is_match_confirm_card(image))
print("is_match_trash", matcher.is_match_trash(image))

cv2.imwrite("test.png", image.data)
