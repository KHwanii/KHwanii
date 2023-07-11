package com.example.smarthome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import com.example.smarthome.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mbinding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mbinding.root)


        val activatebtn = mbinding.imgPowerButton

        activatebtn.setOnClickListener {
            // 로테이션 애니메이션
            val rotation = AnimationUtils.loadAnimation(this, R.anim.rotate)
            activatebtn.startAnimation(rotation)

            // 타이머 설정
            Handler(Looper.getMainLooper()).postDelayed({
                // 애니메이션 끝
                activatebtn.clearAnimation()
                activatebtn.setImageResource(R.drawable.verified)   // 애니메이션 실행 후 이미지 변경

                // 홈액티비티 넘어가기 전 설정
                Handler(Looper.getMainLooper()).postDelayed({
                    // Change screen
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 500) // 0.5 second

            }, 1000)  // 1 second
        }
    }
}