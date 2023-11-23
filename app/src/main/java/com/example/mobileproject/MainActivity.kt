package com.example.mobileproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    val fragmentManager: FragmentManager = supportFragmentManager
    private var isLoggedIn: Boolean = false
    val fragmentBLogin: FragmentBLogin = FragmentBLogin()
    val fragmentALogin: FragmentALogin = FragmentALogin()
    val fragmentHome: FragmentHome = FragmentHome()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        //transaction.replace(R.id.frameLayout, fragmentBLogin).commit()

        val fragmentToLoad = intent.getStringExtra("fragmentToLoad")
        if (fragmentToLoad == "homeFragment") {
            transaction.replace(R.id.frameLayout, fragmentHome).commit()
        } else {
            transaction.replace(R.id.frameLayout, fragmentBLogin).commit()
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        // setOnNavigationItemSelectedListener 대신 setOnItemSelectedListener 사용
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()

            when (menuItem.itemId) {
                R.id.homeItem -> {
                    if(isLoggedIn) { // 로그인후에만 볼 수 있도록
                        // 홈 아이템 클릭 시 다른 Fragment로 교체하도록 처리
                        transaction.replace(R.id.frameLayout, fragmentHome)
                            .commitAllowingStateLoss()
                    } else {
                        transaction.replace(R.id.frameLayout, fragmentBLogin)
                        Toast.makeText(this, "로그인 후 사용하실 수 있습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                R.id.chatItem -> {
                    if(isLoggedIn) {
                        // 대화하기 아이템 클릭 시 다른 Fragment로 교체하도록 처리
                        // transaction.replace(R.id.frameLayout, 다른 Fragment).commitAllowingStateLoss()
                    } else {
                        transaction.replace(R.id.frameLayout, fragmentBLogin)
                        Toast.makeText(this, "로그인 후 사용하실 수 있습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                R.id.mypageItem -> {
                    // '나의정보' 아이템 클릭 시 FragmentBLogin 또는 FragmentALogin을 표시
                    if (isLoggedIn) {
                        transaction.replace(R.id.frameLayout, fragmentALogin)
                    } else {
                        transaction.replace(R.id.frameLayout, fragmentBLogin)
                    }
                    transaction.commitAllowingStateLoss()
                }
            }

            true
        }
    }

    fun setLoggedInStatus(isLoggedIn: Boolean) {
        this.isLoggedIn = isLoggedIn
    }
}