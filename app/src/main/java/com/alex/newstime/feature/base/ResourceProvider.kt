package com.alex.newstime.feature.base

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

class ResourceProvider(private val context: Context) {
    fun getString(@StringRes resource: Int) = context.getString(resource)
    fun getColor(resource: Int) = ContextCompat.getColor(context, resource)
}