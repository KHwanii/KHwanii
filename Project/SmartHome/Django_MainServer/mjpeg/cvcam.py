import numpy as np
import cv2

class MJpegStreamCam:
    def __init__(self, framerate = 25, width = 640, height = 480):
        self.size = (width, height)
        self.framerate = framerate
        self.camera = cv2.VideoCapture(0)
        # 카메라 0, 1 잡히는데 pc cam - 0

    def snapshot(self):
        ret, image = self.camera.read()  # 이미지를 성공적으로 읽어왔을 때 ret에 True 반환
        # ret - videocapture.read() 메서드 반환값
        encode_param = [int(cv2.IMWRITE_JPEG_QUALITY), 80]   # imencode로 jpeg 사용하기 위한 이미지 포맷 지정 
        is_success, jpg = cv2.imencode(".jpg", image, encode_param)
        # is_success - cv2.imencode()의 반환값 - 이미지 인코딩 성공하면 True
        return jpg.tobytes()
    
    
    def __iter__(self):
        encode_param = [int(cv2.IMWRITE_JPEG_QUALITY), 80]
        while True:
            ret, image = self.camera.read()      # 프레임 읽기
            is_success, jpg = cv2.imencode(".jpg", image, encode_param)
            # imencode() - opencv에서 이미지를 인코딩하는 함수
            # 이미지 데이터를 지정한 형식으로 인코딩해서 바이너리 데이터로 반환해
            image = jpg.tobytes()
            
            yield (b'--myboundary\n'        # b - binary data 
                   b'Content-Type:image/jpeg\n'
                   b'Content-Length: ' + f"{len(image)}".encode() + b'\n'
                   b'\n' + image + b'\n')    # 빈 문자열 하나 넣어주고 image data 넣어줘
            
            # time.sleep(1/self.framerate)   # 카메라의 프레임 속도를 제어

            