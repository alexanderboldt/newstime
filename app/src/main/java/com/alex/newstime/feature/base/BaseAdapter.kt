package com.alex.newstime.feature.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<VH : RecyclerView.ViewHolder, ITEM> : RecyclerView.Adapter<VH>() {

    protected val items = mutableListOf<ITEM>()

    // ----------------------------------------------------------------------------

    final override fun getItemCount() = items.size

    final override fun getItemViewType(position: Int) = getItemViewType(items[position])

    final override fun onBindViewHolder(holder: VH, position: Int) {
        onBindViewHolder(holder, items[position])
    }

    // ----------------------------------------------------------------------------

    protected open fun getItemViewType(item: ITEM) = 0
    protected open fun onBindViewHolder(holder: VH, item: ITEM) {}

    // ----------------------------------------------------------------------------

    fun setItems(items: List<ITEM>) {
        this.items.apply {
            clear()
            addAll(items)
        }

        notifyDataSetChanged()
    }
}