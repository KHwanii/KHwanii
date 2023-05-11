# IOT_SERVER
mqtt, mjpeg, kakaotalk, api

Python 3.7 이상 버전 설치 후,

1. 가상환경 설정
python -m venv venv

2. 가상환경 실행
source ./venv/Scripts/activate

3. 필요 package 설치
pip install -r requirements.txt

4. migrate 명령어로 DB 생성
python manage.py makemigrations
python manage.py migrate

5. 서버 실행
python manage.py runserver

6. 브라우저로 접속
http://127.0.0.1:8000/iot
