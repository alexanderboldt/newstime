package com.alex.newstime.feature.topheadlines.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.alex.newstime.databinding.ItemViewArticleBinding
import com.alex.newstime.databinding.ItemViewLoadMoreBinding
import com.alex.newstime.feature.base.BaseAdapter
import com.alex.newstime.feature.topheadlines.TopHeadlinesViewModel
import com.alex.newstime.feature.topheadlines.model.ArticleModel
import com.alex.newstime.feature.topheadlines.model.BaseModel
import com.alex.newstime.feature.topheadlines.model.LoadMoreModel
import com.alex.newstime.util.plusAssign
import com.jakewharton.rxbinding4.view.longClicks
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class TopHeadlinesAdapter(lifecycleOwner: LifecycleOwner, private val viewModel: TopHeadlinesViewModel)
    : BaseAdapter<BaseModel, RecyclerView.ViewHolder, ViewType>(lifecycleOwner, ViewType.values()) {

    class ArticleViewHolder(var binding: ItemViewArticleBinding) : RecyclerView.ViewHolder(binding.root)
    class LoadMoreViewHolder(var binding: ItemViewLoadMoreBinding) : RecyclerView.ViewHolder(binding.root)

    private var longClickSubject = PublishSubject.create<ArticleModel>()

    // ----------------------------------------------------------------------------

    override fun getItemViewType(item: BaseModel) =
        when (item) {
            is ArticleModel -> ViewType.ARTICLE
            is LoadMoreModel -> ViewType.LOAD_MORE
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: ViewType): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.ARTICLE -> ArticleViewHolder(
                ItemViewArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> LoadMoreViewHolder(
                ItemViewLoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: BaseModel) {
        when (holder) {
            is ArticleViewHolder -> bindArticle(holder, item as ArticleModel)
            is LoadMoreViewHolder -> bindLoadMore(holder, item as LoadMoreModel)
        }
    }

    // ----------------------------------------------------------------------------

    override fun onViewBinding() {
        disposables += clickSubject.subscribe {
            when (it) {
                is ArticleModel -> viewModel.clickOnArticle(it)
                is LoadMoreModel -> viewModel.loadMoreArticles()
            }
        }

        disposables += longClickSubject.subscribe {
            viewModel.longClickArticle(it)
        }
    }

    override fun onViewModelBinding() {
        viewModel.recyclerArticlesState.observeNotNull { data ->
            setItems(data as ArrayList)
        }

        viewModel.recyclerLoadMoreState.observeNotNull { enabled ->
            (items.last() as LoadMoreModel).enabled = enabled
            notifyDataSetChanged()
        }
    }

    // ----------------------------------------------------------------------------

    private fun bindArticle(holder: ArticleViewHolder, item: ArticleModel) {
        holder.binding.textViewName.text = item.title
        //holder.binding.textViewName.transitionName = item.title

        holder.binding.imageViewThumbnail.setImage(item.urlToImage)
        //holder.binding.imageViewThumbnail.transitionName = item.urlToImage

        holder.itemView.longClicks()
            .throttleFirst(1, TimeUnit.SECONDS)
            .map { item as ArticleModel }
            .subscribe(longClickSubject)
    }

    private fun bindLoadMore(holder: LoadMoreViewHolder, item: LoadMoreModel) {
        holder.binding.root.alpha = if (item.enabled) 1f else 0.5f
    }
}