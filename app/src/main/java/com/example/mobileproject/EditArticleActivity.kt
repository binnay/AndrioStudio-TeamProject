package com.example.mobileproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
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

        val itemsCollection= db.collection("items")
        val userDoc = itemsCollection.document(auth.currentUser?.email.toString())
        val itemList = userDoc.collection("Item List").document(title.toString())
        System.out.println("email"+auth.currentUser?.email)
        itemList.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val price = documentSnapshot.getLong("price").toString()
                findViewById<EditText>(R.id.priceEditText).setText(price)
                System.out.println("checked : price")
                val content = documentSnapshot.getString("content") ?: "" // null 처리 추가
                findViewById<EditText>(R.id.contentEditText).setText(content)
                val isSold = documentSnapshot.getBoolean("sold")
                if (isSold != null) {
                    if (isSold) {
                        findViewById<RadioButton>(R.id.soldButton).isChecked = true
                    } else {
                        findViewById<RadioButton>(R.id.sellButton).isChecked = true
                    }
                }
            } else {
                // 문서가 존재하지 않을 때의 처리 (옵션)
                Toast.makeText(this, "내용을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            // 에러 처리 (옵션)
            Toast.makeText(this, "오류가 발생했습니다: ${it.message}", Toast.LENGTH_SHORT).show()
        }

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
        val radioGroup = findViewById<RadioGroup>(R.id.sellGroup)
        val checkedRadioButtonId = radioGroup.checkedRadioButtonId
        return checkedRadioButtonId!=-1
    }

    private fun areAllFieldsFilled(): Boolean {
        val price = findViewById<EditText>(R.id.priceEditText).text.toString()
        val content = findViewById<EditText>(R.id.contentEditText).text.toString()

        val radioGroup = findViewById<RadioGroup>(R.id.sellGroup)
        val checkedRadioButtonId = radioGroup.checkedRadioButtonId

        return price.isNotEmpty() && content.isNotEmpty() && checkedRadioButtonId!=-1
    }
    private fun editArticleToFirestore() {
        val userId = auth.currentUser?.email
        val itemsCollection= db.collection("items")
        val userDoc = itemsCollection.document(userId.toString())

        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val priceEditText = findViewById<EditText>(R.id.priceEditText)
        val contentEditText = findViewById<EditText>(R.id.contentEditText)

        val radioGroup = findViewById<RadioGroup>(R.id.sellGroup)
        val checkedRadioButtonId = radioGroup.checkedRadioButtonId
        val isSold : Boolean = (checkedRadioButtonId==R.id.soldButton)

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