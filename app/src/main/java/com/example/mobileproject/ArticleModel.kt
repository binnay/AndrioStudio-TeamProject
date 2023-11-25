package com.example.mobileproject

data class ArticleModel(
    val userId: String,
    val title: String,
    val price: Int,
    val content: String,
    val isSold: Boolean
) {
    constructor(): this("","", 0, "", false)
}