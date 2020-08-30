package com.alex.newstime.feature.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseFragment<VB : ViewBinding> : Fragment(), LifecycleObserver {

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding!!

    protected val disposables by lazy { CompositeDisposable() }

    // ----------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = inflateView(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
        bindViewModel()
    }

    // ----------------------------------------------------------------------------

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    @CallSuper
    open fun onLifecycleCreate() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    @CallSuper
    open fun onLifecycleStart() {
        bindView()
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
        lifecycle.removeObserver(this)
        _binding = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    @CallSuper
    open fun onLifecycleAny() {
    }

    // ----------------------------------------------------------------------------

    abstract fun inflateView(inflater: LayoutInflater, container: ViewGroup?): VB
    open fun setupView() {}
    open fun bindView() {}
    open fun bindViewModel() {}

    // ----------------------------------------------------------------------------

    protected fun getStatusBarHeight(): Int {
        return resources.getIdentifier("status_bar_height", "dimen", "android")
            .let { resourceId ->
                when (resourceId > 0) {
                    true -> resources.getDimensionPixelSize(resourceId)
                    false -> 0
                }
            }
    }

    /*
     * This extensions-function has build-in check for nullability.
     */
    fun <T> LiveData<T>.observe(observer: (t: T) -> Unit) {
        this.observe(viewLifecycleOwner, Observer { data ->
            if (data == null) return@Observer
            observer(data)
        })
    }
}