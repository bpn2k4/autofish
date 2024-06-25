import socket
import subprocess
import time
from adbutils import AdbDevice, AdbConnection, adb, Network, adb_path


class Emulator:
  def __init__(self, device_name: str = "emulator-5554"):
    self.device_name = device_name
    self.max_width = 0
    self.max_fps = 60
    self.bitrate = 1000000000
    self.adb: AdbDevice = adb.device(serial=device_name)
    self.resolution = (1920, 1080)
    self.alive = True
    self.current_frame = None
    self.adb_path = adb_path()

  def start(self):
    self.alive = True
    self.start_scrcpy_server()
    self.init_socket_connection()

  def init_socket_connection(self):
    time.sleep(0.5)
    self.video_socket: socket.socket = self.adb.create_connection(
        network=Network.LOCAL_ABSTRACT,
        address="scrcpy"
    )
    time.sleep(0.2)
    self.video_socket.recv(1)
    print("Video socket connected!")
    self.control_socket: socket.socket = self.adb.create_connection(
        network=Network.LOCAL_ABSTRACT,
        address="scrcpy"
    )
    self.video_socket.recv(64).decode("utf-8").rstrip("\x00")
    self.video_socket.recv(4)
    self.video_socket.setblocking(False)
    print("Control socket connected!")

  def start_scrcpy_server(self):
    scrcpy_file = "scrcpy-server.2.4.jar"
    file_source_path = f"autofish/{scrcpy_file}"
    file_target_path = f"/data/local/tmp/"
    copy_file_command = f"f{self.adb_path} -s {self.device_name} push {file_source_path} {file_target_path}"
    subprocess.check_output(copy_file_command)
    start_server_command = [
        f"CLASSPATH=/data/local/tmp/{scrcpy_file}",
        "app_process",
        "/",
        "com.genymobile.scrcpy.Server",
        "2.4",
        "log_level=info",
        f"max_size={self.max_width}",
        f"max_fps={self.max_fps}",
        f"video_bit_rate={self.bitrate}",
        "tunnel_forward=true",
        "send_frame_meta=false",
        "control=true",
        "audio=false",
        "show_touches=false",
        "stay_awake=false",
        "power_off_on_close=false",
        "clipboard_autosync=false"
    ]
    self.server_socket: AdbConnection = self.adb.shell(
        start_server_command,
        stream=True
    )
    self.server_socket.read(10)
    print("Scrcpy server started!")

  def stop(self):
    self.server_socket.close()
    print("Scrcpy server stopped!")
    self.video_socket.close()
    print("Video socket closed!")
    self.control_socket.close()
    print("Control socket closed!")
    self.alive = False
