from rest_framework import routers
from api.views import SensorViewSet, SecFileViewSet, ImageFileViewSet
from django.urls import path, include

router = routers.DefaultRouter()
router.register('sensor', SensorViewSet) # api/Sensor/(id) 로 들어오는 url 에 대하여 HTTP 메소드 처리
router.register('sec_file', SecFileViewSet) # api/sec_file/(id) 로 들어오는 url 에 대하여 HTTP 메소드 처리
router.register('image_file', ImageFileViewSet) # api/image_file/(id)

urlpatterns = [
     path('', include(router.urls))
]
