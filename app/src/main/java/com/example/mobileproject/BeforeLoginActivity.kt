package com.example.mobileproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileproject.databinding.ActivityBeforeLoginBinding
import com.google.firebase.auth.FirebaseAuth

/*class BeforeLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_before_login)
    }
}*/
class BeforeLoginActivity : AppCompatActivity() {

    private lateinit var userIdEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var editTextDate: EditText
    private lateinit var signinButton: Button
    private lateinit var signupButton: Button

    private val firebaseAuth = FirebaseAuth.getInstance()

    private val LOGIN_STATUS_KEY = "login_status" // SharedPreferences 키값


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_before_login)

        // 위젯들을 findViewById로 찾아서 초기화
        userIdEditText = findViewById(R.id.userId)
        passwordEditText = findViewById(R.id.password)
        nameEditText = findViewById(R.id.name)
        editTextDate = findViewById(R.id.editTextDate)
        signinButton = findViewById(R.id.signin)
        signupButton = findViewById(R.id.signup)

        // signin 버튼에 대한 클릭 리스너를 설정합니다.
        signinButton.setOnClickListener {
            // userId와 password에 접근하여 필요한 작업을 수행합니다.
            val userEmail = userIdEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (isInputValid(userEmail, password)) {
                doLogin(userEmail, password)
            } else {
                Toast.makeText(this, "모든 입력값을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        signupButton.setOnClickListener {
            val userEmail = userIdEditText.text.toString()
            val password = passwordEditText.text.toString()

            doSignup(userEmail, password)
        }

        /*// Firebase의 현재 사용자를 가져와 로그인 상태를 확인합니다.
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            // 이미 로그인된 사용자가 있을 경우, MainActivity로 이동
            moveToMainActivity()
        }*/
    }

    // 입력값 유효성 검사 함수
    private fun isInputValid(userEmail: String, password: String): Boolean {
        return userEmail.isNotEmpty() && password.isNotEmpty()
    }

    private fun doLogin(userEmail: String, password: String) {
        if (userEmail.isNotEmpty() && password.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(userEmail, password)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        // 로그인이 성공하면 MainActivity로 이동
                        saveLoginStatus(true)
                        moveToMainActivity()
                    } else {
                        // 로그인 실패 시 처리
                        Log.w("LoginActivity", "signInWithEmail", it.exception)
                        Toast.makeText(this, "로그인 실패: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "이메일과 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()
        }
    }

    private fun doSignup(userEmail: String, password: String) {
        val name = nameEditText.text.toString()
        val birthDate = editTextDate.text.toString()

        if (isSignupInputValid(userEmail, password, name, birthDate)) {
            firebaseAuth.createUserWithEmailAndPassword(userEmail, password)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        // 회원가입 성공 시 MainActivity로 이동
                        saveLoginStatus(true)
                        moveToMainActivity()
                    } else {
                        Toast.makeText(this, "회원가입 실패: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "모든 필드를 올바르게 입력해주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isSignupInputValid(userEmail: String, password: String, name: String, birthDate: String): Boolean {
        return userEmail.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() && birthDate.isNotEmpty()
    }

    private fun saveLoginStatus(status: Boolean) {
        val sharedPreferences = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(LOGIN_STATUS_KEY, status)
        editor.apply()
    }

    private fun moveToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // 현재 액티비티를 종료하여 뒤로 돌아갈 수 없게 합니다.
    }
}