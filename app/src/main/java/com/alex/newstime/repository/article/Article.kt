package com.alex.newstime.repository.article

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Article : Parcelable {
    var id: Long? = null
    var title: String? = null
    var urlToImage: String? = null
    var content: String? = null
    var url: String? = null
}