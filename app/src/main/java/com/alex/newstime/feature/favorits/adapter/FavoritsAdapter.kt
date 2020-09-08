package com.alex.newstime.feature.favorits.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.alex.newstime.databinding.ItemViewArticleBinding
import com.alex.newstime.feature.base.BaseAdapter
import com.alex.newstime.feature.favorits.FavoritsViewModel
import com.alex.newstime.feature.favorits.model.ArticleState
import com.alex.newstime.util.plusAssign

class FavoritsAdapter(lifecycleOwner: LifecycleOwner, private val viewModel: FavoritsViewModel)
    : BaseAdapter<ArticleState, FavoritsAdapter.ArticleViewHolder, ViewType>(lifecycleOwner, ViewType.values()) {

    class ArticleViewHolder(var binding: ItemViewArticleBinding) : RecyclerView.ViewHolder(binding.root)

    // ----------------------------------------------------------------------------

    override fun getItemViewType(item: ArticleState) = ViewType.ARTICLE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: ViewType): ArticleViewHolder {
        return ArticleViewHolder(
            ItemViewArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, item: ArticleState) {
        holder.binding.apply {
            textViewName.text = item.title
            imageViewThumbnail.setImage(item.urlToImage)
        }
    }

    // ----------------------------------------------------------------------------

    override fun onViewBinding() {
        disposables += clickSubject.subscribe {
            viewModel.clickOnArticle(it)
        }
    }

    override fun onViewModelBinding() {
        viewModel.recyclerArticlesState.observeNotNull {
            setItems(it as ArrayList)
        }
    }
}