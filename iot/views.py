from django.shortcuts import render
from . import sub
from django.views.generic import TemplateView
from django.http import JsonResponse   # Json으로 응답
from django.views.decorators.csrf import csrf_exempt
from .models import SecFile, ImageFile
from django.views import generic
import requests
import json

# 녹화파일 업로드 함수
@csrf_exempt
def upload_sec(request):
    if request.method == 'POST' :
        file_name = request.POST['file_name']
        sec_file = request.FILES['sec_file']
        model = SecFile(file_name = file_name, sec_file=sec_file)
        model.save()

        print('upload file', file_name, sec_file)
        msg = { 'result' : 'success' }

    else:
        msg = { 'result' : 'fail' }

    return JsonResponse(msg)


# 이미지파일 업로드 함수
@csrf_exempt
def upload_image(request):
    if request.method == 'POST':
        file_name = request.POST['file_name']
        image_file = request.FILES['image_file']
        model = ImageFile(file_name=file_name, image_file=image_file)
        model.save()

        print('upload image file', file_name, image_file)
        msg = {'result': 'success'}

    else:
        msg = {'result': 'fail'}

    return JsonResponse(msg)


# 카카오톡 메세지보내기 함수
def send_talk(text, url) :
    talk_url = "https://kapi.kakao.com/v2/api/talk/memo/default/send"
    with open("access_token.txt", 'r') as f :
        token = f.read()
    header = {"Authorization" : f"Bearer {token}"}

    # 문자열 한개, 링크 한개로 구성된 카톡 메세지 구성
    text_template = {
        'object_type' : 'text',
        'text' : text,
        'link' : {
            'web_url' : url,
            'mobile_web_url' : url
        }
    }
    payload = {'template_object' : json.dumps(text_template)}
    res = requests.post(talk_url, data=payload, headers=header)
    return res.json()

# 칩입 발견시 카톡으로 메세지 보내기 함수
def detect_invasion(request) :
    text = request.GET.get('text')
    url = request.GET.get('url')

    res = send_talk(text, url)

    if res.get('result_code') == 0:
        msg = {'result' : 'success'}
    else : 
        msg = {
            'result' : 'fail',
            'reason' : str(res),
        }

    return JsonResponse(msg)


# 녹화 파일 목록 보기
class SecFileListView(generic.ListView):
    model = SecFile
    template_name = 'iot/sec_file_list.html'
    context_object_name = 'sec_files'


# 녹화 파일 상세 보기
class SecFileDetailView(generic.DetailView):
    model = SecFile
    template_name = 'iot/sec_file_detail.html'
    context_object_name = 'vfile'



# 라베파에서 카메라로 찍은 영상 가져오는 함수
class CamView(TemplateView):
    template_name = "cam.html" # 렌더링할 템플릿 파일 경로

    def get_context_data(self):                               # context 변수 구성
        context = super().get_context_data()                  # 디폴트 구성
        context["mode"] = self.request.GET.get("mode", "#")   # 나만의 context 변수 추가
        
        return context   # return한 context가 render(request, 'cam.html', context)로 들어가는 것 
