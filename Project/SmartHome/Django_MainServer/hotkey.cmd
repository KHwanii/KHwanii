@echo off
: 실행되는 것을 보지 않음

doskey makemigrations = python manage.py makemigrations
: makemigrations만 입력해도 되게
doskey migrate = python manage.py migrate
: migrate만 입력해도 되게
doskey runserver = python manage.py runserver 0.0.0.0:8000
: 그냥 runserver만 입력해도 되게


: 이번 터미널에서만 적용되는 설정 - 새로운 터미널이 열릴 때마다 실행해줘야돼
: linux - alias 