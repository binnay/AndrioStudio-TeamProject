package com.example.mobileproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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
            if(areAllFieldsFilled()) {
                addArticleToFirestore()
            } else {
                Toast.makeText(this, "모든 값을 입력해야 합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun areAllFieldsFilled(): Boolean {
        val title = findViewById<EditText>(R.id.titleEditText).text.toString()
        val price = findViewById<EditText>(R.id.priceEditText).text.toString()
        val content = findViewById<EditText>(R.id.contentEditText).text.toString()

        return title.isNotEmpty() && price.isNotEmpty() && content.isNotEmpty()
    }

    private fun addArticleToFirestore() {
        val titleEditText = findViewById<EditText>(R.id.titleEditText)
        val priceEditText = findViewById<EditText>(R.id.priceEditText)
        val contentEditText = findViewById<EditText>(R.id.contentEditText)

        val userId = auth.currentUser?.email
        val title = titleEditText.text.toString()
        val price = priceEditText.text.toString().toInt()
        val content = contentEditText.text.toString()
        val newPostData = ArticleModel(userId.toString(), title, price, content, false)

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
            //필드이름은 자동으로 생성됨. ArticleModel에서 설정한 프로퍼티이름 필드이름은 관련이 없음
            //firestore에서 지정한 필드이름이 우연히 모델클래스 프로퍼티이름과 일치했을 뿐(isSold만 sold로 지정)
            //필드이름을 직접 주고 싶다면
            /*
            val dataToAdd = hashMapOf(
                "userId" to userId.toString(),
                "title" to title,
                "price" to price,
                "content" to content,
                "isSold" to true // 필드 이름을 직접 지정하여 "isSold"로 저장
            )
            ItemList.set(dataToAdd)
             */
            // 글쓰기가 완료되면 FragmentHome을 표시하도록 설정
            val mainActivityIntent = Intent(this, MainActivity::class.java)
            mainActivityIntent.putExtra("fragmentToLoad", "homeFragment") // MainActivity에서 어떤 Fragment를 표시할지를 지정
            startActivity(mainActivityIntent)
        }
    }
}
