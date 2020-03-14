package com.alex.newstime.feature.base

import android.content.Context
import androidx.annotation.StringRes

object ResourceProvider {

    private lateinit var context: Context

    // ----------------------------------------------------------------------------

    fun init(context: Context) {
        this.context = context
    }

    // ----------------------------------------------------------------------------

    fun getString(@StringRes resource: Int) = context.getString(resource)
}