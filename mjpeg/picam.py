import io
import time
import numpy as np
from .picam import PiCamera

class MJpegStreamCam :
    def __init__(self, framerate=25, width=640, height=480):
        self.size = (width, height)
        self.framerate = framerate
        self.camera = PiCamera()
        self.camera.rotation = 180
        self.camera.resolution = self.size
        self.camera.framerate = self.framerate

    def snapshot(self):
        frame = io.BytesIO()
        self.camera.capture(frame, 'jpeg',use_video_port=True)
        frame.seek(0)
        return frame.getvalue()

def __iter__(self): 
    frame = io.BytesIO()
    while True:
        self.camera.capture(frame, format="jpeg", use_video_port=True)
        image = frame.getvalue()
        yield (b'--myboundary\n'
                b'Content-Type:image/jpeg\n'
                b'Content-Length: ' + f"{len(image)}".encode() + b'\n'
                b'\n' + image + b'\n')
        frame.seek(0)
        frame.truncate()
        # time.sleep(1/self.framerate)