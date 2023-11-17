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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Firestore {

    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollection = db.collection("items")

    fun getArticles(callback: (List<ArticleModel>) -> Unit) {
        val articleList = mutableListOf<ArticleModel>()
//        val id1 = itemsCollection.document("1@2.3").collection("Item List").document("test 1")
//            .get().addOnSuccessListener {
//                System.out.println("checked 3 : "+ (it["title"])+" "+it["price"])
//            }
        itemsCollection
            .get().addOnSuccessListener {
            for(document in it){
                document.reference.collection("Item List").get().addOnSuccessListener {
                    for(itemDocument in it) {
//                        System.out.println("checked "+itemDocument.get("title"))
                        val article = ArticleModel(
                            itemDocument.get("title").toString(),
                            itemDocument.get("price").toString(),
                            "",
                            false
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

    private lateinit var adapter: ArticleAdapter

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentHomeBinding = FragmentHomeBinding.bind(view)
        binding = fragmentHomeBinding

        auth = FirebaseAuth.getInstance()
        adapter = ArticleAdapter()
        val firestore = Firestore()

        fragmentHomeBinding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        fragmentHomeBinding.articleRecyclerView.adapter = adapter

        firestore.getArticles { articles ->
            // ArticleAdapter에 데이터 추가
            adapter.setItems(articles)
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
//            context?.let {
//                val intent = Intent(it, AddArticleActivity::class.java)
//                startActivity(intent)
//            }
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