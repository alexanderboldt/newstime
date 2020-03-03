package com.alex.newstime.feature.favorits

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alex.newstime.databinding.ItemViewArticleBinding
import com.alex.newstime.feature.base.BaseAdapter
import com.alex.newstime.feature.favorits.model.ArticleState

class FavoritsAdapter : BaseAdapter<ArticleState, FavoritsAdapter.ArticleViewHolder>() {

    class ArticleViewHolder(var binding: ItemViewArticleBinding) : RecyclerView.ViewHolder(binding.root)

    // ----------------------------------------------------------------------------

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ArticleViewHolder(ItemViewArticleBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, item: ArticleState) {
        holder.binding.apply {
            textViewName.text = item.title
            imageViewThumbnail.setImage(item.urlToImage)
        }
    }
}