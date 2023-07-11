package com.example.myapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.myapp.databinding.ActivityHomeBinding
import com.example.myapp.login.Login.service
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity : AppCompatActivity() {

    companion object ToastManager {
        private var currentToast: Toast? = null

        fun showToast(context: Context?, message: String) {
            currentToast?.cancel()
            currentToast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            currentToast?.show()
        }
    }             // 토스트 메세지 관리하는 오브젝트

    private lateinit var mbinding: ActivityHomeBinding
    private var backPressedTime: Long = 0   // 뒤로가기 버튼을 누른 시간을 저장할 변수 추가

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mbinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(mbinding.root)

        val navView: BottomNavigationView = mbinding.myBottomNav  // 바텀 네비게이션 메뉴 포함
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host) as NavHostFragment   // 네비게이션들을 담는 호스트 -> 프래그먼트 전환시 나타날 화면을 담는 공간
        val navController = navHostFragment.navController    // 네비게이션 컨트롤러 -> 실제 내비게이션 메뉴를 연결

        navView.setupWithNavController(navController)   // 네비게이션 뷰와 컨트롤러와 연결
        // BottomNavigationView와 NavController를 연결 / BottomNavigationView의 네비게이션 기능이 설정
    } // onCreate

    override fun onCreateOptionsMenu(menu: Menu): Boolean {              // 메뉴를 생성하고 메뉴 아이템의 클릭 이벤트 처리
        menuInflater.inflate(R.menu.top_app_bar, menu)  // top_app_bar.xml을 menu 객체로 인플레이트.  xml에 정의된 아이템들이 menu 객체에 추가돼
        return super.onCreateOptionsMenu(menu)    // 나머지 기본 동작 처리
    }
}