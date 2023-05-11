from django.urls import path
from mjpeg.views import *

app_name = 'mjpeg'

urlpatterns = [
    path('', CamView.as_view(template_name = 'mjpeg/cam.html')),
    path('snapshot/', snapshot, name='snapshot'),
    path('stream/', stream, name='stream'),
]
