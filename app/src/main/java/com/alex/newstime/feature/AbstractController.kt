package com.alex.newstime.feature

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.reactivex.disposables.CompositeDisposable
import work.beltran.conductorviewmodel.ViewModelController

abstract class  AbstractController : ViewModelController() {

    protected val disposables = CompositeDisposable()

    // ----------------------------------------------------------------------------

    val activity: AppCompatActivity
        get() = getActivity() as AppCompatActivity

    val context: Context
        get() = activity

    // ----------------------------------------------------------------------------

    override fun onDetach(view: View) {
        disposables.clear()
    }
}