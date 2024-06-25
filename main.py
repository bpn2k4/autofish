import os
import sys
from PySide6.QtWidgets import QApplication
from autofish import App

if __name__ == "__main__":
  os.environ["ADBUTILS_ADB_PATH"] = "D:/program/platform-tools/adb.exe"
  app = QApplication([])
  autofish_app = App()
  autofish_app.show()
  sys.exit(app.exec())
