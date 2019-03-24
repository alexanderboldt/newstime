package com.alex.newstime.util

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    this.add(disposable)
}

fun Router.pushDetailController(controller: Controller) {
    pushController(RouterTransaction
            .with(controller)
            .pushChangeHandler(HorizontalChangeHandler())
            .popChangeHandler(HorizontalChangeHandler()))
}

fun <T> LiveData<T>.observe(lifecycleOwner: LifecycleOwner, observer: (t: T) -> Unit) {
    this.observe(lifecycleOwner, Observer { data ->
        if (data == null) return@Observer
        observer(data)
    })
}