package com.example.mobileproject

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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
        val contentEditText = findViewById<EditText>(R.id.contentEditText)

        val title = titleEditText.text.toString()
        val price = priceEditText.text.toString()
        val content = contentEditText.text.toString()
        val newPostData = ArticleModel(title, price, content, false)
        val userId = auth.currentUser?.email
        val itemsCollection= db.collection("items")
        if(userId!=null) {
            val userDoc = itemsCollection.document(userId.toString())
            userDoc.get().addOnSuccessListener {
                if(!it.exists()) {
                    val dataToAdd = hashMapOf(
                        "email" to userId.toString()
                    )
                    userDoc.set(dataToAdd)
                }
            }
            val ItemList = userDoc.collection("Item List").document(newPostData.title)
            ItemList.set(newPostData)
        }
    }
}
