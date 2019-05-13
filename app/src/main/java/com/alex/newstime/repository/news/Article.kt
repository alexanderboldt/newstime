package com.alex.newstime.repository.news

import org.parceler.Parcel

@Parcel
class Article {
    var id: String? = null
    var title: String? = null
    var urlToImage: String? = null
    var content: String? = null
    var url: String? = null
}