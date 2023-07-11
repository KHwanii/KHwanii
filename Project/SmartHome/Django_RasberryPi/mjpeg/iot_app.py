import threading

import requests
import json
import time
from time import sleep
from datetime import datetime

import cv2
from gpiozero import DistanceSensor
from . import detect_people, upload

from gpiozero import Servo

import adafruit_dht

doorlock = Servo(25)
curtain = Servo(24)
heater = Servo(23)
aircon = Servo(22)
humidifier = Servo(26)
dehumidifier = Servo(27)

doorlock_api_url = "http://192.168.45.155:8000/api/control_doorlock/"  # 장고 API의 엔드포인트 URL
curtain_api_url = "http://192.168.45.155:8000/api/control_curtain/"
heater_api_url = "http://192.168.45.155:8000/api/control_heater/"
aircon_api_url = "http://192.168.45.155:8000/api/control_aircon/"
humidifier_api_url = "http://192.168.45.155:8000/api/control_humidifier/"
dehumidifier_api_url = "http://192.168.45.155:8000/api/control_dehumidifier/"   # 장고 API의 엔드포인트 URL

doorlock_state = 0
curtain_state = 0
heater_state = 0
aircon_state = 0
humidifier_state = 0
dehumidifier_state = 0

class SmartHomeControl(threading.Thread) :
    def __init__(self):
        super(SmartHomeControl, self).__init__()
        self.count = threading.active_count() + 1

        self.distance_state = False

        self.mydht11 = adafruit_dht.DHT11(19)  # Pin number 19
        self.current_status = [False, False, False, False]
        self.standard_temp = 24
        self.standard_humi = 65

    def control_doorlock(self):
        while True:
            global doorlock_api_url, doorlock
            # 서보 모터에 내려온 명령을 받아온다.
            response = requests.get(doorlock_api_url)
            change_state = False

            if response.status_code == 200:
                # 응답 성공 시 데이터 추출
                data = response.json()
                # print('data : ', data)
                doorlock_state = data['results'][0]['doorlockcontrol']
                # print('doorlock state: ', servo_state)
            else:
                # 응답 실패 시 오류 메시지 출력
                print("API 요청 실패:", response.status_code)

            # 명령 상태에 따라 모터를 움직인다.
            if doorlock_state != 0:
                doorlock.max()
                
                print("문이 열렸습니다!")
                if change_state == False and doorlock_state == 0:
                    change_state = True
                    
            else:
                doorlock.min()
                
                print("문이 닫혔습니다!")
                if change_state == False and doorlock_state == 1:
                    change_state = True
                    

            if change_state == True:
                # 서보 모터 상태 정보를 JSON 데이터로 변환
                servo_data = {
                    "doorlockcontrol": doorlock_state
                }

                headers = {'Content-Type': 'application/json'}

                # 장고 API에 HTTP POST 요청 보내기
                response = requests.post(doorlock_api_url, json=servo_data, headers=headers)
                if response.status_code == 201:
                    print("API 요청 성공")
                else:
                    print("API 요청 실패")
                
                change_state = False
                time.sleep(1)


    def measurement_temp_humi(self, mydht11):
        try:
            temp = mydht11.temperature
            humi = mydht11.humidity
            return temp, humi
        except:
            print("온습도를 측정하는데 실패했어요!")
            return None, None


    def check_temp_humi(self, standard_temp, standard_humi, mydht11, current_status):
        # 온습도의 범위를 확인
        temp_now, humi_now = self.measurement_temp_humi(mydht11) 
        print('현재 온도: ', temp_now, '현재 습도: ', humi_now)
        print('설정한 온도: ', standard_temp, '설정한 습도: ', standard_humi)
        print('\n')

        if temp_now is not None and humi_now is not None:
            if type(temp_now) != int:
                print('온도 확인 실패')
            else:
                temp_diff = standard_temp - temp_now

                if temp_diff > 2:    # 기준 온도보다 2도 더 춥다면
                    # 난방을 튼다.
                    self.heater(temp_diff)
                    current_status[0] = True

                elif temp_diff < -2:  # 기준 온도보다 2도 더 덥다면
                    # 에어컨을 튼다.
                    self.aircon(temp_diff)
                    current_status[1] = True

                else:
                    if current_status[0] == True:
                        print('heater 모드 중지')
                        current_status[0] = False

                    elif current_status[1] == True:
                        print('aircon 모드 중지')
                        current_status[1] = False

            if type(humi_now) != int:
                print('습도 확인 실패')

            else:
                humi_diff = standard_humi - humi_now

                if humi_diff > 5:    # 기준보다 더 건조하다면
                    self.humidify(humi_diff)
                    current_status[2] = True   # 가습기를 튼다.

                elif humi_diff < -5:  # 기준보다 더 습하다면
                    self.dehumidify(humi_diff)
                    current_status[3] = True   # 제습기를 튼다.

                else:
                    if current_status[2] == True:
                        print('가습 모드 중지')
                        current_status[2] = False

                    elif current_status[3] == True:
                        print('제습 모드 중지')
                        current_status[3] = False
        
            current_status_name = ['heater', 'airconditioner', 'humidifier', 'dehumidifier']

            for i in range(len(current_status)):
                print(f'[{current_status_name[i]}: {current_status[i]}]  ', end='')
            print('\n')
            print('---------------------------------------------------------------')

            self.upload(temp_now, humi_now, current_status)



    def upload(self, temp, humi, current_status):
        url_temp = 'http://192.168.45.155:8000/api/temp/'
        url_humi = 'http://192.168.45.155:8000/api/humi/'
        url_aircon = aircon_api_url
        url_heater = heater_api_url
        url_humidifier = humidifier_api_url
        url_dehumidifier = dehumidifier_api_url

        data_temp = {
            'place': 'bedroom',
            'value': temp,
            'created_at': datetime.now().isoformat()
        }

        data_humi = {
            'place': 'bedroom',
            'value': humi,
            'created_at': datetime.now().isoformat()
        }

        data_heater = {
            'heatercontrol': current_status[0]
        }

        data_aircon = {
            'airconcontrol': current_status[1]
        }

        data_humidifier = {
            'humidifiercontrol': current_status[2]
        }

        data_dehumidifier = {
            'dehumidifiercontrol': current_status[3]
        }

        try:
            headers = {'Content-Type': 'application/json'}
            response_temp = requests.post(url_temp, data=json.dumps(data_temp), headers=headers)
            response_humi = requests.post(url_humi, data=json.dumps(data_humi), headers=headers)
            response_aircon = requests.post(url_aircon, json=data_aircon, headers=headers)
            response_heater = requests.post(url_heater, json=data_heater, headers=headers)
            response_humidifier = requests.post(url_humidifier, json=data_humidifier, headers=headers)
            response_dehumidifier = requests.post(url_dehumidifier, json=data_dehumidifier, headers=headers)

            if response_temp.status_code == 201 and response_humi.status_code == 201:
                print('데이터 업로드 성공!')
            else:
                print('데이터 업로드 실패:', response_temp.status_code, response_humi.status_code,
                    response_aircon.status_code, response_heater.status_code, response_humidifier.status_code, response_dehumidifier.status_code)
        except requests.exceptions.RequestException as e:
            print('데이터 업로드 도중 오류 발생:', str(e))


    def heater(self, temp_diff):
        value = (temp_diff - 2) * 10
        if value > 99:
            print("난방기를 가장 강하게 튼다.")
        else:
            print(f"난방기를 {value}% 작동시킨다.")


    def aircon(self, temp_diff):
        value = (temp_diff + 2) * 10
        if value < -100:
            print("냉방기를 가장 강하게 튼다.")
        else:
            print(f"냉방기를 {value}% 작동시킨다.")


    def humidify(self, humi_diff):
        value = (humi_diff - 5) * 5
        if value > 99:
            print("가습기를 가장 강하게 튼다.")
        else:
            print(f"가습기를 {value}% 작동시킨다.")


    def dehumidify(self, humi_diff):
        value = (humi_diff + 5) * 5
        if value < -100:
            print("제습기를 가장 강하게 튼다.")
        else:
            print(f"제습기를 {value}% 작동시킨다.")

    def maintain_temp_humi(self):  # 기준 온도, 습도, 핀 번호
        DHT = self.mydht11
        while True:
            current_status = [False, False, False, False]   # 히터, 에어컨, 가습, 제습

            while True:
                try:
                    datetime.now()
                    standard_temp = self.get_standard_temp() or standard_temp  # 서버에서 값 받아오지 못하면 기본값 유지
                    standard_humi = self.get_standard_humi() or standard_humi  # 서버에서 값 받아오지 못하면 기본값 유지
                    self.check_temp_humi(standard_temp, standard_humi, DHT, current_status)
                    sleep(3)

                except Exception as e:  # Catch any exception
                    print(f"에러 발생: {e}")  # Print the error message


    def get_standard_temp(self):
        url = 'http://192.168.45.155:8000/api/standard_temp/' # API 서버에서 값을 받아오기

        try:
            response = requests.get(url)                 # url에서 반응이 있는 경우
            if response.status_code == 200:
                data = response.json()
                results = data['results']                        # "results" 키의 값 가져오기

                if results :                                      # "results" 리스트가 비어있지 않은 경우
                    first_result = results[0]                      # 첫 번째 요소 가져오기
                    standard_temp = first_result['standard_temp']   # "standard_temp" 값 가져오기
                    return standard_temp
                else : 
                    print("데이터가 비어있습니다")
            else:
                print('기준 온도 값을 가져오는데 실패했습니다:', response.status_code)

        except requests.exceptions.RequestException as e:
            print('기준 온도 값을 가져오는 도중 오류 발생:', str(e))


    def get_standard_humi(self):
        url = 'http://192.168.45.155:8000/api/standard_humi/'  # API 서버에서 값을 받아오기

        try:
            response = requests.get(url)             # url에서 반응이 있는 경우
            if response.status_code == 200:
                data = response.json()
                results = data['results']           # "results" 키의 값 가져오기

                if results :                                       # "results" 리스트가 비어있지 않은 경우
                    first_result = results[0]                       # 첫 번째 요소 가져오기
                    standard_humi = first_result['standard_humi']   # "standard_humi" 값 가져오기
                    return standard_humi
                else : 
                    print("데이터가 비어있습니다")
            else:
                print('기준 습도 값을 가져오는데 실패했습니다:', response.status_code)

        except requests.exceptions.RequestException as e:
            print('기준 습도 값을 가져오는 도중 오류 발생:', str(e))


    def start(self):
        self.thread_doorlock = threading.Thread(target=self.control_doorlock)
        self.thread_doorlock.daemon = True

        self.thread_temphumi = threading.Thread(target=self.maintain_temp_humi)
        self.thread_temphumi.daemon = True

        self.thread_doorlock.start()
        self.thread_temphumi.start()

        self.thread_doorlock.join()
        self.thread_temphumi.join()


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
