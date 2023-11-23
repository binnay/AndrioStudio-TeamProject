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

    // 아이템 클릭 리스너 인터페이스 정의
    interface OnItemClickListener {
        fun onItemClick(position: Int, item: ArticleModel)
    }

    // 아이템 클릭 리스너 멤버 변수
    private var listener: OnItemClickListener? = null

    // 아이템 클릭 리스너 설정 메서드
    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    inner class ViewHolder(private val binding: ItemArticleBinding): RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SimpleDateFormat")
        fun bind(articleModel: ArticleModel) {

            binding.titleTextView.text = articleModel.title
            binding.priceTextView.text = articleModel.price
            binding.userIdTextView.text = articleModel.userId
            val isSold = articleModel.isSold
            binding.isSoldTextView.text = if (isSold) "판매완료" else "판매중"
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
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)

        // 아이템 뷰 클릭 시 호출되는 리스너 설정
        binding.root.setOnClickListener {
            val position = viewHolder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val item = itemList[position]
                listener?.onItemClick(position, item)
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }
}