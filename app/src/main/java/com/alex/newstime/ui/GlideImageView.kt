package com.alex.newstime.ui

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.alex.newstime.R
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import jp.wasabeef.glide.transformations.BlurTransformation

@BindingAdapter(value = ["url", "blur"], requireAll = false)
fun setImage(glideImageView: GlideImageView, url: String?, blur: Boolean = false) {
    glideImageView.setImage(url, blur)
}

class GlideImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ImageView(context, attrs) {

    fun setImage(url: String?, blur: Boolean = false) {
        var transformations = arrayOf<Transformation<Bitmap>>()
        if (blur) transformations = transformations.plus(BlurTransformation(25, 20))
        transformations = transformations.plus(CenterCrop())

        val defaultImage = if (!blur) resources.getDrawable(R.drawable.ic_image, null) else null

        GlideApp.with(context)
            .load(url)
            .transform(*transformations)
            .placeholder(defaultImage)
            .into(this)
    }
}