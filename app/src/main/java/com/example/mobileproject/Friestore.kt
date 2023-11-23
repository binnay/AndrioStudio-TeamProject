// 파일명: Firestore.kt
package com.example.mobileproject

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Firestore {

    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollection = db.collection("items")

    fun getArticles(isFiltering: Boolean, callback: (List<ArticleModel>) -> Unit) {
        val articleList = mutableListOf<ArticleModel>()

        itemsCollection.get().addOnSuccessListener { documents ->
            var fetchCount = documents.size() // documents의 크기

            for (document in documents) {
                document.reference.collection("Item List").get()
                    .addOnSuccessListener { itemDocuments ->
                        for (itemDocument in itemDocuments) {
                            val title = itemDocument.getString("title") ?: ""
                            val price = itemDocument.getDouble("price") ?: 0.0
                            val isSold = itemDocument.getBoolean("sold") ?: false

                            if (!isFiltering || (isFiltering && !isSold)) {
                                val article = ArticleModel(title, price, isSold)
                                articleList.add(article)
                            }
                        }

                        synchronized(this) {
                            if (--fetchCount == 0) {
                                // fetchCount가 0이 되면 모든 데이터를 가져왔다고 판단
                                callback(articleList)
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("Firestore", "Failed to fetch item list: $exception")
                    }
            }
        }.addOnFailureListener { exception ->
            Log.e("Firestore", "Failed to fetch items: $exception")
        }
    }
}
