package com.example.mobileproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mobileproject.databinding.FragmentAfterLoginBinding
import com.google.firebase.auth.FirebaseAuth

class FragmentALogin : Fragment() {

    private lateinit var binding: FragmentAfterLoginBinding
    private val firebaseAuth = FirebaseAuth.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*binding = FragmentAfterLoginBinding.inflate(inflater, container, false)
        return binding.root*/
        return inflater.inflate(R.layout.fragment_after_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAfterLoginBinding.bind(view)

        binding.userName.text = firebaseAuth.currentUser?.email
        // signout 버튼에 대한 클릭 리스너를 설정합니다.
        binding.signout.setOnClickListener {
            firebaseAuth.signOut()

            // MainActivity에 로그인 상태를 알립니다. (isLoggedIn을 false로 설정)
            (activity as? MainActivity)?.setLoggedInStatus(false)

            val fragmentBLogin = FragmentBLogin()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, fragmentBLogin)
            transaction.commitAllowingStateLoss()
        }

    }
}