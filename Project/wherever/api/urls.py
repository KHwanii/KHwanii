from django.urls import path, include
from rest_framework import routers
from .views import *
from rest_framework_simplejwt.views import (
    TokenObtainPairView,
    TokenRefreshView,
)

app_name = 'api'

router = routers.DefaultRouter()
# router.register('', 함수)


urlpatterns = [
    path('', include(router.urls)),
    
    path('user_register/', UserRegisterView.as_view(), name='user_register'),
    path('user_login/', UserLoginView.as_view(), name='user_login'),
    path('kakao_login/', KakaoLoginView.as_view(), name='kakao_login'),
]
