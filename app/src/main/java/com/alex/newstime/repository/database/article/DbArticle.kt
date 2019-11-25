package com.alex.newstime.repository.database.article

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DbArticle(
    @PrimaryKey
    val id: Long,
    val title: String,
    val urlToImage: String?,
    val content: String?,
    val url: String?)