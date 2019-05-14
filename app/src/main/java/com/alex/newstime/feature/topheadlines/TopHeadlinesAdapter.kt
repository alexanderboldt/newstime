package com.alex.newstime.feature.topheadlines

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alex.newstime.databinding.ItemViewArticleBinding
import com.alex.newstime.databinding.ItemViewLoadMoreBinding
import com.alex.newstime.feature.BaseAdapter

class TopHeadlinesAdapter : BaseAdapter<BaseModel, RecyclerView.ViewHolder>() {

    class ArticleViewHolder(var binding: ItemViewArticleBinding) : RecyclerView.ViewHolder(binding.root)
    class LoadMoreViewHolder(var binding: ItemViewLoadMoreBinding) : RecyclerView.ViewHolder(binding.root)

    // ----------------------------------------------------------------------------

    override fun getItemViewType(item: BaseModel): Int {
        return when (item) {
            is ArticleModel -> 0
            is LoadMoreModel -> 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> ArticleViewHolder(ItemViewArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> LoadMoreViewHolder(ItemViewLoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: BaseModel) {
        when (holder) {
            is ArticleViewHolder -> {
                item as ArticleModel
                holder.binding.textViewName.text = item.title
                holder.binding.imageViewThumbnail.setImage(item.urlToImage)
            }
        }
    }
}