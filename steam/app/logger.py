from enum import Enum
from datetime import datetime


class LogLevel(Enum):
  DEBUG = 0
  INFO = 1
  WARN = 2
  ERROR = 3
  NONE = 4


class Logger:
  GREEN = "\033[92m"
  RED = "\033[91m"
  BLUE = "\033[94m"
  YELLOW = "\033[93m"
  RESET = "\033[0m"

  def __init__(self, level=LogLevel.INFO):
    self.log_level = level

  @staticmethod
  def get_current_timestamp():
    return datetime.now().strftime("%Y-%m-%d %H:%M:%S.%f")[:-3]

  def d(self, msg):
    if self.log_level.value <= LogLevel.DEBUG.value:
      time = self.get_current_timestamp()
      message = f"[{time}] {self.BLUE}[DEBUG]{self.RESET} {msg}"
      print(message)

  def i(self, msg):
    if self.log_level.value <= LogLevel.INFO.value:
      time = self.get_current_timestamp()
      message = f"[{time}] {self.GREEN}[INFO]{self.RESET} {msg}"
      print(message)

  def w(self, msg):
    if self.log_level.value <= LogLevel.WARN.value:
      time = self.get_current_timestamp()
      message = f"[{time}] {self.YELLOW}[WARN]{self.RESET} {msg}"
      print(message)

  def e(self, msg):
    if self.log_level.value <= LogLevel.ERROR.value:
      time = self.get_current_timestamp()
      message = f"[{time}] {self.RED}[ERROR]{self.RESET} {msg}"
      print(message)
