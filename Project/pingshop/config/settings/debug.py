'''
from .base import *

# secret setting
DEBUG = True
ALLOWED_HOSTS = ['*']

# 테스트용 DB 설정
DATABASES = {
    "default": {
        "ENGINE": "django.db.backends.sqlite3",
        "NAME": BASE_DIR / "db.sqlite3",
    }
}
'''

