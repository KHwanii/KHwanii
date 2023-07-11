from django.urls import path, include
from .views import *
from django.views.generic import TemplateView

app_name = 'iot'

urlpatterns = [ 
    path('mqtt/', TemplateView.as_view(template_name='iot/mqtt.html'), name='mqtt'),
    path('invasion/', detect_invasion),
    path('upload_image/', upload_image, name='upload_image'),
    path('upload_sec/', upload_sec, name='upload_sec'),
    path('sec_file/', SecFileListView.as_view(), name='list'),
    path('sec_file/<int:pk>', SecFileDetailView.as_view(), name='detail'),
    path('mjpeg/', CamView.as_view(), name="mjpeg"),
    path('kakao/', include('kakao.urls')),
]