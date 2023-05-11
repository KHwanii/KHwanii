from rest_framework import viewsets, status
from rest_framework.response import Response
from iot.models import Sensor, SecFile, ImageFile
from .serializers import SensorSerializer, SecFileSerializer, ImageFileSerializer


class SensorViewSet(viewsets.ModelViewSet): 
    queryset = Sensor.objects.all().order_by('-id')
    serializer_class = SensorSerializer

    def create(self, request, *args, **kwargs):
        response = super().create(request, *args, **kwargs)
        queryset = Sensor.objects.all().order_by('id')
        if queryset.count() > 20:
            queryset.first().delete()
        return response


class SecFileViewSet(viewsets.ModelViewSet):
    queryset = SecFile.objects.all().order_by('-id')  
    serializer_class = SecFileSerializer  

    def create(self, request, *args, **kwargs):
        response = super().create(request, *args, **kwargs)
        queryset = SecFile.objects.all().order_by('id')
        if queryset.count() > 20:
            queryset.first().delete()
        return response


class ImageFileViewSet(viewsets.ModelViewSet):
    queryset = ImageFile.objects.all().order_by('-id')        # ID 정렬
    serializer_class = ImageFileSerializer                   # 이미지 데이터에 대한 직렬화 처리

    def create(self, request, *args, **kwargs):
        response = super().create(request, *args, **kwargs)
        queryset = ImageFile.objects.all().order_by('id')
        if queryset.count() > 20:                         # 이미지 데이터 생성시, 20개 이상이 되면 
            queryset.first().delete()                     # 가장 먼저 만들어진 데이터부터 삭제
        return response
    

    

