package com.alex.newstime.repository.article

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.parceler.Parcel

@Entity
@Parcel
class Article {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
    var title: String? = null
    var urlToImage: String? = null
    var content: String? = null
    var url: String? = null
}