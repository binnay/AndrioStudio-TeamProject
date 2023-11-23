//파일명: FragmentHome.kt
package com.example.mobileproject

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobileproject.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


class FragmentHome : Fragment(R.layout.fragment_home) {
    private lateinit var auth: FirebaseAuth
    // 전역변수는 nullable 하기 때문에 onViewCreate() 안에서는 절대적으로 null이 들어오지
    // 못하게 하기 위해 임시로 지역변수로 설정
    private var binding: FragmentHomeBinding? = null
    private lateinit var adapter: ArticleAdapter
    //'판매중'(filterbutton) 상태 변수
    private var isFiltering: Boolean = false


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentHomeBinding = FragmentHomeBinding.bind(view)
        auth = FirebaseAuth.getInstance()
        adapter = ArticleAdapter()
        val firestore = Firestore()

        with(fragmentHomeBinding.articleRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@FragmentHome.adapter
        }

        // 최초 화면 로딩 시 모든 아이템 표시//필터링 상태 전달
        firestore.getArticles(isFiltering) { articles ->
            adapter.setItems(articles)
        }

        //filterButton(판매중 버튼) 클릭 리스너
        // filterButton 클릭 시 필터링 적용
        fragmentHomeBinding.filterButton.setOnClickListener {
            isFiltering = !isFiltering
            applyFilter(isFiltering, fragmentHomeBinding)
        }


        fragmentHomeBinding.addFloatingButton.setOnClickListener {
            requireContext().let {
                if (auth.currentUser != null) {
                    // 필터링 상태에 따라 아이템 로딩
                    firestore.getArticles(isFiltering) { articles ->
                        adapter.setItems(articles)
                    }
                    // filterButton 텍스트 업데이트
                    updateFilterButtonText(fragmentHomeBinding, isFiltering)
                } else {
                    Snackbar.make(view, "로그인 후 사용하실 수 있습니다.", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun applyFilter(isFiltering: Boolean, fragmentHomeBinding: FragmentHomeBinding) {
        // filterButton 클릭 시 필터링 상태에 따라 아이템 로딩
        val firestore = Firestore()
        firestore.getArticles(isFiltering) { articles ->
            adapter.setItems(articles)
        }
        // filterButton 텍스트 업데이트
        updateFilterButtonText(fragmentHomeBinding, isFiltering)
    }

    private fun updateFilterButtonText(fragmentHomeBinding: FragmentHomeBinding, isFiltering: Boolean) {
        // 필터링 상태에 따라 filterButton 텍스트 업데이트
        val buttonText = if (isFiltering) "판매중" else "전체"
        fragmentHomeBinding.filterButton.text = buttonText
    }


}