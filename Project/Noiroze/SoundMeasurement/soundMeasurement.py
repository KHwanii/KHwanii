
import os, errno, random
from datetime import datetime
import pyaudio, threading, time, requests, numpy, json
import glob
from operator import itemgetter

import boto3
from pydub import AudioSegment  # 소리 증폭을 위한 모듈
import spl_lib as spl
from scipy.signal import lfilter



# 가능한 동, 호수, 장소의 리스트
dong_list = ["101", "102", "103", "104", "105"]
ho_list = ["101", "102", "201", "202",
           "301", "302", "401", "402",
           "501", "502", "601", "602",
           "701", "702", "801", "802",
           "901", "902", "1001", "1002"]
place_list = ["방1", "방2", "거실", "주방", "안방"]

dong = "101"
ho = "401"
place = "방2"                   # 센서가 설치된 초기 값. 동, 호수, 설치장소
aws_access_key_id = os.getenv('AWS_ACCESS_KEY_ID')
aws_secret_access_key = os.getenv('AWS_SECRET_ACCESS_KEY')
Threshold = 43                  # 층간소음 기춘치 값. 오후 10시를 기준으로 변경


def change_location():                           # 동, 호수, 장소를 변경하는 함수
    global dong, ho, place                       # 전역 변수를 변경하기 위해 선언
    dong = random.choice(dong_list)
    ho = random.choice(ho_list)
    place = random.choice(place_list)
    print("Location changed to 동 {}, 호수 {}, 장소 {}".format(dong, ho, place))

def update_threshold():                         # 층간소음 기준 데시벨을 변경하는 함수
    current_time = datetime.now().time()
    global Threshold
    if current_time >= datetime.strptime('06:00:00', '%H:%M:%S').time() and current_time < datetime.strptime('22:00:00', '%H:%M:%S').time():
        Threshold = 43
    else:
        Threshold = 38
    print("현재 threshold 값:", Threshold)
    # time.sleep(60)       # 1분 후에 이 함수를 다시 실행


def is_meaningful(old, new):
    return abs(old - new) > 3       # 기존 측정값과 새로운 측정값의 차이가 3 이상일 경우에만 측정이 되도록 설정.


def upload_avg_dB(avg_dB):          # 평균 데시벨 값을 받고, 이를 서버에 업로드 하는 함수
        url_sound_level = 'http://noiroze.com/api/sound_level/'      # 업로드 할 서버의 엔드포인트
        avg_dB = round(avg_dB, 2)
        sound_data = {
            "dong": dong,
            "ho": ho,
            "place": place,
            "value": avg_dB,
            'created_at': datetime.now().isoformat()
        }                                              # 업로드 할 데이터의 형식. 메인 서버의 serializers를 참고.

        try:
            headers = {'Content-Type': 'application/json'}
            response_sound_level = requests.post(url_sound_level, data=json.dumps(sound_data), headers=headers)

            if response_sound_level.status_code == 201 :
                print('데이터 업로드 성공!')
            else:
                print('데이터 업로드 실패:', response_sound_level.status_code)

        except requests.exceptions.RequestException as e:
            print('데이터 업로드 도중 오류 발생:', str(e))

def send_audio_to_api(audio_path, dong, ho, place, value):
    url = 'http://noiroze.com/api/sound_file/'                           # 파일을 업로드 할 API 엔드포인트
    with open(audio_path, 'rb') as audio_file:                                # audio_path에 설정된 파일 가져오기
        file_name = os.path.basename(audio_path)                              # file_name을 audio_path로 설정
        files = { 
            'dong': (None, dong),
            'ho': (None, ho),
            'place': (None, place),
            'value': (None, value),
            'file_name': (None, file_name),
            'sound_file': (file_name, audio_file, 'audio/wav')                # 메인 서버의 모델 및 api/Serializer 에 따라 구성
        }
        response = requests.post(url, files=files)                            # post로 파일 업로드

    if response.status_code == 200 or response.status_code == 201:
        print("파일이 업로드 되었습니다.")
    else:
        print(f"업로드에 실패하였습니다. Response: {response.status_code}")
        print(response.content)                     # 에러 디버깅용

    s3_bucket = "noiroze-noisefile-backup"

    # S3에 파일 업로드
    s3 = boto3.client('s3')
    try:
        s3.upload_file(audio_path, s3_bucket, file_name)
        print(f"{file_name}가 S3 버킷 {s3_bucket}에 업로드 되었습니다.")
    except Exception as e:
        print(f"S3 업로드에 실패하였습니다. 에러: {e}")

