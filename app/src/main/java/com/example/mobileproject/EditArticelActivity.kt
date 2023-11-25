package com.example.mobileproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditArticleActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_article)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val intent = intent
        val title = intent.getStringExtra("title")
        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        titleTextView.text = title

        findViewById<Button>(R.id.submitButton).setOnClickListener {
            if (areAllFieldsFilled() && isValidIsSoldInput()) {
                editArticleToFirestore()
            } else {
                if (!areAllFieldsFilled()) {
                    Toast.makeText(this, "모든 값을 입력해야 합니다.", Toast.LENGTH_SHORT).show()
                }
                if (!isValidIsSoldInput()) {
                    Toast.makeText(this, "올바른 판매여부를 입력하세요 (판매중 또는 판매완료).", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun isValidIsSoldInput(): Boolean {
        val isSold = findViewById<EditText>(R.id.isSoldEditText).text.toString()
        return isSold == "판매중" || isSold == "판매완료"
    }

    private fun areAllFieldsFilled(): Boolean {
        val price = findViewById<EditText>(R.id.priceEditText).text.toString()
        val content = findViewById<EditText>(R.id.contentEditText).text.toString()
        val isSold = findViewById<EditText>(R.id.isSoldEditText).text.toString()

        return price.isNotEmpty() && content.isNotEmpty() && isSold.isNotEmpty()
    }
    private fun editArticleToFirestore() {
        val userId = auth.currentUser?.email
        val itemsCollection= db.collection("items")
        val userDoc = itemsCollection.document(userId.toString())

        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val priceEditText = findViewById<EditText>(R.id.priceEditText)
        val contentEditText = findViewById<EditText>(R.id.contentEditText)
        val isSoldEditText = findViewById<EditText>(R.id.isSoldEditText)
        val isSold = isSoldEditText.text.toString() != "판매중"

        val title = titleTextView.text.toString()
        val price = priceEditText.text.toString().toInt()
        val content = contentEditText.text.toString()

        val editPostData = ArticleModel(userId.toString(), title, price, content, isSold)

        val ItemList = userDoc.collection("Item List").document(editPostData.title)
        ItemList.set(editPostData)

        val mainActivityIntent = Intent(this, MainActivity::class.java)
        mainActivityIntent.putExtra("fragmentToLoad", "homeFragment") // MainActivity에서 어떤 Fragment를 표시할지를 지정
        startActivity(mainActivityIntent)
    }
}