package com.alex.newstime.repository.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RpModelArticle(
    var title: String,
    var urlToImage: String? = null,
    var content: String? = null,
    var url: String,
    var publishedAt: String,
    var source: String) : Parcelable