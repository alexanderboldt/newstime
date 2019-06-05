package com.alex.newstime.repository.database.article

import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import com.alex.newstime.repository.article.Article
import io.reactivex.Single

@Dao
interface ArticleDao {

    @Query("select * from article")
    fun getAll(): Single<List<Article>>

    @Insert(onConflict = IGNORE)
    fun insert(article: Article): Long

    @Insert(onConflict = IGNORE)
    fun insert(articles: List<Article>): List<Long>

    @Update
    fun update(article: Article): Int

    @Delete
    fun delete(article: Article): Int

    @Query("delete from article")
    fun deleteAll(): Int
}