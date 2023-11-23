//파일명: ArticleModel.kt
package com.example.mobileproject

data class ArticleModel(
    val title: String = "",
    val price: Double = 0.0,
    val isSold: Boolean = false,
    //val content: String = "",
    //val imageUrl: String = ""
){
    constructor(): this("", 0.0, false)
}
