from rest_framework import routers
from api.views import *
from django.urls import path, include



urlpatterns = [
    path('api/register/', RegisterAPI.as_view(), name='register'),
    path('api/login/', LoginAPI.as_view(), name='login'),
]


'''
router = routers.DefaultRouter()


router.register('login', CustomUserViewSet)

urlpatterns = [
     path('', include(router.urls))
]
'''