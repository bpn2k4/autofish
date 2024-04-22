from typing import List
from PyQt6.QtWidgets import QApplication, QWidget
import sys


class App(QApplication):
  def __init__(self, argv: List[str]) -> None:
    super().__init__(argv)


if __name__ == '__main__':
  app = App(sys.argv)
  window = QWidget()
  window.show()
  app.exec()
