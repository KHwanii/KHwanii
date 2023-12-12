package com.example.myapp.login

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Retrofit
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// val URL = "http://172.29.49.230:8000"       // 서버 URL 설정
val URL = "http://192.168.45.48:8000"
// val URL = "https://management.noiroze.com"

object Login {
    val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()                                            // okHttp 로그 확인용 변수 선언

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service get() = retrofit.create(LoginService::class.java)   // 레트로핏 설정
} // Login object

interface LoginService {
    @POST("/api/user_login/")
    suspend fun userLogin(                      // 로그인을 처리하는 함수
        @Body request: LoginRequest
    ): Response<LoginUser> // 서버로부터 응답을 받고, LoginUser 데이터 클래스에 데이터를 저장.
} // LoginService

data class LoginRequest(
    val userid: String,
    val password: String
)

data class LoginUser(
    @SerializedName("user_id")
    val userid: String,
    @SerializedName("access_token")
    val token: String,
    @SerializedName("user_dong")
    val dong: String,
    @SerializedName("user_ho")
    val ho: String,
) // 로그인 성공 시, 받아오는 User 데이터

// 날짜 변환 함수
val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.ENGLISH)
val monthDayFormat = SimpleDateFormat("M월 d일", Locale.KOREAN)
val monthDayHourMinFormat = SimpleDateFormat("M월 d일 HH시 mm분", Locale.KOREAN)

fun monthDayChange(created_date: String): String {    // 날짜 형식을 변경하는 함수
    val date: Date = originalFormat.parse(created_date)
    return monthDayFormat.format(date)
}

fun monthDayHourMinChange(created_date: String): String {    // 날짜 형식을 변경하는 함수
    val date: Date = originalFormat.parse(created_date)
    return monthDayHourMinFormat.format(date)
}