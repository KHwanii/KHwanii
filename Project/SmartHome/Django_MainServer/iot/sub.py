import paho.mqtt.client as mqtt
from .models import Sensor
from django.utils import timezone
from django.conf import settings

def on_connect(client, userdata, flags, rc):
    print("Connected with result code " + str(rc))
    if rc == 0:
        print('MQTT 브로커 연결 성공, iot/# 구독 신청')  # runserver해서 연결되면
        client.subscribe("iot/#")     # 연결 성공시 토픽 구독 신청
    else:
        print('연결 실패 : ', rc)


def on_message(client, userdata, msg):
    value = float(msg.payload.decode())   # byte 배열 --> str 변환
    if settings.MQTT_DEBUG_PRINT:
        print(f" {msg.topic} {value}")
        
    _, _, place, category = msg.topic.split('/')   # /를 기준으로 나눠
    # 아두이노에서 com.publish("iot/sensor/livingroom/temp", temp);

    # DB 저장
    data = Sensor(place = place, category = category, value = value,
                  created_at = timezone.now())
    data.save()

if settings.MQTT_SUBSCRIBE:
    client = mqtt.Client()
    client.on_connect = on_connect
    client.on_message = on_message

    try:
        client.connect("localhost")
        client.loop_start()
    except Exception as err:
        print('에러 : %s' %err)
