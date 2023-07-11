from django.urls import path, include
from . import views
from django_summernote import urls as summernote_urls

app_name = 'main'

urlpatterns = [
    path('', views.index, name='index'),
    path('main/', views.main, name='main'),
    path('main/create/', views.club_create, name='club_create'),
    path('main/board_list/', views.board_list, name='board_list'),
    path('main/board_list/<int:board_id>/', views.detail, name='detail'),
    path('main/board_list/board/create/', views.board_create, name='board_create'),
    path('main/board_list/reply/create/<int:board_id>/', views.reply_create, name='reply_create'),
    path('main/board_list/vote/board/<int:board_id>/', views.vote_board, name='vote_board'),

    path('main/board/modify/<int:board_id>/', views.board_modify, name='board_modify'),
    path('main/board/delete/<int:board_id>/', views.board_delete, name='board_delete'),

    path('main/reply/modify/<int:reply_id>/', views.reply_modify, name='reply_modify'),
    path('main/reply/delete/<int:reply_id>/', views.reply_delete, name='reply_delete'),

    path('vote/board/<int:board_id>/', views.vote_board, name = 'vote_board'),
    path('main/delete/<int:club_id>/', views.club_delete, name='club_delete'),

    path('summernote/', include('django_summernote.urls')),
]