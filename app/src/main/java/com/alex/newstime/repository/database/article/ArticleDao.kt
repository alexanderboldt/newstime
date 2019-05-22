package com.alex.newstime.repository.database.article

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.alex.newstime.repository.article.Article
import io.reactivex.Single

@Dao
interface ArticleDao {

    @Query("select * from article")
    fun getAll(): Single<List<Article>>

    @Insert(onConflict = REPLACE)
    fun insert(article: Article): Long

    @Insert(onConflict = REPLACE)
    fun insert(articles: List<Article>): List<Long>

    @Query("delete from article")
    fun deleteAll(): Int
}