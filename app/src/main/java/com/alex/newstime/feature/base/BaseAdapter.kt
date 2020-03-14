package com.alex.newstime.feature.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder>(private val lifecycleOwner: LifecycleOwner)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(), LifecycleObserver {

    protected val items = ArrayList<T>()

    protected val disposables = CompositeDisposable()

    var clickSubject = PublishSubject.create<T>()

    // ----------------------------------------------------------------------------

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    // ----------------------------------------------------------------------------
    // override RecyclerView.Adapter

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
    // override LifecycleObserver

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onLifecycleCreate() {
        println("?==== ON_CREATE")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onLifecycleStart() {
        println("?==== ON_START")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onLifecycleResume() {
        println("?==== ON_RESUME")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onLifecyclePause() {
        println("?==== ON_PAUSE")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onLifecycleStop() {
        println("?==== ON_STOP")
        disposables.clear()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onLifecycleDestroy() {
        println("?==== ON_DESTROY")
        lifecycleOwner.lifecycle.removeObserver(this)
    }

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
        items.apply {
            val count = count()
            clear()
            notifyItemRangeRemoved(0, count)
        }
    }

    // ----------------------------------------------------------------------------

    open fun getItemViewType(item: T) = 0
    open fun onBindViewHolder(holder: VH, item: T) {}
}