package com.alex.newstime.feature.topheadlines

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.alex.newstime.bus.ConnectivityEvent
import com.alex.newstime.databinding.ItemViewArticleBinding
import com.alex.newstime.databinding.ItemViewLoadMoreBinding
import com.alex.newstime.feature.topheadlines.model.UiModelRecyclerItem
import org.greenrobot.eventbus.EventBus

class TopHeadlinesAdapter(
    val clickOnArticle: (UiModelRecyclerItem.UiModelArticle) -> Unit,
    val clickOnLoadMore: () -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = ArrayList<UiModelRecyclerItem>()

    // ----------------------------------------------------------------------------

    class ArticleViewHolder(val binding: ItemViewArticleBinding) : RecyclerView.ViewHolder(binding.root)
    class LoadMoreViewHolder(binding: ItemViewLoadMoreBinding) : RecyclerView.ViewHolder(binding.root)

    // ----------------------------------------------------------------------------

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is UiModelRecyclerItem.UiModelArticle) 0 else 1
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> ArticleViewHolder(
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
            is LoadMoreViewHolder -> bindLoadMore(holder)
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

    private fun bindArticle(holder: ArticleViewHolder, item: UiModelRecyclerItem.UiModelArticle) {
        holder.binding.textViewName.text = item.title
        //holder.binding.textViewName.transitionName = item.title

        holder.binding.imageViewThumbnail.load(item.urlToImage)
        //holder.binding.imageViewThumbnail.transitionName = item.urlToImage

        holder.itemView.setOnClickListener {
            clickOnArticle(item)
        }
    }

    private fun bindLoadMore(holder: LoadMoreViewHolder) {
        holder.itemView.setOnClickListener {
            clickOnLoadMore()
        }
    }
}