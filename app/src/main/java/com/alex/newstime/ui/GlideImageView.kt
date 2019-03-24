package com.alex.newstime.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class GlideImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
    : ImageView(context, attrs) {

    // ----------------------------------------------------------------------------

    fun setImage(url: String?) {
        Glide.with(context)
                .load(url)
                .apply(RequestOptions.centerCropTransform())
                .into(this)
    }
}