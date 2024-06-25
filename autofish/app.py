from PySide6.QtWidgets import QMainWindow, QLabel, QPushButton
from PySide6.QtGui import QIcon, QImage, QPixmap
from PySide6.QtCore import QTimer
import cv2
from av.codec import CodecContext

from autofish.emulator import Emulator

codec = CodecContext.create("h264", "r")


class App(QMainWindow):
  def __init__(self):
    super().__init__()

    self.current_frame: cv2.typing.MatLike = None
    self.emulator = Emulator()
    self.emulator.start()
    self.setup_ui()
    self.setup_video_stream()

  def setup_video_stream(self):
    self.video_timer = QTimer(self)
    self.video_timer.timeout.connect(self.update_video_frame)
    self.video_timer.start(1000 // 120)

  def update_video_frame(self):
    if self.emulator.alive:
      try:
        raw_h264 = self.emulator.video_socket.recv(0x10000)
        packets = codec.parse(raw_h264)
        for packet in packets:
          frames = codec.decode(packet)
          for frame in frames:
            frame: cv2.typing.MatLike = frame.to_ndarray(format="bgr24")
            self.current_frame = frame
            image = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
            height, width, channel = image.shape
            image = QImage(image.data,
                           width,
                           height,
                           channel * width,
                           QImage.Format.Format_RGB888
                           )
            pixmap = QPixmap.fromImage(image)
            self.video.setPixmap(pixmap)
      except:
        pass

  def setup_ui(self):
    self.setWindowTitle("AutoFish")
    self.setWindowIcon(QIcon("logo.png"))
    self.setFixedHeight(400)
    self.setFixedWidth(1000)

    self.video = QLabel(self)
    self.video.setGeometry(10, 10, 640, 360)
    self.video.setScaledContents(True)

    self.button_start_fishing = QPushButton("Start", self)
    self.button_start_fishing.setGeometry(660, 10, 100, 40)

    image_path = "image.png"
    image = cv2.imread(image_path)
    image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
    image = cv2.resize(image, (640, 360))
    height, width, channel = image.shape
    image = QImage(image.data,
                   width,
                   height,
                   channel * width,
                   QImage.Format.Format_RGB888
                   )
    pixmap = QPixmap.fromImage(image)
    self.video.setPixmap(pixmap)

  def closeEvent(self, event):
    self.emulator.stop()
    self.video_timer.stop()
    super().closeEvent(event)
