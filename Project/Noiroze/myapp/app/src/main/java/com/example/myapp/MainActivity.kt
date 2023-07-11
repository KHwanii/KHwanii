package com.example.myapp


import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

import com.example.myapp.databinding.ActivityMainBinding
import com.example.myapp.login.Login.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    companion object ToastManager {
        private var currentToast: Toast? = null

        fun showToast(context: Context?, message: String) {
            currentToast?.cancel()
            currentToast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            currentToast?.show()
        }
    }             // 토스트 메세지 관리하는 오브젝트

    private lateinit var mbinding: ActivityMainBinding

    private lateinit var title : ImageView
    private lateinit var userIdInput: EditText
    private lateinit var pwdInput: EditText

    private var backPressedTime: Long = 0            // 뒤로가기 버튼 변수 초기화

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mbinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mbinding.root)

        title = mbinding.mainLogo
        userIdInput = mbinding.IDinput
        pwdInput = mbinding.PWDinput
        val loginBtn = mbinding.loginBtn
        val transitionDrawable = ResourcesCompat.getDrawable(resources, R.drawable.button_transition,null) as TransitionDrawable
        loginBtn.background = transitionDrawable

        loginBtn.setOnClickListener {
            transitionDrawable.startTransition(100) // 0.2초 동안 색상 변경

            loginBtn.postDelayed({
                transitionDrawable.reverseTransition(100) // 0.2초 동안 색상 복원
            }, 100L)

            val userid = userIdInput.text.toString()
            val password = pwdInput.text.toString()

            if (userid.isBlank() || password.isBlank()) {
                showToast(this, "아이디와 비밀번호를 입력해주세요.")
            }
            else {
                loginUser(userid, password)
            }
        }
    }

    private fun loginUser(userid: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {          // 코루틴에서 I/O 에 최적화된 쓰레드를 불러와서 작업함
            val loginResponse = service.userLogin(userid, password)         // service의 로그인함수를 불러오는 val 생성.
            withContext(Dispatchers.Main) {             // UI 작업 등은 메인 스레드에서 실행되어야 하므로, Dispatchers.Main 을 통해, 메인 스레드를 다시 불러옴
                if (loginResponse.isSuccessful && loginResponse.body() != null) {            // 로그인 함수처리 성공 && 로그인 데이터가 null이 아닌 경우
                    val sharedPref = getSharedPreferences("LoginData", Context.MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.putString("user_id", userid)
                    editor.putString("token", loginResponse.body()?.token)
                    editor.putString("user_dong", loginResponse.body()?.dong)
                    editor.putString("user_ho", loginResponse.body()?.ho)
                    editor.apply()
                    // val dong = loginResponse.body()?.dong
                    // val ho = loginResponse.body()?.ho
                    // Log.d("로그인 성공 정보", "User dong: ${dong}, User ho: ${ho}")  // 로그 추가
                    Toast.makeText(this@MainActivity, "로그인 성공!", Toast.LENGTH_SHORT).show()      // 여기에서 response.body()?.token을 사용하여 다른 요청에 사용가능.
                    val intent = Intent(this@MainActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else {                                                                    // 로그인 함수처리 실패 || 로그인 데이터가 null인 경우
                    AlertDialog.Builder(this@MainActivity)      // 로그인 실패 시 대화 상자를 표시
                        .setTitle("로그인 실패")
                        .setMessage("아이디 또는 비밀번호를 확인해주세요.")
                        .setPositiveButton("확인", null)
                        .show()
                }
            }
        }
    }  // loginUser

    override fun onBackPressed() {
            if (backPressedTime + 1500 > System.currentTimeMillis()) {   // 1.5초 이내 '두 번 눌러 종료' 기능 수행
                super.onBackPressed()
                return
            }
            else {
                showToast(this, "한번 더 누르면 종료합니다.")
            }
            backPressedTime = System.currentTimeMillis()
    }     // 뒤로가기 버튼 함수
}  // onCreate
