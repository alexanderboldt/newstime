package com.alex.newstime.feature.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.NonNull
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import com.bluelinelabs.conductor.archlifecycle.LifecycleController
import io.reactivex.disposables.CompositeDisposable

abstract class BaseController<VB : ViewBinding> : LifecycleController(), LifecycleObserver {

    protected lateinit var binding: VB

    protected val disposables by lazy { CompositeDisposable() }

    protected val context: Context
        get() = activity as Context

    private val viewModelStore = ViewModelStore()

    /**
     * * This extensions-function has a check for nullability and passes the appropriate LifecycleOwner
     */
    internal fun <T> LiveData<T>.observeNotNull(observer: (t: T) -> Unit) {
        this.observe(this@BaseController, Observer { data ->
            if (data == null) return@Observer
            observer(data)
        })
    }

    // ----------------------------------------------------------------------------

    init {
        retainViewMode = RetainViewMode.RETAIN_DETACH
    }

    // ----------------------------------------------------------------------------

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        binding = onCreateBinding(inflater, container)

        lifecycle.addObserver(this)

        return binding.root
    }

    // ----------------------------------------------------------------------------

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    @CallSuper
    open fun onLifecycleCreate() {
        onSetupView()
        onViewModelBinding()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    @CallSuper
    open fun onLifecycleStart() {
        onViewBinding()
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
        viewModelStore.clear()
        lifecycle.removeObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    @CallSuper
    open fun onLifecycleAny() {
    }

    // ----------------------------------------------------------------------------

    abstract fun onCreateBinding(inflater: LayoutInflater, container: ViewGroup): VB
    open fun onSetupView() {}
    open fun onViewBinding() {}
    open fun onViewModelBinding() {}

    fun <VM : ViewModel> getViewModel(@NonNull modelClass: Class<VM>): VM {
        return viewModelProvider().get(modelClass)
    }

    // ----------------------------------------------------------------------------

    fun getStatusBarHeight(): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return when (resourceId > 0) {
            true -> context.resources.getDimensionPixelSize(resourceId)
            false -> 0
        }
    }

    // ----------------------------------------------------------------------------

    private fun viewModelProvider(): ViewModelProvider {
        return viewModelProvider(ViewModelProvider.AndroidViewModelFactory(activity!!.application))
    }

    private fun viewModelProvider(factory: ViewModelProvider.NewInstanceFactory): ViewModelProvider {
        return ViewModelProvider(viewModelStore, factory)
    }
}