package com.example.mobileproject

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DetailArticleActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_article)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val itemsCollection= db.collection("items")

        // Intent에서 정보 추출
        val intent = intent
        val title = intent.getStringExtra("title")
        val sellerName = intent.getStringExtra("userId")
        val isSold = intent.getBooleanExtra("isSold", false) // 기본값으로 false 설정
        val price = intent.getStringExtra("price")
        val description = intent.getStringExtra("content")

        // 레이아웃의 TextView에 정보 설정
        val titleTextView = findViewById<TextView>(R.id.detailTitle)
        val sellerNameTextView = findViewById<TextView>(R.id.detailSeller)
        val isSoldTextView = findViewById<TextView>(R.id.detailIsSold)
        val priceTextView = findViewById<TextView>(R.id.detailPrice)
        val descriptionTextView = findViewById<TextView>(R.id.detailDescription)

        titleTextView.text = title
        sellerNameTextView.text = sellerName
        isSoldTextView.text = if (isSold) "판매완료" else "판매중" // isSold 값에 따라 텍스트 설정
        priceTextView.text = price
        descriptionTextView.text = description
    }
}