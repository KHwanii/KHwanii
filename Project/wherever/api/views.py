from rest_framework import status
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.permissions import IsAuthenticated
from rest_framework_simplejwt.tokens import RefreshToken

from django.contrib.auth import authenticate
from common.models import CustomUser
from api.serializers import UserRegisterSerializer, UserLoginSerializer
import requests

class UserRegisterView(APIView) :
    def post(self, request):
        serializer = UserRegisterSerializer(data=request.data)
        if serializer.is_valid() : 
            user = serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
    

class UserLoginView(APIView) : 
    def post(self, request) :
        serializer = UserLoginSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)               # 로그인 검증 실패 시, DRF에서 HTTP응답을 자동으로 처리함. False인 경우, 오류처리를 수동으로 처리 필요
        user = serializer.validated_data
        refresh = RefreshToken.for_user(user)
        return Response({
            'refresh': str(refresh),
            'token': str(refresh.access_token)
        })


class UserLogoutView(APIView) :
    permission_classes = [IsAuthenticated]

    def post(self, request):
        # Refresh 토큰을 블랙리스트에 추가
        refresh_token = request.data['refresh']
        token = RefreshToken(refresh_token)
        token.blacklist()

        # 또는 다음과 같이 BlacklistedToken 모델을 직접 사용하여 추가할 수도 있습니다.
        # BlacklistedToken.objects.create(token=refresh_token)
        
        return Response(status=204)  # No Content
    

def get_kakao_token(code):
    # 카카오 토큰 요청 URL 및 필요한 데이터 설정
    url = "https://kauth.kakao.com/oauth/token"
    payload = {
        "grant_type": "authorization_code",
        "client_id": "YOUR_KAKAO_REST_API_KEY",
        "redirect_uri": "YOUR_REDIRECT_URI",        # YOUR_SERVER_URL/kakao_login/ 로 리다이렉트 필요
        "code": code
    }
    
    response = requests.post(url, data=payload)
    response_data = response.json()
    
    # 에러 처리
    if "error" in response_data:
        raise Exception("Failed to get Kakao token")
    
    return response_data['access_token']

def get_kakao_user_info(access_token):
    # 카카오 사용자 정보 요청 URL 및 헤더 설정
    url = "https://kapi.kakao.com/v2/user/me"
    headers = {
        "Authorization": f"Bearer {access_token}"
    }
    
    response = requests.get(url, headers=headers)
    response_data = response.json()

    # 에러 처리
    if "error" in response_data:
        raise Exception("Failed to get Kakao user info")

    return response_data


class KakaoLoginView(APIView):
    def get(self, request):
        code = request.GET.get("code")
        kakao_token = get_kakao_token(code)
        kakao_user_info = get_kakao_user_info(kakao_token)

        user, created = CustomUser.objects.get_or_create(userid=kakao_user_info['id'])

        if created:
            # 첫 로그인: 추가 정보를 입력받는 페이지로 리디렉션
            return Response({"message": "Additional info required", "user_id": user.id}, status=status.HTTP_200_OK)

        # 기존 로그인 사용자: 토큰 발급
        refresh = RefreshToken.for_user(user)
        return Response({
            'refresh': str(refresh),
            'token': str(refresh.access_token)
        })
