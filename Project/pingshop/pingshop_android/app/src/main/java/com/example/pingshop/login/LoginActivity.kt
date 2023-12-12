package com.example.pingshop.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pingshop.R
import com.kakao.sdk.common.KakaoSdk


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

         val kakaoKey = BuildConfig.KAKAO_NATIVE_APP_KEY

        // Kakao SDK 초기화
         KakaoSdk.init(this, kakaoKey)
    }
}