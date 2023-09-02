from django.shortcuts import render, redirect
from django.conf import settings
from . import models

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


def additional_info(request):
    if request.method == 'POST':
        user_id = request.POST.get('user_id')
        email = request.POST.get('email')
        name = request.POST.get('name')
        gender = request.POST.get('gender')
        nationality = request.POST.get('nationality')

        user = models.CustomUser.objects.get(userid=user_id)
        user.email = email
        user.name = name
        user.gender = gender
        user.nationality = nationality

        # print(user, user.email, user.name, user.gender, user.nationality)
        user.save()

    return redirect('common:login')  # 로그인 페이지로 리다이렉션