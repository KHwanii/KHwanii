from django.contrib.auth import views as auth_views
from django.urls import path, include
from . import views

# 이 namespace를 통해 여러 앱 간의 URL 이름 충돌을 방지
app_name = 'common'  


urlpatterns = [ 
    path('login/', views.login_request, name='login'),  # 로그인 페이지,        common/login
    path('register/', views.register_request, name='register'),  # 로그인 페이지   common/register
    path('additional_info/', views.additional_info, name='additional_info'),
]