package com.alex.newstime.repository.database.article

import com.alex.newstime.repository.database.NewstimeDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class ArticleTable {

    fun getAll(): Single<List<DbArticle>> {
        return NewstimeDatabase
            .database
            .articleDao()
            .getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun insert(dbArticle: DbArticle): Single<Long> {
        return NewstimeDatabase
            .database
            .articleDao()
            .insert(dbArticle)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun insert(dbArticles: List<DbArticle>): Single<List<Long>> {
        return NewstimeDatabase
            .database
            .articleDao()
            .insert(dbArticles)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun update(dbArticle: DbArticle): Single<Int> {
        return NewstimeDatabase
            .database
            .articleDao()
            .update(dbArticle)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun delete(dbArticle: DbArticle): Single<Int> {
        return NewstimeDatabase
            .database
            .articleDao()
            .delete(dbArticle)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun deleteAll(): Single<Int> {
        return NewstimeDatabase
            .database
            .articleDao()
            .deleteAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}