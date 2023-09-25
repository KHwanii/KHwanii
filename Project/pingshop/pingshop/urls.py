from django.contrib.auth import views as auth_views
from django.urls import path, include
from . import views

# 이 namespace를 통해 여러 앱 간의 URL 이름 충돌을 방지
app_name = 'pingshop'  


urlpatterns = [ 
    path('main/', views.main_page_request, name='main_page'),
]