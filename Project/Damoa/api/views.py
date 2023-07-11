from django.contrib.auth import authenticate
from rest_framework import views, status
from rest_framework.response import Response
from common.models import CustomUser
from .serializers import CustomUserSerializer
from django.shortcuts import render, redirect

class RegisterAPI(views.APIView):
    def post(self, request):
        serializer = CustomUserSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response({"알림": "회원가입이 완료되었습니다."}, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class LoginAPI(views.APIView):
    def post(self, request):
        username = request.data.get('username')
        password = request.data.get('password')
        user = authenticate(username=username, password=password)
        if user:
            return Response({"알림": "로그인에 성공하였습니다."})
        return Response({"경고": "인증되지 않은 로그인 시도"}, status=status.HTTP_400_BAD_REQUEST)
    

'''
return redirect('common:login')
return render(request, 'common/signup.html', {'form': form})
'''