package com.example.mobileproject

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobileproject.databinding.FragmentHomeBinding

class FragmentHome : Fragment(R.layout.fragment_home) {

    companion object {
        fun newInstance() = FragmentHome()
        const val TAG = "HomeFragment"
    }

    // 전역변수는 nullable 하기 때문에 onViewCreate() 안에서는 절대적으로 null이 들어오지
    // 못하게 하기 위해 임시로 지역변수로 설정
    private var binding: FragmentHomeBinding? = null

    private lateinit var adapter: ArticleAdapter

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentHomeBinding = FragmentHomeBinding.bind(view)
        binding = fragmentHomeBinding


        adapter = ArticleAdapter()

        fragmentHomeBinding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        fragmentHomeBinding.articleRecyclerView.adapter = adapter

        fragmentHomeBinding.addFloatingButton.setOnClickListener {
//            context?.let {
//                if (auth.currentUser != null) {
//                    val intent = Intent(it, AddArticleActivity::class.java)
//                    startActivity(intent)
//                } else {
//                    Snackbar.make(view, "로그인 후 사용하실 수 있습니다.", Snackbar.LENGTH_SHORT).show()
//                }
//            }
            context?.let {
                val intent = Intent(it, AddArticleActivity::class.java)
                startActivity(intent)
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}