package com.alex.newstime.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.alex.newstime.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions

class GlideImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ImageView(context, attrs) {

    fun setImage(url: String?) {
        val options = RequestOptions()
            .error(R.drawable.ic_image)
            .centerCrop()

        Glide.with(context)
            .load(url)
            .apply(options)
            .into(this)
    }
}