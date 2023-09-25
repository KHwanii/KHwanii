from django.shortcuts import render, redirect

# Create your views here.

def main_page_request(request) : 
    return render(request, 'pingshop/main.html')                     # 메인 페이지로 이동하는 함수