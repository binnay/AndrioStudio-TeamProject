package com.example.mobileproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    val fragmentManager: FragmentManager = supportFragmentManager
    val fragmentBLogin: FragmentBLogin = FragmentBLogin()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragmentBLogin).commit()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        // setOnNavigationItemSelectedListener 대신 setOnItemSelectedListener 사용
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()

            when (menuItem.itemId) {
                R.id.homeItem -> {
                    // 홈 아이템 클릭 시 다른 Fragment로 교체하도록 처리
                    // transaction.replace(R.id.frameLayout, 다른 Fragment).commitAllowingStateLoss()
                }
                R.id.chatItem -> {
                    // 대화하기 아이템 클릭 시 다른 Fragment로 교체하도록 처리
                    // transaction.replace(R.id.frameLayout, 다른 Fragment).commitAllowingStateLoss()
                }
                R.id.mypageItem -> {
                    // '나의정보' 아이템 클릭 시 FragmentBLogin을 표시
                    transaction.replace(R.id.frameLayout, fragmentBLogin).commitAllowingStateLoss()
                }
            }

            true
        }
    }

}