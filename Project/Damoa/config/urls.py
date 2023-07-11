
from django.contrib import admin
from django.urls import path, include
from main import views
from django_summernote import urls as summernote_urls
from django.conf import settings
from django.conf.urls.static import static

urlpatterns = [
    path("admin/", admin.site.urls),
    path("", include('main.urls')),  # / 페이지에 해당하는 urlpatterns 
    path('common/', include('common.urls')),
    path('main/', views.main, name='main'), # main/ 페이지에 해당하는 urlpatterns
    path('api/', include('api.urls')),
    path('login/', views.main, name='login'),
    path('summernote/', include('django_summernote.urls')),
] + static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)

