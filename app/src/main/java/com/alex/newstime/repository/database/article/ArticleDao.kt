package com.alex.newstime.repository.database.article

import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import io.reactivex.rxjava3.core.Single

@Dao
interface ArticleDao {

    @Query("select * from dbArticle")
    fun getAll(): Single<List<DbArticle>>

    @Insert(onConflict = IGNORE)
    fun insert(dbArticle: DbArticle): Single<Long>

    @Insert(onConflict = IGNORE)
    fun insert(dbArticles: List<DbArticle>): Single<List<Long>>

    @Update
    fun update(dbArticle: DbArticle): Single<Int>

    @Delete
    fun delete(dbArticle: DbArticle): Single<Int>

    @Query("delete from dbArticle")
    fun deleteAll(): Single<Int>
}