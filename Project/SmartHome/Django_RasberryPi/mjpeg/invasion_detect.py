from gpiozero import MotionSensor
from signal import pause
import cv2
from time import sleep
from datetime import datetime
import requests


INVASION_URL = 'http://192.168.45.155:8000/iot/invasion/'

def detect_invasion(request) :
    now = datetime.now()
    text = now.strftime('침입 감지 \n%Y-%m-%d %H:%M:%S')
    params = {
        'text' : text,
        'url' : 'http://192.168.45.155:8000/iot/mjpeg'
    }

    response = requests.get(INVASION_URL, params = params)
    res = response.json()

    if res['result'] == 'success' :
        print("Invasion Detected...")
    else :
        print("detection failed")
        print(res['reason'])

    pir = MotionSensor(21)
    pir.when_motion = detect_invasion
    pause()