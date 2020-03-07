package com.alex.newstime.feature.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.*
import com.bluelinelabs.conductor.archlifecycle.LifecycleController
import io.reactivex.disposables.CompositeDisposable

abstract class BaseController<T : ViewDataBinding>(@LayoutRes private val layout: Int) : LifecycleController(), LifecycleObserver {

    protected lateinit var binding: T

    protected val disposables by lazy { CompositeDisposable() }

    protected val context: Context
        get() = activity as Context

    private val viewModelStore = ViewModelStore()

    /**
     * * This extensions-function has a check for nullability and passes the appropriate LifecycleOwner
     */
    internal fun <T> LiveData<T>.observeNotNull(observer: (t: T) -> Unit) {
        this.observe(activity as AppCompatActivity, Observer { data ->
            if (data == null) return@Observer
            observer(data)
        })
    }

    // ----------------------------------------------------------------------------

    init {
        retainViewMode = RetainViewMode.RETAIN_DETACH
    }

    // ----------------------------------------------------------------------------

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        binding = DataBindingUtil.inflate(inflater, layout, container, false)
        binding.lifecycleOwner = this

        lifecycle.addObserver(this)

        return binding.root
    }

    override fun onDestroy() {
        viewModelStore.clear()
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

    fun <VM : ViewModel> getViewModel(@NonNull modelClass: Class<VM>): VM {
        return viewModelProvider().get(modelClass)
    }

    // ----------------------------------------------------------------------------

    private fun viewModelProvider(): ViewModelProvider {
        return viewModelProvider(ViewModelProvider.AndroidViewModelFactory(activity!!.application))
    }

    private fun viewModelProvider(factory: ViewModelProvider.NewInstanceFactory): ViewModelProvider {
        return ViewModelProvider(viewModelStore, factory)
    }
}