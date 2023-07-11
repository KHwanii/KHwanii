import cv2
import time
from gpiozero import DistanceSensor
import threading
from . import detect_people, upload

class MJpegStreamCam:
    def __init__(self, framerate=25, width=640, height=480, echo=20, trigger=21, threshold=20, update_interval=2):
        self.size = (width, height)
        self.framerate = framerate
        self.echo = echo
        self.trigger = trigger
        self.threshold = threshold
        self.update_interval = update_interval
        self.count = threading.active_count() + 1

        self.sensor = DistanceSensor(echo=self.echo, trigger=self.trigger)
        self.camera = cv2.VideoCapture(0)  # 0번 카메라 연결
        self.camera.set(cv2.CAP_PROP_FRAME_WIDTH, self.size[0])
        self.camera.set(cv2.CAP_PROP_FRAME_HEIGHT, self.size[1])

        self.distance_state = False

    def snapshot(self):
        ret, frame = self.camera.read()
        if ret:
            _, jpeg = cv2.imencode('.jpg', frame, [int(cv2.IMWRITE_JPEG_QUALITY), 90])
            return jpeg.tobytes()

    def distance(self):
        if self.distance_state == True:
            return
        self.distance_state = True

        while True:
            distance = (self.sensor).distance * 100 # cm로 측정
            print('distance: ', distance)
            if distance < 20:   # 거리가 20 이하일 경우
                jpeg = self.snapshot()  # 사진을 찍는다.
                image_name = detect_people.square_line(jpeg)    # 찍은 사진이 사람이 맞는지 확인한다.
                print('image_name : ', image_name, '\ntype :', type(image_name))
                if image_name != '인간아님': # 사람이었을 경우
                    result = upload.upload(image_name)  # 이미지 파일을 업로드한다.
                    print('result : ', result)
                
            time.sleep(self.update_interval)

    def __iter__(self):
        thread_distance = threading.Thread(target=self.distance)
        thread_distance.daemon = True
        thread_distance.start()

        while True:
            ret, frame = self.camera.read()
            if not ret:
                break
            _, jpeg = cv2.imencode('.jpg', frame, [int(cv2.IMWRITE_JPEG_QUALITY), 90])
            yield (b'--myboundary\n'
                   b'Content-Type:image/jpeg\n'
                   b'Content-Length: ' + f"{len(jpeg.tobytes())}".encode() + b'\n'
                   b'\n' + jpeg.tobytes() + b'\n')
            time.sleep(1/self.framerate)


        




