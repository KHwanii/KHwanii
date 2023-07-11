import requests
import time
from time import sleep
from datetime import datetime


from gpiozero import Servo


doorlock = Servo(16)


doorlock_api_url = "http://172.30.1.78:8000/api/control_doorlock/"  # 장고 API의 엔드포인트 URL

doorlock_state = 0


def control_doorlock():
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

control_doorlock()





