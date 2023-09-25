from django.urls import path, include
from rest_framework import routers
from .views import UserRegisterView, UserLoginView, KakaoLoginView, kakao_login_request

app_name = 'api'

router = routers.DefaultRouter()
# router.register('sound_level', SoundLevelViewSet)

urlpatterns = [
    path('', include(router.urls)),
    
    path('user_register/', UserRegisterView.as_view(), name='user_register'),
    path('user_login/', UserLoginView.as_view(), name='user_login'),
    path('kakao_login/', KakaoLoginView.as_view(), name='kakao_login'),
    path('kakao_login_request/', kakao_login_request, name='kakao_login_request'),
]