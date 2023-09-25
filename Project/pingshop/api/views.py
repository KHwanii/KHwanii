from rest_framework import status
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.permissions import IsAuthenticated
from rest_framework_simplejwt.tokens import RefreshToken

from django.shortcuts import render, redirect
from django.conf import settings
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
            'refresh_token': str(refresh),
            'access_token': str(refresh.access_token)
        })


class UserLogoutView(APIView) :                 # 로그아웃 함수. 추후에 클라이언트로 옮길 예정
    permission_classes = [IsAuthenticated]

    def post(self):
        return Response(status=204)  # No Content
    

def get_kakao_token(code):
    # 카카오 토큰 요청 URL 및 필요한 데이터 설정
    url = "https://kauth.kakao.com/oauth/token"
    payload = {
        "grant_type": "authorization_code",
        "client_id": settings.KAKAO_REST_API_KEY,
        "redirect_uri": settings.REDIRECT_URI,        # SERVER_URL/kakao_login/ 로 리다이렉트 필요
        "code": code
    }
    response = requests.post(url, data=payload)
    response_data = response.json()
    
    # 에러 처리
    if "error" in response_data:
        raise Exception("카카오톡 토큰 인증 실패")
    
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
        raise Exception("사용자 정보 불러오기 실패 !")

    return response_data


def kakao_login_request(request):
    redirect_uri = settings.REDIRECT_URI
    kakao_auth_url = f"{settings.KAKAO_AUTHORIZATION_URL}?client_id={settings.KAKAO_REST_API_KEY}&redirect_uri={redirect_uri}&response_type=code"
    return redirect(kakao_auth_url)


class KakaoLoginView(APIView):
    def get(self, request):
        # print("실행확인")
        code = request.GET.get("code")
        try:
            kakao_token = get_kakao_token(code)
            kakao_user_info = get_kakao_user_info(kakao_token)
            # print(kakao_token)
            # print(kakao_user_info)

        except Exception as e:
            # 예외가 발생한 경우 오류 메시지를 출력하고 500 (내부 서버 오류) 응답을 반환합니다.
            print(f"Error: {e}")
            return Response({"error": str(e)}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

        user, created = CustomUser.objects.get_or_create(
            userid=kakao_user_info['id'],
            defaults={
                # 'name': kakao_user_info['name'],
                # 'email': kakao_user_info['email']
            }
        )

        if created or not user.gender or not user.nationality:
            context = {
                "user_id" : user.userid, 
                "알림" : "추가 정보 입력이 필요합니다.",
            }
            return render(request, 'common/additional_info.html', context)

        # 기존 로그인 사용자: 토큰 발급
        refresh = RefreshToken.for_user(user)
        return Response({
            'refresh_token': str(refresh),
            'access_token': str(refresh.access_token)
        })