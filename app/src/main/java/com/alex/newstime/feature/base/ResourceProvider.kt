package com.alex.newstime.feature.base

import android.app.Application
import androidx.annotation.StringRes

object ResourceProvider {

    private lateinit var application: Application

    // ----------------------------------------------------------------------------

    fun init(application: Application) {
        this.application = application
    }

    // ----------------------------------------------------------------------------

    fun getString(@StringRes resource: Int) = application.getString(resource)
}