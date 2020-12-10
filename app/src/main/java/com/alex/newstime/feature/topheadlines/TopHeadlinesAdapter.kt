package com.alex.newstime.feature.topheadlines

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.alex.newstime.databinding.ItemViewArticleBinding
import com.alex.newstime.databinding.ItemViewLoadMoreBinding
import com.alex.newstime.feature.base.BaseAdapter
import com.alex.newstime.feature.topheadlines.model.UiModelRecyclerItem

class TopHeadlinesAdapter(
    private val clickOnArticle: (UiModelRecyclerItem.UiModelArticle) -> Unit,
    private val clickOnLoadMore: () -> Unit) : BaseAdapter<RecyclerView.ViewHolder, UiModelRecyclerItem>() {

    enum class Types {
        PLACEHOLDER,
        ARTICLE,
        LOAD_MORE
    }

    // ----------------------------------------------------------------------------

    inner class PlaceholderViewHolder(val binding: ItemViewArticleBinding) : RecyclerView.ViewHolder(binding.root)
    inner class ArticleViewHolder(val binding: ItemViewArticleBinding) : RecyclerView.ViewHolder(binding.root)
    inner class LoadMoreViewHolder(val binding: ItemViewLoadMoreBinding) : RecyclerView.ViewHolder(binding.root)

    // ----------------------------------------------------------------------------

    override fun getItemViewType(item: UiModelRecyclerItem): Int {
        return when (item) {
            is UiModelRecyclerItem.UiModelPlaceholder -> Types.PLACEHOLDER.ordinal
            is UiModelRecyclerItem.UiModelArticle -> Types.ARTICLE.ordinal
            is UiModelRecyclerItem.UiModelLoadMore -> Types.LOAD_MORE.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Types.PLACEHOLDER.ordinal -> PlaceholderViewHolder(
                ItemViewArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            Types.ARTICLE.ordinal -> ArticleViewHolder(
                ItemViewArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> LoadMoreViewHolder(
                ItemViewLoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: UiModelRecyclerItem) {
        when (holder) {
            is PlaceholderViewHolder -> bindPlaceHolder(holder, item as UiModelRecyclerItem.UiModelPlaceholder)
            is ArticleViewHolder -> bindArticle(holder, item as UiModelRecyclerItem.UiModelArticle)
            is LoadMoreViewHolder -> bindLoadMore(holder, item as UiModelRecyclerItem.UiModelLoadMore)
        }
    }

    // ----------------------------------------------------------------------------

    fun enabledLoadMoreButton(isEnabled: Boolean) {
        val index = items.indexOfLast { item -> item is UiModelRecyclerItem.UiModelLoadMore }
        if (index == -1) return

        (items[index] as UiModelRecyclerItem.UiModelLoadMore).isEnabled = isEnabled

        notifyItemChanged(index)
    }

    // ----------------------------------------------------------------------------

    private fun bindPlaceHolder(holder: PlaceholderViewHolder, placeholder: UiModelRecyclerItem.UiModelPlaceholder) {
        holder.binding.apply {
            textViewSource.setBackgroundColor(placeholder.color)
            textViewDate.setBackgroundColor(placeholder.color)
            textViewTitle.setBackgroundColor(placeholder.color)
            imageViewThumbnail.setBackgroundColor(placeholder.color)
        }
    }

    private fun bindArticle(holder: ArticleViewHolder, article: UiModelRecyclerItem.UiModelArticle) {
        holder.binding.apply {
            textViewSource.text = article.source
            textViewTitle.text = article.title
            imageViewThumbnail.setBackgroundColor(article.colorPlaceholder)
            imageViewThumbnail.load(article.urlToImage)
            textViewDate.text = article.date
        }

        holder.itemView.setOnClickListener {
            clickOnArticle(article)
        }
    }

    private fun bindLoadMore(holder: LoadMoreViewHolder, loadMore: UiModelRecyclerItem.UiModelLoadMore) {
        holder.binding.apply {
            buttonLoadMore.isEnabled = loadMore.isEnabled
            buttonLoadMore.alpha = if (loadMore.isEnabled) 1f else 0.5f
            buttonLoadMore.setOnClickListener {
                clickOnLoadMore()
            }
        }
    }
}