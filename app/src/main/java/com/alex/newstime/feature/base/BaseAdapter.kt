package com.alex.newstime.feature.base

import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder, VT : Enum<VT>>(
    private val lifecycleOwner: LifecycleOwner? = null,
    private val viewTypes: Array<VT>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(), LifecycleObserver {

    protected val items = ArrayList<T>()

    protected val disposables = CompositeDisposable()

    var clickSubject = PublishSubject.create<T>()

    /**
     * * This extensions-function has a check for nullability and passes the appropriate LifecycleOwner
     */
    internal fun <T> LiveData<T>.observeNotNull(observer: (t: T) -> Unit) {
        if (lifecycleOwner == null) return

        this.observe(lifecycleOwner, Observer { data ->
            if (data == null) return@Observer
            observer(data)
        })
    }

    // ----------------------------------------------------------------------------

    init {
        lifecycleOwner?.lifecycle?.addObserver(this)
    }

    // ----------------------------------------------------------------------------
    // override RecyclerView.Adapter

    final override fun getItemCount() = items.size

    final override fun getItemViewType(position: Int) = getItemViewType(items[position]).ordinal

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return onCreateViewHolder(parent, viewTypes[viewType])
    }

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
    @CallSuper
    open fun onLifecycleCreate() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    @CallSuper
    open fun onLifecycleStart() {
        onViewBinding()
        onViewModelBinding()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    @CallSuper
    open fun onLifecycleResume() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    @CallSuper
    open fun onLifecyclePause() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    @CallSuper
    open fun onLifecycleStop() {
        disposables.clear()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    @CallSuper
    open fun onLifecycleDestroy() {
        lifecycleOwner?.lifecycle?.removeObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    @CallSuper
    open fun onLifecycleAny() {
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

    open fun onViewBinding() {}
    open fun onViewModelBinding() {}

    abstract fun getItemViewType(item: T): VT
    abstract fun onCreateViewHolder(parent: ViewGroup, viewType: VT): VH
    abstract fun onBindViewHolder(holder: VH, item: T)
}