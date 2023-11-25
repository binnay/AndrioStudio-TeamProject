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
    val fragmentALogin: FragmentALogin = FragmentALogin()
    val fragmentHome: FragmentHome = FragmentHome()
    val fragementChatList : FragmentChatList = FragmentChatList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setLoggedInStatus(true)

        val transaction: FragmentTransaction = fragmentManager.beginTransaction()

        val fragmentToLoad = intent.getStringExtra("fragmentToLoad")
        /*if (fragmentToLoad == "homeFragment") {
            setLoggedInStatus(true) // 글수정/삭제 후 로그인 안정성 유지 위해 추가
            transaction.replace(R.id.frameLayout, fragmentHome).commit()
        }*/
        transaction.replace(R.id.frameLayout, fragmentHome).commit()

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
                    }
                }
                R.id.chatItem -> {
                    if(isLoggedIn) {
                        // 대화하기 아이템 클릭 시 다른 Fragment로 교체하도록 처리
                        // transaction.replace(R.id.frameLayout, 다른 Fragment).commitAllowingStateLoss()
                        transaction.replace(R.id.frameLayout, fragementChatList).commitAllowingStateLoss()
                    }
                }
                R.id.mypageItem -> {

                    transaction.replace(R.id.frameLayout, fragmentALogin)
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