package com.example.mobileproject

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobileproject.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.recyclerview.widget.RecyclerView

class Firestore {

    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollection = db.collection("items")

    fun getArticles(callback: (List<ArticleModel>) -> Unit) {
        val articleList = mutableListOf<ArticleModel>()

        itemsCollection
            .get().addOnSuccessListener {
            for(document in it){
                document.reference.collection("Item List").get().addOnSuccessListener {
                    for(itemDocument in it) {
//                        System.out.println("checked "+itemDocument.get("title"))
                        val article = ArticleModel(
                            document.get("email").toString(),
                            itemDocument.get("title").toString(),
                            itemDocument.get("price").toString().toInt(),
                            "",
                            itemDocument.get("sold").toString().toBoolean()
                        )
                        articleList.add(article)
                        System.out.println("checked article title : "+article.title + ", price : "+article.price)
                        System.out.println("checked size : "+articleList.size)
                    }
                    callback(articleList)
                }
            }
//                System.out.println("checked size : "+articleList.size)
        }
    }
}
class FragmentHome : Fragment(R.layout.fragment_home) {
    private lateinit var auth: FirebaseAuth
    // 전역변수는 nullable 하기 때문에 onViewCreate() 안에서는 절대적으로 null이 들어오지
    // 못하게 하기 위해 임시로 지역변수로 설정
    private var binding: FragmentHomeBinding? = null
    private lateinit var firestore: Firestore
    private lateinit var adapter: ArticleAdapter
    private var filterType: FilterType = FilterType.ALL

    enum class FilterType {
        ALL, SELLING, SOLD
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentHomeBinding = FragmentHomeBinding.bind(view)
        binding = fragmentHomeBinding

        auth = FirebaseAuth.getInstance()
        adapter = ArticleAdapter()
        firestore = Firestore()

        fragmentHomeBinding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        fragmentHomeBinding.articleRecyclerView.adapter = adapter

        // 필터 버튼 초기화
        val filterButton = view.findViewById<Button>(R.id.filterButton)

        filterArticles(filterType) //첫화면에서 전체 보이도록

        // 필터 버튼 클릭 이벤트 처리
        filterButton.setOnClickListener {
            when (filterType) {
                FilterType.ALL -> {
                    filterType = FilterType.SELLING
                    filterButton.text = "판매중"
                }
                FilterType.SELLING -> {
                    filterType = FilterType.SOLD
                    filterButton.text = "판매완료"
                }
                FilterType.SOLD -> {
                    filterType = FilterType.ALL
                    filterButton.text = "전체"
                }
            }
            // 글 목록 필터링
            filterArticles(filterType)
        }

        fragmentHomeBinding.addFloatingButton.setOnClickListener {
            context?.let {
                if (auth.currentUser != null) {
                    val intent = Intent(it, AddArticleActivity::class.java)
                    startActivity(intent)
                } else {
                    Snackbar.make(view, "로그인 후 사용하실 수 있습니다.", Snackbar.LENGTH_SHORT).show()
                }
            }

        }

        // RecyclerView의 아이템 클릭 리스너 설정
        adapter.setOnItemClickListener(object : ArticleAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, item: ArticleModel) {
                // 클릭한 아이템에서 필요한 정보 추출
                val userId = item.userId
                val title = item.title
                val price = item.price
                val content = item.content

                if(auth.currentUser?.email==userId) {
                    val intent = Intent(requireContext(), EditArticleActivity::class.java)
                    intent.putExtra("title", title)

                    // 수정하기 페이지로 이동
                    startActivity(intent)
                } else {
                    val intent = Intent(requireContext(), DetailArticleActivity::class.java)
                    intent.putExtra("seller", userId)
                    intent.putExtra("title", title)
                    intent.putExtra("price", price)
                    intent.putExtra("content", content)

                    // 상세 보기 페이지로 이동
                    startActivity(intent)
                }

            }
        })
    }

    // 글 목록 필터링 함수
    private fun filterArticles(filterType: FilterType) {
        // Firestore에서 글 목록을 가져와서 필터링하여 표시
        firestore.getArticles { articles ->
            val filteredArticles = when (filterType) {
                FilterType.ALL -> articles // 모든 글 표시
                FilterType.SELLING -> articles.filter { !it.isSold } // 판매중인 글만 표시
                FilterType.SOLD -> articles.filter { it.isSold } // 판매완료된 글만 표시
            }
            // ArticleAdapter에 데이터 추가
            adapter.setItems(filteredArticles)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}