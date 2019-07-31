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
    fun insert(article: Article): Single<Long>

    @Insert(onConflict = IGNORE)
    fun insert(articles: List<Article>): Single<List<Long>>

    @Update
    fun update(article: Article): Single<Int>

    @Delete
    fun delete(article: Article): Single<Int>

    @Query("delete from article")
    fun deleteAll(): Single<Int>
}