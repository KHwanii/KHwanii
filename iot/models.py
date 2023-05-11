from django.db import models
from django.utils import timezone

# 센서 모델
class Sensor(models.Model):
    place = models.CharField(max_length=50)       # 설치 장소
    category = models.CharField(max_length=50)    # 센서 종류
    value = models.FloatField()                   # 센서 값
    created_at = models.DateTimeField()           # 측정 날짜-시간

# 녹화파일 모델
class SecFile(models.Model):
    file_name = models.CharField(max_length=100)                     # 녹화 파일명
    sec_file = models.FileField(upload_to="sec_file/%Y_%m_%d")      # 녹화일시

# 이미지파일 모델
class ImageFile(models.Model) :
    file_name = models.CharField(max_length=80)
    image_file = models.FileField(upload_to="image_file/%Y_%m_%d")
    created_at = models.DateTimeField(default=timezone.now())