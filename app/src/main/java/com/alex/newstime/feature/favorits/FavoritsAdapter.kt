package com.alex.newstime.feature.favorits

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.alex.newstime.databinding.ItemViewArticleBinding
import com.alex.newstime.feature.base.BaseAdapter
import com.alex.newstime.feature.favorits.model.ArticleState
import com.alex.newstime.util.plusAssign
import io.reactivex.disposables.CompositeDisposable

class FavoritsAdapter(lifecycleOwner: LifecycleOwner, private val viewModel: FavoritsViewModel)
    : BaseAdapter<ArticleState, FavoritsAdapter.ArticleViewHolder>(lifecycleOwner) {

    class ArticleViewHolder(var binding: ItemViewArticleBinding) : RecyclerView.ViewHolder(binding.root)

    // ----------------------------------------------------------------------------

    init {
        disposables += clickSubject.subscribe {
            viewModel.clickOnArticle(it)
        }

        viewModel.recyclerArticlesState.observe(lifecycleOwner, Observer {
            setItems(it as ArrayList)
        })
    }

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