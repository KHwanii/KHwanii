from django.db import models
from django.contrib.auth.models import UserManager, AbstractBaseUser, PermissionsMixin
from django.utils import timezone


class CustomUserManager(UserManager) :
    def create_user(self, userid, password, email, name, gender, nationality) :         # 일반 유저 생성. 유저 생성 시 필요한 매개변수 순서 확인할 것!(다른 함수 생성 시 입력순서 중요)
        if not userid :
            raise ValueError("아이디는 필수항목입니다.")
        if not name :
            raise ValueError("이름은 필수항목입니다.")
        if not gender :
            raise ValueError("성별은 필수항목입니다.")
        if not nationality :
            raise ValueError("국적은 필수항목입니다.")

        email = self.normalize_email(email)                                             # 이메일은 이메일 형식으로 변환
        user = self.model(userid=userid, email=email, name=name, gender=gender, nationality=nationality)    # 유저 생성시 입력한 정보를 각각 커스텀 유저의 정보란에 집어넣기 (데이터 입력) 
        user.set_password(password)                                                     # 입력받은 정보를 password 로 설정
        user.is_active = True                                                           # 유저 활성화 
        user.save(using=self._db)                                                       # db에 저장

        return user                                                                     # 유저 생성
    

    def create_superuser(self, userid, password=None, **extra_fields):                  # 슈퍼유저 생성
        extra_fields.setdefault('email', '')
        extra_fields.setdefault('name', '')
        extra_fields.setdefault('gender', '')                 
        extra_fields.setdefault('nationality', '')                                      # 커스텀유저 모델에서 만든 필드들을 슈퍼유저 생성시에 입력할 수 있게끔 해줌.

        if extra_fields.get('email') == '':
            raise ValueError('이메일은 필수항목입니다.')
        if extra_fields.get('name') == '':
            raise ValueError('이름은 필수항목입니다.')
        if extra_fields.get('gender') not in ('male', 'female'):
            raise ValueError('성별은 필수항목입니다.')
        if extra_fields.get('nationality') not in ('America', 'Korea', 'Japan', 'China', 'France', 'England', 'Germany'):
            raise ValueError('국적은 필수항목이며, 선택지 중 하나를 입력해야 합니다.')      # 입력받은 값이 없으면 필수로 다시 입력하도록 함
            
        user = self.create_user (userid=userid, password=password, **extra_fields)      # 유저 생성시 입력한 정보를 각각 커스텀 유저의 정보란에 집어넣기 (데이터 입력) 
        user.is_superuser = True
        user.is_staff = True                                                            # 슈퍼유저 및 스태프 권한 부여.
        user.save(using=self._db)

        return user

class CustomUser(AbstractBaseUser, PermissionsMixin) :
    userid = userid = models.CharField(max_length = 15, unique=True, blank=False)
    email = models.EmailField(unique=True, blank=False)
    name = models.CharField(max_length=15, blank=False)
    gender_choices = [
        ('Male', '남'),
        ('Female', '여'),
    ]
    gender = models.CharField(max_length=10, choices=gender_choices, blank=False)
    nationality_choices = [
        ('America', '미국'),
        ('Korea', '한국'),
        ('Japan', '일본'),
        ('China', '중국'),
        ('France', '프랑스'),
        ('England', '영국'),
        ('Germany', '독일'),
    ]
    nationality = models.CharField(max_length=20, choices=nationality_choices, blank=False)
    
    is_active = models.BooleanField(default=True)
    is_superuser = models.BooleanField(default=False)
    is_staff = models.BooleanField(default=False)

    date_joined = models.DateTimeField(default=timezone.now)
    last_login = models.DateTimeField(blank=True, null=True)

    objects = CustomUserManager()

    USERNAME_FIELD = 'userid'                         # 장고에서 기본적으로 username -> 아이디 로 인식하는 것을 커스텀유저에서 만든 userid 로 바꿔줌
    REQUIRED_FIELDS = ['email', 'name', 'gender', 'nationality']

    class Meta :
        ordering = ['-date_joined']
        verbose_name = 'User'
        verbose_name_plural = 'Users'