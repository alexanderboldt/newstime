package com.alex.newstime.feature

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.CompositeDisposable
import work.beltran.conductorviewmodel.ViewModelController

abstract class BaseController<T : ViewDataBinding>(@LayoutRes private val layout: Int) : ViewModelController(), LifecycleObserver {

    protected lateinit var binding: T

    protected val disposables = CompositeDisposable()

    protected val context: Context
        get() = activity as Context

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

        lifecycle.removeObserver(this)
    }

    // ----------------------------------------------------------------------------

    abstract fun onSetupView()
    abstract fun onSetupViewBinding()
    abstract fun onSetupViewModelBinding()
}