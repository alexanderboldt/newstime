package com.alex.newstime.feature.topheadlines

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alex.newstime.databinding.ItemViewArticleBinding
import com.alex.newstime.feature.BaseAdapter

class TopHeadlinesAdapter : BaseAdapter<UiArticle, TopHeadlinesAdapter.ArticleViewHolder>() {

    class ArticleViewHolder(var binding: ItemViewArticleBinding) : RecyclerView.ViewHolder(binding.root)

    // ----------------------------------------------------------------------------

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ArticleViewHolder(ItemViewArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ArticleViewHolder, item: UiArticle) {
        holder.binding.textViewName.text = item.title
        holder.binding.imageViewThumbnail.setImage(item.urlToImage)
    }
}