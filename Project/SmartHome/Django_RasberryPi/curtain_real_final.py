from gpiozero import Servo
import time
import requests
from datetime import datetime



curtain_time_url = "http://172.30.1.78:8000/api/curtain_time/"
curtain_url = "http://172.30.1.78:8000/api/control_curtain/"
servo = Servo(6)
curtain_state_now = bool


def curtain_state_get():     # 커튼의 현재 상태를 받아온다.
    global curtain_url
    response = requests.get(curtain_url)

    if response.status_code == 200: # 받아오는데 성공했을 시
        data = response.json()
        curtain_order = data['results'][0]['curtaincontrol']
        # print('curtain : ', curtain_state_now)
    else:
        # 응답 실패 시 오류 메시지 출력
        print("API 요청 실패3:", response.status_code)
        curtain_order = None

    return curtain_order    # 받아온 현재 상태를 돌려준다.

def curtain_move(curtain_order):   # 커튼을 움직인다.
    global curtain_state_now
    if curtain_order != curtain_state_now:
        curtain_state_now = curtain_order
        if curtain_order == True:
            servo.max()
            curtain_state_now_post()
            print('  ->커튼이 열렸습니다.')
        else:
            servo.min()
            curtain_state_now_post()
            print('  ->커튼이 닫혔습니다.')


def curtain_control():
    global curtain_state_now, curtain_url, servo

    while True:
        # 현재 시간을 가져와 시간과 분만 가져온다.
        time_now = datetime.now().strftime("%H%M")

        # 커튼 시간 데이터 가져오기
        time_day, time_night = curtain_time_get()

        # 명령 상태에 따라 모터를 움직인다.
        print('time_now   : ', time_now)
        print('time_day   : ', time_day)
        print('time_night : ', time_night)

        time_now_int = int(time_now)
        time_day_int = int(time_day)
        time_night_int = int(time_night)

        if time_day_int - 1 <= time_now_int <= time_day_int + 1:
            curtain_move(True)
        elif time_night_int - 1 <= time_now_int <= time_night_int + 1:
            curtain_move(False)
        else:
            # 커튼 상태 데이터 가져오기
            curtain_order = curtain_state_get()
            if curtain_order is not None:
                curtain_move(curtain_order)

        time.sleep(1)



def convert_time(time):     # 서버에서 받아온 시간을 시간과 분만 알맞게 자른다.
    if type(time) == str:
        datetime_obj = datetime.strptime(time, "%Y-%m-%dT%H:%M:%S+09:00")
        time = datetime_obj.strftime("%H%M")

    return time

def curtain_time_get():     # 커튼이 열리는 시간과 닫히는 시간을 가져온다.
    global curtain_time_url
    # API 서버에 데이터가 없다면 기본값으로 대체한다.
        # 열리는 시간 기본값(아침) : 0800
        # 닫히는 시간 기본값(저녁) : 2000
    response_time = requests.get(curtain_time_url)

    if response_time.status_code == 200:
        data = response_time.json()

        time_day_data = data['results'][0]['time_day']
        time_day = convert_time(time_day_data)
        time_night_data = data['results'][0]['time_night']
        time_night = convert_time(time_night_data)
    else:
        # 응답 실패 시 오류 메시지 출력
        print("시간 데이터를 받아오지 못했습니다. : ", response_time.status_code)
        time_day = '0800'
        time_night = '2000'
    
    return time_day, time_night

def curtain_state_now_post(): # 임의로 움직인 커튼의 현재 상태를 전송한다.
    global curtain_state_now, curtain_url
    # 커튼 상태 정보를 JSON 데이터로 변환
    servo_data = {
        "curtaincontrol": curtain_state_now
    }
    headers = {'Content-Type': 'application/json'}

    # 장고 API에 HTTP POST 요청 보내기
    response = requests.post(curtain_url, json=servo_data, headers=headers)
    if response.status_code == 201:
        print("API 요청 성공2")
    else:
        print("API 요청 실패2")



def curtain_time_roof():
    while True:
        curtain_time()
        time.sleep(10)


curtain_control()
