from rest_framework import serializers
from rest_framework_simplejwt import tokens
from django.contrib.auth import authenticate

from common.models import CustomUser


class UserSerializer(serializers.ModelSerializer):
    password = serializers.CharField(write_only=True)  # 비밀번호 필드 추가

    class Meta:
        model = CustomUser
        fields = ('userid', 'password', 'email', 'name', 'gender', 'nationality')     # API 서버에 보여질 필드
    

class UserRegisterSerializer(serializers.ModelSerializer):
    password2 = serializers.CharField(write_only=True)

    class Meta:
        model = CustomUser             
        fields = ('userid', 'password', 'password2', 'email', 'name', 'gender', 'nationality')         

    def validate(self, data):
        if data['password'] != data['password2'] :
            raise serializers.ValidationError("비밀번호가 일치하지 않습니다.")
        return data

    def create(self, validated_data):
        validated_data.pop('password2')   
        user = CustomUser.objects.create_user(
            validated_data['userid'],
            validated_data['password'],  
            validated_data['email'],
            validated_data['name'],
            validated_data['gender'],
            validated_data['nationality'],
        )
        return user

class UserLoginSerializer(serializers.Serializer):
    userid = serializers.CharField()
    password = serializers.CharField()                   # 로그인 시 필요한 필드들

    def validate(self, data):      # 로그인 검증하는 함수
        user = authenticate(userid=data['userid'], password=data['password'])
        if user and user.is_active:
            return user
        
        raise serializers.ValidationError("로그인 정보가 유효하지 않습니다.")