package com.alex.newstime.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alex.newstime.repository.article.Article
import com.alex.newstime.repository.database.article.ArticleDao

@Database(entities = arrayOf(Article::class), version = 1)
abstract class NewstimeDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao

    companion object {
        lateinit var database: NewstimeDatabase

        fun init(context: Context) {
            database = Room.databaseBuilder(context, NewstimeDatabase::class.java, "newstime_database.db").build()
        }
    }
}