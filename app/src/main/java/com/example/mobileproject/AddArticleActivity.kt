package com.example.mobileproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileproject.databinding.ActivityAddArticleBinding

class AddArticleActivity : AppCompatActivity() {
//    private val binding by lazy { ActivityAddArticleBinding.inflate(layoutInflater) }
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_article)
}
}
