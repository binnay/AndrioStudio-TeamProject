// ArticleAdapter.kt

package com.example.mobileproject

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobileproject.databinding.ItemArticleBinding

class ArticleAdapter : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    private var itemList: List<ArticleModel> = emptyList()

    fun setItems(items: List<ArticleModel>) {
        itemList = items
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SimpleDateFormat")
        fun bind(articleModel: ArticleModel) {
            binding.titleTextView.text = articleModel.title
            binding.priceTextView.text = articleModel.price.toString()

            // 이미지가 있는 경우 Glide를 사용하여 이미지를 표시
            // if (articleModel.imageUrl.isNotEmpty()) {
            //     Glide.with(binding.thumbnailImageView.context)
            //         .load(articleModel.imageUrl)
            //         .into(binding.thumbnailImageView)
            // }

            // isSold 값에 따라 soldTextView 텍스트 설정
            val soldText = if (articleModel.isSold) "판매완료" else "판매 중"
            binding.soldTextView.text = soldText
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // LayoutInflater.from(parent.context) 대신에 ViewBinding을 사용하여 인플레이트
        return ViewHolder(ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }
}
