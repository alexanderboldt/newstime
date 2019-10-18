package com.alex.newstime.feature.base

import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected val items = ArrayList<T>()

    var clickSubject = PublishSubject.create<T>()

    // ----------------------------------------------------------------------------

    fun setItems(items: ArrayList<T>) {
        this.items.apply {
            clear()
            addAll(items)
        }

        notifyDataSetChanged()
    }

    fun insertItem(item: T) {
        items.add(item)

        notifyItemInserted(items.lastIndex)
    }

    fun update(index: Int, item: T) {
        items[index] = item

        notifyItemChanged(index)
    }

    fun removeItem(index: Int) {
        items.removeAt(index)

        notifyItemRemoved(index)
    }

    fun clearItems() {
        val count = this.items.count()

        this.items.clear()

        notifyItemRangeRemoved(0, count)
    }

    // ----------------------------------------------------------------------------

    final override fun getItemCount() = items.size

    final override fun getItemViewType(position: Int) = getItemViewType(items[position])

    final override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[holder.adapterPosition]

        holder.itemView.clicks()
            .throttleFirst(1, TimeUnit.SECONDS)
            .map { item }
            .subscribe(clickSubject)

        onBindViewHolder(holder as VH, item)
    }

    // ----------------------------------------------------------------------------

    open fun getItemViewType(item: T) = 0
    open fun onBindViewHolder(holder: VH, item: T) {}
}