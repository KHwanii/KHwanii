import io
import time
import numpy as np
from picamera import PiCamera

class MJpegStreamCam :
    def __init__(self, framerate=25, width=640, height=480):
        self.size = (width, height)            # 카메라 크기(해상도) 설정
        self.framerate = framerate             # 카메라 프레임 설정

        self.camera = PiCamera()               # Picamere 사용
        self.camera.rotation = 180             # 카메라 회전
        self.camera.resolution = self.size
        self.camera.framerate = self.framerate    # 처음 설정한 크기(해상도)와 프레임을 카메라에 적용


    def snapshot(self):
        frame = io.BytesIO()
        self.camera.capture(frame, 'jpeg', use_video_port=True)
        frame.seek(0)

        return frame.getvalue()            # Byte 배열 리턴
    

    def __iter__(self) :
        frame = io.BytesIO()
        while True:
            self.camera.capture(frame, format="jpeg", use_video_port=True)
            image = frame.getvalue()
            # yield 는 마치 return 과 같다.
            yield (b'--myboundary\n'         # b는 바이너리 파일임을 뜻함.           
                b'Content-Type:image/jpeg\n'
                b'Content-Length: ' + f"{len(image)}".encode() + b'\n'
                b'\n' + image + b'\n')
            frame.seek(0)
            frame.truncate()
            # time.sleep(1/self.framerate)

    
