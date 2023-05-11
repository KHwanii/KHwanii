# 일반 어플리케이션의 form 클래스 역할과 유사
# html 응답 X , json 응답 O
# 모델을 json 문자열로 변환하는 것을 직렬화(serialize)

from rest_framework import serializers 
from iot.models import Sensor, SecFile, ImageFile

class SensorSerializer(serializers.ModelSerializer): 

    class Meta: 
        model = Sensor
        fields = ('id','place','category', 'value','created_at')   # 필드 설정


class SecFileSerializer(serializers.ModelSerializer):

    class Meta:
        model = SecFile
        fields = ('file_name', 'sec_file')


class ImageFileSerializer(serializers.ModelSerializer) :

    class Meta :
        model = ImageFile
        fields = ('file_name', 'image_file', 'created_at')
