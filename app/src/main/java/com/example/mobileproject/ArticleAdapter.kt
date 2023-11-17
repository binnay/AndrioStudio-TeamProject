package com.example.mobileproject

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileproject.databinding.ItemArticleBinding

class ArticleAdapter : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    private var itemList: List<ArticleModel> = emptyList()
    fun setItems(items: List<ArticleModel>) {
        itemList = items
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemArticleBinding): RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SimpleDateFormat")
        fun bind(articleModel: ArticleModel) {

            binding.titleTextView.text = articleModel.title
            binding.priceTextView.text = articleModel.price

//            if (articleModel.imageUrl.isNotEmpty()) {
//                Glide.with(binding.thumbnailImageView)
//                    .load(articleModel.imageUrl)
//                    .into(binding.thumbnailImageView)
//            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }
}