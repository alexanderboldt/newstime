package com.alex.newstime.feature.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

abstract class BaseFragment : Fragment() {

    /**
     * Returns the height of the StatusBar.
     */
    protected fun getStatusBarHeight(): Int {
        return resources.getIdentifier("status_bar_height", "dimen", "android")
            .let { resourceId ->
                when (resourceId > 0) {
                    true -> resources.getDimensionPixelSize(resourceId)
                    false -> 0
                }
            }
    }

    /**
     * Convenient observe-function.
     */
    protected fun <T> LiveData<T>.observe(observer: (t: T) -> Unit) {
        this.observe(viewLifecycleOwner, Observer { data ->
            observer(data)
        })
    }
}