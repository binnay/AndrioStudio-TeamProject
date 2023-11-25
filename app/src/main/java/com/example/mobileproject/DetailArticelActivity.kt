package com.example.mobileproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
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


        // Intent에서 정보 추출
        val intent = intent
        val title = intent.getStringExtra("title")
        val seller = intent.getStringExtra("seller")
        val price = intent.getIntExtra("price", 0)

        val itemsCollection= db.collection("items")
        val userDoc = itemsCollection.document(seller.toString())
        val itemList = userDoc.collection("Item List").document(title.toString())

        itemList.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val content = documentSnapshot.getString("content") ?: "" // null 처리 추가
                findViewById<TextView>(R.id.detailDescription).text = content
                val isSold = documentSnapshot.getBoolean("sold") ?: false
                findViewById<TextView>(R.id.detailIsSold).text = if (isSold) "판매완료" else "판매중"
            } else {
                // 문서가 존재하지 않을 때의 처리 (옵션)
                Toast.makeText(this, "내용을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            // 에러 처리 (옵션)
            Toast.makeText(this, "오류가 발생했습니다: ${it.message}", Toast.LENGTH_SHORT).show()
        }


        // 레이아웃의 TextView에 정보 설정
        val titleTextView = findViewById<TextView>(R.id.detailTitle)
        val sellerNameTextView = findViewById<TextView>(R.id.detailSeller)
        val priceTextView = findViewById<TextView>(R.id.detailPrice)

        titleTextView.text = title
        sellerNameTextView.text = seller
        priceTextView.text = price.toString()


        if(seller != null) {
            val sendButton = findViewById<Button>(R.id.msgBtn)

            sendButton.setOnClickListener {
                val intent = Intent(this, ChatRoomActivity::class.java)
                intent.putExtra("sellerEmail", seller)
                intent.putExtra("currentEmail", auth.currentUser?.email.toString())

                val buyerCollection = db.collection("chats").document(auth.currentUser?.email.toString()).collection("buyer")
                val chatData = hashMapOf(
                    "email" to seller
                )
                buyerCollection.document(seller.toString()).set(chatData)
                    .addOnSuccessListener {
                        startActivity(intent)
                    }
            }
        }

    }

}