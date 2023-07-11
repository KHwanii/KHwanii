package com.example.smarthome

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.smarthome.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeActivity : AppCompatActivity() {

    companion object ToastManager {

        private var currentToast: Toast? = null

        fun showToast(context: Context?, message: String) {
            currentToast?.cancel()
            currentToast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            currentToast?.show()
        }
    }             // 토스트 메세지 관리하는 오브젝트.

    private lateinit var mbinding: ActivityHomeBinding

    private var backPressedTime: Long = 0            // 뒤로가기 버튼 변수 초기화


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mbinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(mbinding.root)


        // 바텀 네비게이션 메뉴 포함
        val navView: BottomNavigationView = mbinding.myBottomNav
        // 네비게이션들을 담는 호스트 -> 프래그먼트 전환시 나타날 화면을 담는 공간
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host) as NavHostFragment
        // 네비게이션 컨트롤러 -> 실제 내비게이션 메뉴를 연결
        val navController = navHostFragment.navController

        navView.setupWithNavController(navController)   // 네비게이션 뷰와 컨트롤러와 연결
        // BottomNavigationView와 NavController를 연결 / BottomNavigationView의 네비게이션 기능이 설정
    }

    // 메뉴를 생성하고 메뉴 아이템의 클릭 이벤트 처리
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)  // top_app_bar.xml을 menu 객체로 인플레이트
        // xml에 정의된 아이템들이 menu 객체에 추가돼

        return super.onCreateOptionsMenu(menu)    // 나머지 기본 동작 처리
    }

    override fun onBackPressed() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host) as NavHostFragment
        val navController = navHostFragment.navController

        if (navController.currentDestination?.id == navController.graph.startDestinationId) {
            // 현재 프래그먼트가 시작 프래그먼트(즉, 이전 프래그먼트가 없는 경우)이면 1.5초 이내 '두 번 눌러 종료' 기능 수행
            if (backPressedTime + 1500 > System.currentTimeMillis()) {
                super.onBackPressed()
                return
            } else {
                showToast(baseContext, "한번 더 누르면 종료합니다.")
            }
            backPressedTime = System.currentTimeMillis()
        } else {
            // 현재 프래그먼트가 시작 프래그먼트가 아니면(즉, 이전 프래그먼트가 있는 경우)이면 이전 프래그먼트로 이동
            navController.navigateUp()
        }
    }     // 뒤로가기 버튼 함수

}