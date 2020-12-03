package com.alex.newstime.feature.topheadlines

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.alex.newstime.databinding.ItemViewArticleBinding
import com.alex.newstime.databinding.ItemViewLoadMoreBinding
import com.alex.newstime.feature.topheadlines.model.UiModelRecyclerItem

class TopHeadlinesAdapter(
    val clickOnArticle: (UiModelRecyclerItem.UiModelArticle) -> Unit,
    val clickOnLoadMore: () -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = ArrayList<UiModelRecyclerItem>()

    private val TYPE_ARTICLE = 0
    private val TYPE_LOAD_MORE = 1

    // ----------------------------------------------------------------------------

    inner class ArticleViewHolder(val binding: ItemViewArticleBinding) : RecyclerView.ViewHolder(binding.root)
    inner class LoadMoreViewHolder(val binding: ItemViewLoadMoreBinding) : RecyclerView.ViewHolder(binding.root)

    // ----------------------------------------------------------------------------

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is UiModelRecyclerItem.UiModelArticle) TYPE_ARTICLE else TYPE_LOAD_MORE
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ARTICLE -> ArticleViewHolder(
                ItemViewArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> LoadMoreViewHolder(
                ItemViewLoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[holder.adapterPosition]

        when (holder) {
            is ArticleViewHolder -> bindArticle(holder, item as UiModelRecyclerItem.UiModelArticle)
            is LoadMoreViewHolder -> bindLoadMore(holder, item as UiModelRecyclerItem.UiModelLoadMore)
        }
    }

    // ----------------------------------------------------------------------------

    fun setItems(items: List<UiModelRecyclerItem>) {
        this.items.apply {
            clear()
            addAll(items)
        }

        notifyDataSetChanged()
    }

    // ----------------------------------------------------------------------------

    private fun bindArticle(holder: ArticleViewHolder, article: UiModelRecyclerItem.UiModelArticle) {
        // animation
        //holder.binding.textViewName.transitionName = item.title
        //holder.binding.imageViewThumbnail.transitionName = item.urlToImage

        holder.binding.apply {
            textViewSource.text = article.source
            textViewTitle.text = article.title
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