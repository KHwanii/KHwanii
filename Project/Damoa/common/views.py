from django.contrib.auth import authenticate, login
from django.shortcuts import render, redirect
from django.contrib.auth.decorators import login_required

from .forms import CustomUserForm
from django.contrib.auth.forms import PasswordChangeForm
from django.contrib.auth import update_session_auth_hash, logout
from django.contrib import messages, auth
from django.contrib.auth.hashers import check_password


def signup(request):
    """
    회원가입
    """
    if request.method == "POST":
        form = CustomUserForm(request.POST)  # 새로운 사용자 생성
        if form.is_valid():
            form.save()
            username = form.cleaned_data.get('username')  # 회원가입 화면에서 입력한 값을 얻어와
            raw_password = form.cleaned_data.get('password1')
            user = authenticate(username=username, password=raw_password) # 사용자 인증
            # 자동로그인 삭제 (오류남)
            return redirect('common:login')
    else:
        form = CustomUserForm()

    return render(request, 'common/signup.html', {'form': form})


@login_required(login_url = 'common:login')
def change_password(request):
  if request.method == "POST":
    user = request.user
    origin_password = request.POST["origin_password"]

    if check_password(origin_password, user.password):
      new_password = request.POST["new_password"]
      confirm_password = request.POST["confirm_password"]
      if new_password == confirm_password:
        user.set_password(new_password)
        user.save()
        logout(request) # 변경 성공시 로그아웃
        return redirect('common:login')
      else:
        messages.error(request, '변경할 비밀번호를 다시 확인해주세요.')

    else:
      messages.error(request, '기존 비밀번호가 일치하지 않습니다.')

    return render(request, 'common/change_password.html')
  
  else:
    return render(request, 'common/change_password.html')