def upload_files(folder_path, value, num_files=3):
    files = glob.glob(os.path.join(folder_path, "*.wav"))     # 모든 wav 파일의 경로를 가져옵니다.
    files_with_times = [(f, os.path.getmtime(f)) for f in files]  # 각 파일에 대해 생성 시간과 함께 튜플 리스트를 만듭니다.
    sorted_files_with_times = sorted(files_with_times, key=itemgetter(1), reverse=True)  # 파일을 생성 시간 순으로 정렬합니다.

    for i, (file_path, _) in enumerate(sorted_files_with_times):       # 가장 최신의 파일부터 업로드를 시작
        if i >= num_files:
            break
        send_audio_to_api(file_path, dong, ho, place, value)
        print(f"Uploaded file {file_path}")


def check_dB(old=0, error_count=0, min_decibel=100, max_decibel=0):         # 데시벨 측정 함수.
    print("Listening")
    update_threshold()

    CHUNKS = [4096, 9600]       # Use what you need
    CHUNK = CHUNKS[1]
    FORMAT = pyaudio.paInt16    # 16 bit
    CHANNEL = 1    # 1-mono / 2-stereo

    RATES = [22050, 44300, 48000]
    RATE = RATES[2]

    NUMERATOR, DENOMINATOR = spl.A_weighting(RATE)

    pa = pyaudio.PyAudio()          # 마이크 센서 초기화

    stream = pa.open(format = FORMAT,
                    channels = CHANNEL,
                    rate = RATE,
                    input = True,
                    input_device_index=2,
                    frames_per_buffer = CHUNK)

    start_5s = time.time()
    values_5s = []                  # 5초 동안의 데시벨 평균값 측정을 위한 시간변수와 리스트.

    start_1min = time.time()
    values_1min = []                 # 1분 동안의 데시벨 평균값 측정을 위한 시간변수와 리스트.

    while True:
        try:
            block = stream.read(CHUNK)          # read() 의 리턴값은 string. You need to decode it into an array later.
        except IOError as e:
            error_count += 1
            print(" (%d) Error recording: %s" % (error_count, e))
        else:
            decoded_block = numpy.frombuffer(block, 'int16')
            y = lfilter(NUMERATOR, DENOMINATOR, decoded_block)      # A-weighted filter 적용
            rms_val = spl.rms_flat(y)              # spl 라이브러리에서 계산한 값을 가져옴
            if rms_val == 0:            
                rms_val = 0.0001 # 데시벨 계산식의 값이 0이 되지 않도록 하는 예방코드
            new_decibel = 20 * numpy.log10(rms_val) + 15           # 데시벨 계산 식.

            if is_meaningful(old, new_decibel):
                old = new_decibel
                print('Current A-weighted: {:+.2f} dB'.format(new_decibel))
                max_decibel = max(max_decibel, new_decibel)
                print('Maximum A-weighted: {:+.2f} dB'.format(max_decibel))

                values_5s.append(new_decibel)
                values_1min.append(new_decibel)            # 평균 데시벨 측정을 위해 리스트에 데시벨 값을 추가.

                # 5초 동안의 평균 데시벨 값을 측정
                if time.time() - start_5s >= 5:
                    avg_5s = sum(values_5s) / len(values_5s)
                    print('5s Average A-weighted: {:+.2f} dB'.format(avg_5s))
                    values_5s.clear()
                    start_5s = time.time()

                # 1분간의 평균 데시벨 값을 측정
                if time.time() - start_1min >= 60:
                    avg_1min = sum(values_1min) / len(values_1min)
                    print('1min Average A-weighted: {:+.2f} dB'.format(avg_1min))
                    values_1min.clear()
                    start_1min = time.time()

                    upload_avg_dB(avg_1min)

                    if avg_1min >= Threshold :
                        record_date = datetime.now().strftime("%Y_%m_%d")
                        media_folder = os.path.join("media", record_date)
                        upload_thread = threading.Timer(120, upload_files, args=(media_folder, avg_1min))  # 2분 후에 upload_files 함수를 실행
                        upload_thread.start()
                        # time.sleep(120)
                        # upload_files(media_folder, avg_1min)  # 2분 후에 upload_files 함수를 실행

                    # 위치 변경 함수 호출
                    change_location()

if __name__ == '__main__':
    check_dB()



upload_thread = threading.Timer(120, upload_files, args=(media_folder, avg_1min))