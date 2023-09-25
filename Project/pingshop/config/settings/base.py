'''
import os

# base dir: config's parent directory
BASE_DIR = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))     # BASE_DIR = pingshop
ROOT_DIR = os.path.dirname(BASE_DIR)                                                        # ROOT_DIR = Project

# Quick-start development settings - unsuitable for production
# See https://docs.djangoproject.com/en/4.1/howto/deployment/checklist/

# SECURITY WARNING: keep the secret key used in production secret!
SECRET_KEY = "django-insecure-zpckm4ej!w@r&0$^)qs#e=yw0$7sf1zeqf_2km!w2ii380t_@1"

# SECURITY WARNING: don't run with debug turned on in production!


# Application definition
INSTALLED_APPS = [
    "django.contrib.admin",
    "django.contrib.auth",
    "django.contrib.contenttypes",
    "django.contrib.sessions",
    "django.contrib.messages",
    "django.contrib.staticfiles",
    "api",
    "common",
    "pingshop",
]

MIDDLEWARE = [
    "django.middleware.security.SecurityMiddleware",
    "django.contrib.sessions.middleware.SessionMiddleware",
    "django.middleware.common.CommonMiddleware",
    "django.middleware.csrf.CsrfViewMiddleware",
    "django.contrib.auth.middleware.AuthenticationMiddleware",
    "django.contrib.messages.middleware.MessageMiddleware",
    "django.middleware.clickjacking.XFrameOptionsMiddleware",
]

ROOT_URLCONF = "config.urls"

TEMPLATES = [
    {
        "BACKEND": "django.template.backends.django.DjangoTemplates",
        "DIRS": [BASE_DIR / "templates"],
        "APP_DIRS": True,
        "OPTIONS": {
            "context_processors": [
                "django.template.context_processors.debug",
                "django.template.context_processors.request",
                "django.contrib.auth.context_processors.auth",
                "django.contrib.messages.context_processors.messages",
            ],
        },
    },
]

WSGI_APPLICATION = "config.wsgi.application"

# Password validation
# https://docs.djangoproject.com/en/4.1/ref/settings/#auth-password-validators
AUTH_PASSWORD_VALIDATORS = [
    {
        "NAME": "django.contrib.auth.password_validation.UserAttributeSimilarityValidator",
    },
    {
        "NAME": "django.contrib.auth.password_validation.MinimumLengthValidator",
    },
    {
        "NAME": "django.contrib.auth.password_validation.CommonPasswordValidator",
    },
    {
        "NAME": "django.contrib.auth.password_validation.NumericPasswordValidator",
    },
]

# Internationalization
# https://docs.djangoproject.com/en/4.1/topics/i18n/
LANGUAGE_CODE = "ko-kr"
TIME_ZONE = "Asia/Seoul"
USE_I18N = True
USE_TZ = True

# static 폴더 설정
STATIC_URL = "static/"

# media 폴더 설정
MEDIA_URL = "/media/"
MEDIA_ROOT = os.path.join(BASE_DIR, 'media')

# 커스텀 유저 설정
AUTH_USER_MODEL = "common.CustomUser"

# REST 프레임워크 설정
REST_FRAMEWORK = {
    'DEFAULT_AUTHENTICATION_CLASSES': [ # Authentication 인증 SimpleJWT 사용
        'rest_framework_simplejwt.authentication.JWTAuthentication',
    ],

    'DEFAULT_RENDERER_CLASSES': [
        'rest_framework.renderers.JSONRenderer',
    ],
    
    'DEFAULT_PARSER_CLASSES': [
        'rest_framework.parsers.JSONParser',
    ],
    
    # API 페이지네이션 설정
    'DEFAULT_PAGINATION_CLASS': 'rest_framework.pagination.PageNumberPagination',       
    'PAGE_SIZE': 100
}

from datetime import timedelta
SIMPLE_JWT = {
    'ACCESS_TOKEN_LIFETIME': timedelta(minutes=30),
    'SLIDING_TOKEN_REFRESH_LIFETIME': timedelta(days=1),
    'ROTATE_REFRESH_TOKENS': False,
    'ALGORITHM': 'HS256',
    'SIGNING_KEY': SECRET_KEY,
    'VERIFYING_KEY': None,
    'AUTH_HEADER_TYPES': ('Bearer',),
    'USER_ID_FIELD': 'id',
    'USER_ID_CLAIM': 'user_id',
    'AUTH_TOKEN_CLASSES': ('rest_framework_simplejwt.tokens.AccessToken',),
    'TOKEN_TYPE_CLAIM': 'token_type',
}

# 카카오톡 로그인 설정 openAPI
AUTHENTICATION_BACKENDS = [
    'django.contrib.auth.backends.ModelBackend',    # Django의 기본 인증 설정
    'social_core.backends.kakao.KakaoOAuth2',       # OAuth2 인증설정 추가
]       # Django의 인증 메커니즘 설정


# Default primary key field type
# https://docs.djangoproject.com/en/4.1/ref/settings/#default-auto-field
DEFAULT_AUTO_FIELD = "django.db.models.BigAutoField"


import json
# .config_secret 폴더에서 설정파일 불러오기
config_path = os.path.join(os.path.dirname(os.path.dirname(__file__)), '.config_secret', 'config.json')
with open(config_path) as f:
    config = json.load(f)

# KAKAO_DEVELOPER 설정키
KAKAO_REST_API_KEY = config["KAKAO_REST_API_KEY"]
REDIRECT_URI = config["REDIRECT_URI"]

# AWS 설정키
# AWS_ACCESS_KEY_ID = config["AWS_ACCESS_KEY"]
# AWS_SECRET_ACCESS_KEY = config["AWS_SECRET_ACCESS_KEY"]
'''