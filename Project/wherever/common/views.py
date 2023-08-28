from django.shortcuts import render, redirect
from django.conf import settings

# Create your views here.

def redirect_login_page(request) : 
    return redirect('common/login')                     # 기본 IP로 접속 시, 로그인 페이지로 이동하도록 하는 함수

def login_request(request):
    context = {
        'KAKAO_REST_API_KEY': settings.KAKAO_REST_API_KEY,
        'REDIRECT_URI': settings.REDIRECT_URI,
    }
    return render(request, 'common/login.html', context)         # template/common/login.html 로그인 페이지와 연결해주는 함수


def register_request(request):
    return render(request, 'common/register.html')      # template/common/register.html 회원가입 페이지와 연결해주는 함수