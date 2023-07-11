from django import forms
from django.contrib.auth.forms import UserCreationForm
from .models import CustomUser


# 회원가입에 사용할 UserForm (UserCreateionForm을 상속)
# UserCreateionForm은 username(이름), pw1, pw2 속성을 가짐

class CustomUserForm(UserCreationForm):
    email = forms.EmailField(label="이메일")
    username = forms.CharField(max_length = 15, label="아이디")
    name = forms.CharField(max_length = 15, label="성명")
    age = forms.IntegerField(label="나이")
    ad = forms.CharField(max_length = 50, label="거주지")

    class Meta(UserCreationForm.Meta) :
        model = CustomUser
        fields = UserCreationForm.Meta.fields + ('name', 'email', 'age', 'ad')
        
