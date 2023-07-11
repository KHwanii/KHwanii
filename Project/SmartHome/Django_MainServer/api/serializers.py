# 일반 어플리케이션의 form 클래스 역할과 유사
# html 응답 X , json 응답 O
# 모델을 json 문자열로 변환하는 것을 직렬화(serialize)

from rest_framework import serializers 
from iot.models import *



class SecFileSerializer(serializers.ModelSerializer):

    class Meta:
        model = SecFile
        fields = ('file_name', 'sec_file')


class ImageFileSerializer(serializers.ModelSerializer) :

    class Meta :
        model = ImageFile
        fields = ('file_name', 'image_file', 'created_at')

    def get_created_at(self, obj):
        return obj.created_at.strftime('%y_%m_%d_%H_%M') # 날짜를 년월일시분 까지표시, String형태
    

class CDSSerializer(serializers.ModelSerializer) :

    class Meta :
        model = CDS
        fields = ('id', 'place', 'value', 'created_at')

    def get_created_at(self, obj):
        return obj.created_at.strftime('%y_%m_%d_%H_%M') # 날짜를 년월일시분 까지표시, String형태
    

class TempSerializer(serializers.ModelSerializer) :

    class Meta :
        model = DHT_temp
        fields = ('id', 'place', 'value', 'created_at')

    def get_created_at(self, obj):
        return obj.created_at.strftime('%y_%m_%d_%H_%M') # 날짜를 년월일시분 까지표시, String형태
    

class HumiSerializer(serializers.ModelSerializer) :

    class Meta :
        model = DHT_humi
        fields = ('id', 'place', 'value', 'created_at')

    def get_created_at(self, obj):
        return obj.created_at.strftime('%y_%m_%d_%H_%M') # 날짜를 년월일시분 까지표시, String형태


class ControlCurtainSerializer(serializers.ModelSerializer):      # 커튼 제어 시리얼라이저

    class Meta:
        model = ControlCurtain
        fields = ['curtaincontrol']


class ControlDoorlockSerializer(serializers.ModelSerializer):      # 도어락 제어 시리얼라이저

    class Meta:
        model = ControlDoorlock
        fields = ['doorlockcontrol']


class ControlHeaterSerializer(serializers.ModelSerializer):      # 히터 제어 시리얼라이저

    class Meta:
        model = ControlHeater
        fields = ['heatercontrol']


class ControlAirconSerializer(serializers.ModelSerializer):      # 에어컨 제어 시리얼라이저

    class Meta:
        model = ControlAircon
        fields = ['airconcontrol']


class ControlDehumidifierSerializer(serializers.ModelSerializer):      # 제습기 제어 시리얼라이저

    class Meta:
        model = ControlDehumidifier
        fields = ['dehumidifiercontrol']


class ControlHumidifierSerializer(serializers.ModelSerializer):      # 가습기 제어 시리얼라이저

    class Meta:
        model = ControlHumidifier
        fields = ['humidifiercontrol']


class StandardTemperatureSerializer(serializers.ModelSerializer):      # 가습기 제어 시리얼라이저

    class Meta:
        model = StandardTemperature
        fields = ['standard_temp']


class StandardHumiditySerializer(serializers.ModelSerializer):      # 가습기 제어 시리얼라이저

    class Meta:
        model = StandardHumidity
        fields = ['standard_humi']


class TotalSmartControlSerializer(serializers.ModelSerializer):

    class Meta:
        model = TotalSmartControl
        fields = ['total_control']


class CurtainTimeSerializer(serializers.ModelSerializer):
    class Meta:
        model = CurtainTime
        fields = ['time_day', 'time_night']
    
