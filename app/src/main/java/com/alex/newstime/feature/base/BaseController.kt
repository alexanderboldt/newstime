package com.alex.newstime.feature.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.*
import io.reactivex.disposables.CompositeDisposable
import work.beltran.conductorviewmodel.ViewModelController

abstract class BaseController<T : ViewDataBinding>(@LayoutRes private val layout: Int) : ViewModelController(), LifecycleObserver {

    protected lateinit var binding: T

    protected val disposables = CompositeDisposable()

    protected val context: Context
        get() = activity as Context

    /*
     * This extensions-function has a check for nullability and passes the appropriate LifecycleOwner
     */
    internal fun <T> LiveData<T>.observe(observer: (t: T) -> Unit) {
        this.observe(binding.lifecycleOwner!!, Observer { data ->
            if (data == null) return@Observer
            observer(data)
        })
    }

    // ----------------------------------------------------------------------------

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        binding = DataBindingUtil.inflate(inflater, layout, container, false)
        binding.lifecycleOwner = this

        lifecycle.addObserver(this)

        return binding.root
    }

    // ----------------------------------------------------------------------------

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onLifecycleCreate() {
        onSetupView()
        onSetupViewModelBinding()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onLifecycleStart() {
        onSetupViewBinding()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onLifecycleStop() {
        disposables.clear()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onLifecycleDestroy() {
        lifecycle.removeObserver(this)
    }

    // ----------------------------------------------------------------------------

    abstract fun onSetupView()
    abstract fun onSetupViewBinding()
    abstract fun onSetupViewModelBinding()
}