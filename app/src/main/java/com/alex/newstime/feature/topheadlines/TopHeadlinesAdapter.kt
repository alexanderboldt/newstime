package com.alex.newstime.feature.topheadlines

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alex.newstime.databinding.ItemViewArticleBinding
import com.alex.newstime.databinding.ItemViewLoadMoreBinding
import com.alex.newstime.feature.base.BaseAdapter

class TopHeadlinesAdapter : BaseAdapter<BaseModel, RecyclerView.ViewHolder>() {

    class ArticleViewHolder(var binding: ItemViewArticleBinding) : RecyclerView.ViewHolder(binding.root)
    class LoadMoreViewHolder(var binding: ItemViewLoadMoreBinding) : RecyclerView.ViewHolder(binding.root)

    enum class Types {
        ARTICLE,
        LOAD_MORE
    }

    // ----------------------------------------------------------------------------

    override fun getItemViewType(item: BaseModel): Int {
        return when (item) {
            is ArticleModel -> Types.ARTICLE.ordinal
            is LoadMoreModel -> Types.LOAD_MORE.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Types.ARTICLE.ordinal -> ArticleViewHolder(ItemViewArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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
            is LoadMoreViewHolder -> {
                item as LoadMoreModel
                holder.binding.root.alpha = if (item.enabled) 1f else 0.5f
            }
        }
    }

    // ----------------------------------------------------------------------------

    fun enableLoadMore(enable: Boolean) {
        (items.last() as LoadMoreModel).enabled = enable
        notifyDataSetChanged()
    }
}