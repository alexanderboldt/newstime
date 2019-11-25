package com.alex.newstime.repository.article

import org.parceler.Parcel

@Parcel
class Article {
    var id: Long? = null
    var title: String? = null
    var urlToImage: String? = null
    var content: String? = null
    var url: String? = null
}