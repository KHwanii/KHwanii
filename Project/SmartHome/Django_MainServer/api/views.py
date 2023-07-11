from rest_framework import viewsets
from rest_framework.response import Response

from iot.models import *
from .serializers import *


class SecFileViewSet(viewsets.ModelViewSet):
    queryset = SecFile.objects.all().order_by('-id')  
    serializer_class = SecFileSerializer  

    def create(self, request, *args, **kwargs):
        response = super().create(request, *args, **kwargs)
        queryset = SecFile.objects.all().order_by('id')
        if queryset.count() >= 20:
            queryset.first().delete()
        return response


class ImageFileViewSet(viewsets.ModelViewSet):
    queryset = ImageFile.objects.all().order_by('-id')        # ID 정렬
    serializer_class = ImageFileSerializer                   # 이미지 데이터에 대한 직렬화 처리

    def create(self, request, *args, **kwargs):
        response = super().create(request, *args, **kwargs)
        queryset = ImageFile.objects.all().order_by('id')
        if queryset.count() >= 20:                         # 이미지 데이터 생성시, 20개 이상이 되면 
            queryset.first().delete()                     # 가장 먼저 만들어진 데이터부터 삭제
        return response
    

class CDSViewSet(viewsets.ModelViewSet):
    queryset = CDS.objects.all().order_by('-id')        # ID 정렬
    serializer_class = CDSSerializer                   # 센서 데이터에 대한 직렬화 처리

    def create(self, request, *args, **kwargs):
        response = super().create(request, *args, **kwargs)
        queryset = CDS.objects.all().order_by('id')
        if queryset.count() >= 20:                         # 데이터 생성시, 20개 이상이 되면 
            queryset.first().delete()                     # 가장 먼저 만들어진 데이터부터 삭제
        return response
    

class TempViewSet(viewsets.ModelViewSet) :
    queryset = DHT_temp.objects.all().order_by('-id')        # ID 정렬
    serializer_class = TempSerializer
    
    def create(self, request, *args, **kwargs):
        response = super().create(request, *args, **kwargs)
        queryset = DHT_temp.objects.all().order_by('id')
        if queryset.count() >= 20:                         # 데이터 생성시, 20개 이상이 되면 
            queryset.first().delete()                     # 가장 먼저 만들어진 데이터부터 삭제
        return response
    
    

class HumiViewSet(viewsets.ModelViewSet) :
    queryset = DHT_humi.objects.all().order_by('-id')        # ID 정렬
    serializer_class = HumiSerializer
    
    def create(self, request, *args, **kwargs):
        response = super().create(request, *args, **kwargs)
        queryset = DHT_humi.objects.all().order_by('id')
        if queryset.count() >= 20:                         # 데이터 생성시, 20개 이상이 되면 
            queryset.first().delete()                     # 가장 먼저 만들어진 데이터부터 삭제
        return response
    


class ControlCurtainViewSet(viewsets.ModelViewSet):
    queryset = ControlCurtain.objects.all().order_by('-id')
    serializer_class = ControlCurtainSerializer

    def create(self, request, *args, **kwargs):
        response = super().create(request, *args, **kwargs)
        queryset = ControlCurtain.objects.all()
        if queryset.count() >= 2:                         # 데이터 생성시, 2개 이상이 되면 
            queryset.first().delete()                     # 가장 먼저 만들어진 데이터부터 삭제
        return response

    

class ControlDoorlockViewSet(viewsets.ModelViewSet):
    queryset = ControlDoorlock.objects.all().order_by('-id')
    serializer_class = ControlDoorlockSerializer

    def create(self, request, *args, **kwargs):
        response = super().create(request, *args, **kwargs)
        queryset = ControlDoorlock.objects.all()
        if queryset.count() >= 2:                         # 데이터 생성시, 2개 이상이 되면 
            queryset.first().delete()                     # 가장 먼저 만들어진 데이터부터 삭제
        return response
    

