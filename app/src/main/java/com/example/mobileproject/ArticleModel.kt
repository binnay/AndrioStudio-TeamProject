package com.example.mobileproject

data class ArticleModel(
    val title: String,
    val price: String,
    val imageUrl: String
) {
    constructor(): this("", "", "")
}