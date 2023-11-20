package com.example.mobileproject

data class ArticleModel(
    val title: String,
    val price: String,
    val content: String,
    val isSold: Boolean
) {
    constructor(): this("", "", "", false)
}