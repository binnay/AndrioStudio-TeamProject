package com.example.mobileproject

import android.content.ClipData.Item
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileproject.databinding.ActivityAddArticleBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddArticleActivity : AppCompatActivity() {
//    private val binding by lazy { ActivityAddArticleBinding.inflate(layoutInflater) }

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_article)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        findViewById<Button>(R.id.submitButton).setOnClickListener {
            addArticleToFirestore()
        }
    }

    private fun addArticleToFirestore() {
        val titleEditText = findViewById<EditText>(R.id.titleEditText)
        val priceEditText = findViewById<EditText>(R.id.priceEditText)

        val title = titleEditText.text.toString()
        val price = priceEditText.text.toString()
        val newPostData = ArticleModel(title, price,"")
        val userId = auth.currentUser?.email
        val itemsCollection= db.collection("items")
        if(userId!=null) {
            val userDoc = itemsCollection.document(userId.toString())
            val ItemList = userDoc.collection("Item List").document(newPostData.title)
            ItemList.set(newPostData)
        }
    }
}
