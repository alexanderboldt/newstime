package com.alex.newstime.repository.database.article

import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import io.reactivex.rxjava3.core.Single

@Dao
interface ArticleDao {

    @Query("select * from dbModelArticle")
    fun getAll(): Single<List<DbModelArticle>>

    @Insert(onConflict = IGNORE)
    fun insert(dbArticle: DbModelArticle): Single<Long>

    @Insert(onConflict = IGNORE)
    fun insert(dbArticles: List<DbModelArticle>): Single<List<Long>>

    @Update
    fun update(dbArticle: DbModelArticle): Single<Int>

    @Delete
    fun delete(dbArticle: DbModelArticle): Single<Int>

    @Query("delete from dbModelArticle")
    fun deleteAll(): Single<Int>
}