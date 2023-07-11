from django.db import models
from django.utils import timezone

class Sensor(models.Model):
    place = models.CharField(max_length=50)       # 설치 장소
    category = models.CharField(max_length=80) 
    value = models.FloatField()                   # 센서 값
    created_at = models.DateTimeField() 


# 녹화파일 모델
class SecFile(models.Model):
    file_name = models.CharField(max_length=100)                     # 녹화 파일명
    sec_file = models.FileField(upload_to="sec_file/%Y_%m_%d")      # 녹화일시

# 이미지파일 모델
class ImageFile(models.Model) :
    file_name = models.CharField(max_length=80)
    image_file = models.FileField(upload_to="image_file/%Y_%m_%d")
    created_at = models.DateTimeField(default=timezone.now)

# 조도센서 모델
class CDS(models.Model) :
    place = models.CharField(max_length=50)       # 설치 장소
    value = models.FloatField()                   # 밝기 값
    created_at = models.DateTimeField(default=timezone.now)           # 측정 날짜-시간


# DHT 온도 센서
class DHT_temp(models.Model):
    place = models.CharField(max_length=50)       # 설치 장소
    value = models.FloatField()                   # 센서 값
    created_at = models.DateTimeField()           # 측정 날짜-시간


# DHT 습도 센서
class DHT_humi(models.Model):
    place = models.CharField(max_length=50)       # 설치 장소
    value = models.FloatField()                   # 센서 값
    created_at = models.DateTimeField()  


# 커튼제어 모델
class ControlCurtain(models.Model):
    curtaincontrol = models.BooleanField(default=False)   

    

# 도어락제어 모델
class ControlDoorlock(models.Model):
    doorlockcontrol = models.BooleanField(default=False)   # 불린타입


# 히터제어 모델
class ControlHeater(models.Model):
    heatercontrol = models.BooleanField(default=False)   



# 에어컨제어 모델                                                    
class ControlAircon(models.Model) :
    airconcontrol = models.BooleanField(default=False)             # Temp와 연결된 기기



# 제습기제어 모델
class ControlDehumidifier(models.Model) :
    dehumidifiercontrol = models.BooleanField(default=False)



# 가습기제어 모델     
class ControlHumidifier(models.Model) : 
    humidifiercontrol = models.BooleanField(default=False)         # Humi와 연결된 기기


# 가습기제어 모델     
class StandardTemperature(models.Model) : 
    standard_temp = models.FloatField()         


# 가습기제어 모델     
class StandardHumidity(models.Model) : 
    standard_humi = models.FloatField() 


    
class TotalSmartControl(models.Model) : 
    total_control = models.BooleanField(default=False)


class CurtainTime(models.Model):
    time_day = models.DateTimeField()
    time_night = models.DateTimeField()