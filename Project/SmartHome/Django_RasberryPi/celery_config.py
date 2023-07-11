from __future__ import absolute_import, unicode_literals
import os
from celery_config import Celery


os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'iot_server.settings')  # 장고의 settings 모듈을 Celery의 기본 설정으로 사용합니다.

app = Celery('iot_server')      # 해당 프로젝트의 settings.py에 접근하게 해줌.

app.config_from_object('django.conf:settings', namespace='CELERY')


app.autodiscover_tasks()  # 장고 앱에서 @shared_task 데코레이터를 사용하여 작업을 로드하도록 합니다.