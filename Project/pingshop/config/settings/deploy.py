from .base import *

# secret setting
DEBUG = False
ALLOWED_HOSTS = ['*']

# 배포용 DB로 재설정
DATABASES = {
    "default": {
        "ENGINE": "django.db.backends.sqlite3",
        "NAME": BASE_DIR / "db.sqlite3",
    }
}
