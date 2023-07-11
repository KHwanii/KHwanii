from django.db import models
# from django.contrib.auth.models import User
from django.conf import settings
from django_summernote.models import AbstractAttachment

class Club(models.Model):
    class category(models.TextChoices):
        from django.utils.translation import gettext_lazy as _
        COOK = 'CK', _('Cook')
        CULTURE = 'CT', _('Culture')
        GAME = 'GA', _('Game')
        LANGUAGE = 'LA', _('Language')
        MUSIC = 'MU', _('Music')
        OUTDOOR = 'OD', _('Outdoor')
        SPORTS = 'SP', _('Sports')
        
    category = models.CharField(
        max_length=10,
        choices = category.choices,
        null=True
    )
    name=models.TextField('클럽이름')
    author = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE, default=1, null=True, related_name='author_club')
    def __str__(self):
        return self.name


# 게시글 모델
class Board(models.Model):
    club = models.ForeignKey(Club, on_delete=models.CASCADE)
    author = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE, default=1, null=True, related_name='author_board')
    subject = models.CharField('제목', max_length = 200,
                               help_text='게시글의 제목을 한 줄로 작성하세요.')
    content = models.TextField('내용', help_text='내용을 상세히 작성하세요.')
    create_date = models.DateTimeField('생성일')
    modify_date = models.DateTimeField(null=True, blank=True)
    voter=models.ManyToManyField(settings.AUTH_USER_MODEL, related_name='voter_board')
    event_date = models.DateTimeField('모임일',null=True, blank=True)
    
    def __str__(self):
        return self.subject


# 댓글 모델
class Reply(models.Model):
    board = models.ForeignKey(Board, on_delete=models.CASCADE)
    author = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE, default=1, null=True)
    content = models.TextField()
    create_date = models.DateTimeField('생성일')
    modify_date = models.DateTimeField(null=True, blank=True)
    # like = models.ManyToManyField(User, related_name='reply_like')

    def __str__(self):
        return self.content

# 클럽 내부 게시글 모델
class ClubPost(models.Model) :
    board = models.ForeignKey(Board, on_delete=models.CASCADE)
    content = models.TextField()
    create_date = models.DateTimeField('생성일')
