package com.alex.newstime.feature

import android.support.v7.widget.RecyclerView
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

abstract class BaseAdapter<T, VH> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = ArrayList<T>()

    var clickSubject = PublishSubject.create<T>()

    // ----------------------------------------------------------------------------

    fun clearItems() {
        this.items.clear()

        notifyDataSetChanged()
    }

    fun setItems(items: ArrayList<T>) {
        this.items.clear()
        this.items.addAll(items)

        notifyDataSetChanged()
    }

    // ----------------------------------------------------------------------------

    final override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[holder.adapterPosition]

        holder.itemView.clicks()
            .throttleFirst(1, TimeUnit.SECONDS)
            .map { item }
            .subscribe(clickSubject)

        onBindViewHolder(holder as VH, item)
    }

    abstract fun onBindViewHolder(holder: VH, item: T)
}