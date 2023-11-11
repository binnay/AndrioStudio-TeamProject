package com.example.mobileproject

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mobileproject.databinding.FragmentBeforeLoginBinding
import com.google.firebase.auth.FirebaseAuth

class FragmentBLogin : Fragment() {

    private lateinit var binding: FragmentBeforeLoginBinding
    private val firebaseAuth = FirebaseAuth.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBeforeLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // signin 버튼에 대한 클릭 리스너를 설정합니다.
        binding.signin.setOnClickListener {
            // userId와 password에 접근하여 필요한 작업을 수행합니다.
            val userEmail = binding.userId.text.toString()
            val password = binding.password.text.toString()

            // doLogin 함수를 호출하여 로그인을 시도합니다.
            doLogin(userEmail, password)
        }

        binding.signup.setOnClickListener {
            val userEmail = binding.userId.text.toString()
            val password = binding.password.text.toString()

            doSignup(userEmail, password)
        }
    }

    override fun onStart() {
        super.onStart()

        // Firebase의 현재 사용자를 가져와 로그인 상태를 확인합니다.
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            // 이미 로그인된 사용자가 있을 경우, MainActivity에 로그인 상태를 알립니다.
            (activity as? MainActivity)?.setLoggedInStatus(true)
        } else {
            // 로그아웃 시 MainActivity에 로그인 상태를 알립니다. (isLoggedIn을 false로 설정)
            (activity as? MainActivity)?.setLoggedInStatus(false)
        }
    }

    private fun doLogin(userEmail: String, password: String) {
        if (userEmail.isNotEmpty() && password.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(userEmail, password)
                .addOnCompleteListener(requireActivity()) {
                    if (it.isSuccessful) {
                        // 로그인이 성공하면 원하는 Fragment로 전환
                        val fragmentALogin = FragmentALogin()
                        val transaction = requireActivity().supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.frameLayout, fragmentALogin)
                        transaction.addToBackStack(null)
                        transaction.commitAllowingStateLoss()

                        binding.userId.text = null
                        binding.password.text = null
                    } else {
                        // 로그인 실패 시 처리
                        Log.w("LoginActivity", "signInWithEmail", it.exception)
                        val activity = activity
                        if (activity != null) {
                            Toast.makeText(requireContext(), "Authentication failed.", Toast.LENGTH_SHORT).show()
                        } else {
                            Log.e("LoginActivity", "Activity is null. Unable to show Toast.")
                        }
                    }
                }
        } else {
            Toast.makeText(requireContext(), "Email or password is empty.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun doSignup(userEmail: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(userEmail, password)
            .addOnCompleteListener(requireActivity()) {
                if(it.isSuccessful) {
                    val fragmentALogin = FragmentALogin()
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.frameLayout, fragmentALogin)
                    transaction.commitAllowingStateLoss()
                } else {
                    Log.d("SignUp", it.exception.toString())
                    Toast.makeText(requireContext(), "SignUp failed: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}