class ControlHeaterViewSet(viewsets.ModelViewSet):        # 히터
    queryset = ControlHeater.objects.all().order_by('-id')
    serializer_class = ControlHeaterSerializer

    def create(self, request, *args, **kwargs):
        response = super().create(request, *args, **kwargs)
        queryset = ControlHeater.objects.all()
        if queryset.count() >= 2:                         # 데이터 생성시, 2개 이상이 되면 
            queryset.first().delete()                     # 가장 먼저 만들어진 데이터부터 삭제
        return response
    

class ControlAirconViewSet(viewsets.ModelViewSet):           # 에어컨
    queryset = ControlAircon.objects.all().order_by('-id')
    serializer_class = ControlAirconSerializer

    def create(self, request, *args, **kwargs):
        response = super().create(request, *args, **kwargs)
        queryset = ControlAircon.objects.all()
        if queryset.count() >= 2:                         # 데이터 생성시, 2개 이상이 되면 
            queryset.first().delete()                     # 가장 먼저 만들어진 데이터부터 삭제
        return response
    

class ControlDehumidifierViewSet(viewsets.ModelViewSet):          # 제습기
    queryset = ControlDehumidifier.objects.all().order_by('-id')
    serializer_class = ControlDehumidifierSerializer

    def create(self, request, *args, **kwargs):
        response = super().create(request, *args, **kwargs)
        queryset = ControlDehumidifier.objects.all()
        if queryset.count() >= 2:                         # 데이터 생성시, 2개 이상이 되면 
            queryset.first().delete()                     # 가장 먼저 만들어진 데이터부터 삭제
        return response
    

class ControlHumidifierViewSet(viewsets.ModelViewSet):          # 가습기
    queryset = ControlHumidifier.objects.all().order_by('-id')
    serializer_class = ControlHumidifierSerializer

    def create(self, request, *args, **kwargs):
        response = super().create(request, *args, **kwargs)
        queryset = ControlHumidifier.objects.all()
        if queryset.count() >= 2:                         # 데이터 생성시, 2개 이상이 되면 
            queryset.first().delete()                     # 가장 먼저 만들어진 데이터부터 삭제
        return response
    

class StandardTemperatureViewSet(viewsets.ModelViewSet):           # 기준온도
    queryset = StandardTemperature.objects.all().order_by('-id')
    serializer_class = StandardTemperatureSerializer

    def create(self, request, *args, **kwargs):
        response = super().create(request, *args, **kwargs)
        queryset = StandardTemperature.objects.all()
        if queryset.count() >= 2:                         # 데이터 생성시, 2개 이상이 되면 
            queryset.first().delete()                     # 가장 먼저 만들어진 데이터부터 삭제
        return response
    

class StandardHumidityViewSet(viewsets.ModelViewSet):        # 기준습도
    queryset = StandardHumidity.objects.all().order_by('-id')
    serializer_class = StandardHumiditySerializer

    def create(self, request, *args, **kwargs):
        response = super().create(request, *args, **kwargs)
        queryset = StandardHumidity.objects.all()
        if queryset.count() >= 2:                         # 데이터 생성시, 2개 이상이 되면 
            queryset.first().delete()                     # 가장 먼저 만들어진 데이터부터 삭제
        return response
    


class TotalSmartControlViewSet(viewsets.ModelViewSet):        # 온습도 스마트컨트롤 통합제어
    queryset = TotalSmartControl.objects.all().order_by('-id')
    serializer_class = TotalSmartControlSerializer

    def create(self, request, *args, **kwargs):
        response = super().create(request, *args, **kwargs)
        queryset = TotalSmartControl.objects.all()
        if queryset.count() >= 2:                         # 데이터 생성시, 2개 이상이 되면 
            queryset.first().delete()                     # 가장 먼저 만들어진 데이터부터 삭제
        return response
    

class CurtainTimeViewSet(viewsets.ModelViewSet):
    queryset = CurtainTime.objects.all()
    serializer_class = CurtainTimeSerializer

    def create(self, request, *args, **kwargs):
        response = super().create(request, *args, **kwargs)
        queryset = CurtainTime.objects.all()

        if queryset.count() >= 2:                      
            queryset.first().delete()                   
        return response