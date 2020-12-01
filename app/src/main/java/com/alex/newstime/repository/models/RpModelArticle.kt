package com.alex.newstime.repository.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class RpModelArticle : Parcelable {
    var id: Long? = null
    var title: String? = null
    var urlToImage: String? = null
    var content: String? = null
    var url: String? = null
}