package com.alex.newstime.repository.database.article

import com.alex.newstime.repository.article.Article
import com.alex.newstime.repository.database.NewstimeDatabase
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object ArticleTable {

    fun getAll(): Single<List<Article>> {
        return NewstimeDatabase.database
            .articleDao()
            .getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun insert(article: Article): Single<Long> {
        return NewstimeDatabase
            .database
            .articleDao()
            .insert(article)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun insert(articles: List<Article>): Single<List<Long>> {
        return NewstimeDatabase
            .database
            .articleDao()
            .insert(articles)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun update(article: Article): Single<Int> {
        return NewstimeDatabase
            .database
            .articleDao()
            .update(article)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun delete(article: Article): Single<Int> {
        return NewstimeDatabase
            .database
            .articleDao()
            .delete(article)
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