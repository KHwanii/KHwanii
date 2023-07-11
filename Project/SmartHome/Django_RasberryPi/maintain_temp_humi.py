# import board  # 데이터 송신용 board모듈
import adafruit_dht
from datetime import datetime
from time import sleep
import requests
import json
from gpiozero import Servo

Aircon = Servo(16)
Aircon_api_url = 'http://172.30.1.78:8000/api/control_aircon/'

def measurement_temp_humi(mydht11):
    try:
        temp = mydht11.temperature
        humi = mydht11.humidity
        return temp, humi
    except:
        print("온습도를 측정하는데 실패했어요!")
        return None, None


def check_temp_humi(standard_temp, standard_humi, mydht11, current_status):
    # 온습도의 범위를 확인
    temp_now, humi_now = measurement_temp_humi(mydht11) 
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
                heater(temp_diff)
                current_status[0] = True

            elif temp_diff < -2:  # 기준 온도보다 2도 더 덥다면
                # 에어컨을 튼다.
                aircon(temp_diff)
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
                humidify(humi_diff)
                current_status[2] = True   # 가습기를 튼다.

            elif humi_diff < -5:  # 기준보다 더 습하다면
                dehumidify(humi_diff)
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

        upload(temp_now, humi_now, current_status)



def upload(temp, humi, current_status):
    url_temp = 'http://172.30.1.78:8000/api/temp/'
    url_humi = 'http://172.30.1.78:8000/api/humi/'
    url_aircon = 'http://172.30.1.78:8000/api/control_aircon/'
    url_heater = 'http://172.30.1.78:8000/api/control_heater/'
    url_humidifier = 'http://172.30.1.78:8000/api/control_humidifier/'
    url_dehumidifier = 'http://172.30.1.78:8000/api/control_dehumidifier/'

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


def heater(temp_diff):
    value = (temp_diff - 2) * 10
    if value > 99:
        print("난방기를 가장 강하게 튼다.")
    else:
        print(f"난방기를 {value}% 작동시킨다.")


def aircon(temp_diff):
    value = (temp_diff + 2) * 10
    if value < -100:
        print("냉방기를 가장 강하게 튼다.")
    else:
        print(f"냉방기를 {value}% 작동시킨다.")


def humidify(humi_diff):
    value = (humi_diff - 5) * 5
    if value > 99:
        print("가습기를 가장 강하게 튼다.")
    else:
        print(f"가습기를 {value}% 작동시킨다.")


def dehumidify(humi_diff):
    value = (humi_diff + 5) * 5
    if value < -100:
        print("제습기를 가장 강하게 튼다.")
    else:
        print(f"제습기를 {value}% 작동시킨다.")


def maintain_temp_humi(standard_temp=24, standard_humi=65, pin_num=19):
    mydht11 = adafruit_dht.DHT11(pin_num)
    current_status = [False, False, False, False]   # 히터, 에어컨, 가습, 제습

    while True :
        get_total_control_state()             # total_control_state 상태를 계속 받아오고 확인한다.
        sleep(1)

        if get_total_control_state() :
            try:
                datetime.now()
                standard_temp = get_standard_temp() or standard_temp  # 서버에서 값 받아오지 못하면 기본값 유지
                standard_humi = get_standard_humi() or standard_humi  # 서버에서 값 받아오지 못하면 기본값 유지
                check_temp_humi(standard_temp, standard_humi, mydht11, current_status)
                get_total_control_state()
                sleep(2)

            except KeyboardInterrupt:
                for i in range(len(current_status)):
                    current_status[i] = False
                break

        else :
            temp_now, humi_now = measurement_temp_humi(mydht11) 
            print('현재 온도: ', temp_now, '현재 습도: ', humi_now)
            print('\n')
            get_total_control_state()
            sleep(2)


def get_standard_temp():
    url = 'http://172.30.1.78:8000/api/standard_temp/' # API 서버에서 값을 받아오기

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


def get_standard_humi():
    url = 'http://172.30.1.78:8000/api/standard_humi/'  # API 서버에서 값을 받아오기

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


def get_total_control_state() :
    url = 'http://172.30.1.78:8000/api/total_control/' # API 서버에서 total_control값을 받아오기

    try:
        response = requests.get(url)                 # url에서 반응이 있는 경우
        if response.status_code == 200:
            data = response.json()
            results = data['results']                        # "results" 키의 값 가져오기

            if results :                                      # "results" 리스트가 비어있지 않은 경우
                first_result = results[0]                      # 첫 번째 요소 가져오기
                total_control = first_result['total_control']   # "total_control" 값 가져오기
                return total_control
            else : 
                print("데이터가 비어있습니다")
        else:
            print('스마트 제어상태를 확인하는데 실패했습니다:', response.status_code)

    except requests.exceptions.RequestException as e:
        print('스마트 제어상태를 확인하는 도중 오류 발생:', str(e))



# maintain_temp_humi(get_standard_temp(), get_standard_humi(), 19)   # 기준 온도, 습도, 핀 번호


import time

def control_aircon():
    while True:
        global Aircon_api_url, Aircon
        # 서보 모터에 내려온 명령을 받아온다.
        response = requests.get(Aircon_api_url)
        change_state = False

        if response.status_code == 200:
            # 응답 성공 시 데이터 추출
            data = response.json()
            # print('data : ', data)
            aircon_state = data['results'][0]['airconcontrol']
            # print('doorlock state: ', servo_state)
        else:
            # 응답 실패 시 오류 메시지 출력
            print("API 요청 실패:", response.status_code)

        # 명령 상태에 따라 모터를 움직인다.
        if aircon_state != 0:
            Aircon.max()
            
            print("에어컨 ON!")
            if change_state == False and aircon_state == 0:
                change_state = True
                
        else:
            Aircon.min()
            
            print("에어컨 OFF!")
            if change_state == False and aircon_state == 1:
                change_state = True
                

        if change_state == True:
            # 서보 모터 상태 정보를 JSON 데이터로 변환
            servo_data = {
                "airconcontrol": aircon_state
            }

            headers = {'Content-Type': 'application/json'}

            # 장고 API에 HTTP POST 요청 보내기
            response = requests.post(Aircon_api_url, json=servo_data, headers=headers)
            if response.status_code == 201:
                print("API 요청 성공")
            else:
                print("API 요청 실패")
            
            change_state = False
            time.sleep(1)

control_aircon()







