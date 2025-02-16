"""config URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/4.1/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path, include
from django.conf import settings
from django.conf.urls.static import static
from common import views

urlpatterns = [
    path('', views.redirect_login_page),                 # common/views.py 에서 만든 redirect 함수를 통해, 기본 페이지로 이동 시, 로그인 페이지로 이동하도록 설정
    path("admin/", admin.site.urls),
    path('api/', include('api.urls')),
    path('common/', include('common.urls')),
    path('wherever/', include('wherever.urls')),
    path('social-auth/', include('social_django.urls', namespace='social'))
] + static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)
