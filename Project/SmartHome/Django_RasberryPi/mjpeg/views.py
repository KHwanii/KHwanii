from django.shortcuts import render
from django.views.generic import TemplateView
from django.views import View
from django.http import HttpResponse, StreamingHttpResponse
# from .picam import MJpegStreamCam
# from .opencv import MJpegStreamCam_cv2
from time import sleep
from .camera import MJpegStreamCam

# mjpegstream = MJpegStreamCam()
# mjpegstream = MJpegStreamCam_cv2()   # cv2 USB 카메라 사용 

# smarthome = SmartHomeControl()   # 스마트 컨트롤 클래스 인스턴스 생성 -> smarthome
# smarthome.start()                 # 스마트 컨트롤 스레드 시작

streamCam = MJpegStreamCam()

class CamView(TemplateView):
    template_name = "cam.html" # 렌더링할 템플릿 파일 경로

    def get_context_data(self): # context 변수 구성
        context = super().get_context_data()
        context["mode"] = self.request.GET.get("mode", "#")
        
        return context
    

'''class UsbCamView(TemplateView) :
    template_name = "cam.html" # 렌더링할 템플릿 파일 경로
''' 

def snapshot(request):
    sleep(0.3)
    image = streamCam.snapshot()

    return HttpResponse(image, content_type="image/jpeg")


def stream(request):
    try:
        return StreamingHttpResponse(streamCam, content_type='multipart/x-mixed-replace;boundary=--myboundary')
    
    except Exception as e:
        print(f"에러 발생: {e}")
