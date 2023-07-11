from rest_framework import routers
from api.views import *
from django.urls import path, include

router = routers.DefaultRouter()
router.register('sec_file', SecFileViewSet) # api/sec_file/(id)  로 들어오는 url 에 대하여 HTTP 메소드 처리
router.register('image_file', ImageFileViewSet) # api/image_file/(id)

router.register('cds', CDSViewSet) # api/cds/(id)
router.register('temp', TempViewSet) # api/temp/(id)
router.register('humi', HumiViewSet) # api/humi/(id)
router.register('standard_temp', StandardTemperatureViewSet) # api/standard_temp/(id)
router.register('standard_humi', StandardHumidityViewSet) # api/standard_humi/(id)



router.register('control_curtain', ControlCurtainViewSet) # api/control_curtain/(id)
router.register('control_doorlock', ControlDoorlockViewSet) # api/control_doorlock/(id)
router.register('control_heater', ControlHeaterViewSet) # api/control_heater/(id)
router.register('control_aircon', ControlAirconViewSet) # api/control_aircon/(id)
router.register('control_dehumidifier', ControlDehumidifierViewSet) # api/control_dehumidifier/(id)
router.register('control_humidifier', ControlHumidifierViewSet) # api/control_humidifier/(id)

router.register('curtain_time', CurtainTimeViewSet)

router.register('total_control', TotalSmartControlViewSet) # api/total_control/(id)




urlpatterns = [
     path('', include(router.urls))
]
