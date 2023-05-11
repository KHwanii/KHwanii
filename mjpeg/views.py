from django.shortcuts import render

from django.views.generic import TemplateView
from django.http import HttpResponse, StreamingHttpResponse
from .cvcam import MJpegStreamCam

mjpegstream = MJpegStreamCam()

class CamView(TemplateView):
    template_name = 'cam.html'    # 랜더링할 템플릿 파일 경로

    def get_context_data(self):    # context 변수 구성
        context = super().get_context_data();       # 디폴트 구성
        context["mode"] = self.request.GET.get("mode", "#")  # 나만의 context 변수 추가
        return context    # return한 context가 render(request, 'cam.html', context)로 들어가는 것

def snapshot(request):
    image = mjpegstream.snapshot()
    return HttpResponse(image, content_type="image/jpeg")

def stream(request):
    return StreamingHttpResponse(mjpegstream, 
    content_type='multipart/x-mixed-replace;boundary=--myboundary')
