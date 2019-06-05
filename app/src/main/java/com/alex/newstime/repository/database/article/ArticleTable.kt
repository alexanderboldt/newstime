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
        return Single
            .create<Long> { emitter ->
                val id = NewstimeDatabase.database.articleDao().insert(article)

                emitter.onSuccess(id)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun insert(articles: List<Article>): Single<List<Long>> {
        return Single
            .create<List<Long>> { emitter ->
                val ids = NewstimeDatabase.database.articleDao().insert(articles)

                emitter.onSuccess(ids)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun update(article: Article): Single<Int> {
        return Single
            .create<Int> { emitter ->
                val numberOfUpdatedRows = NewstimeDatabase.database.articleDao().update(article)

                emitter.onSuccess(numberOfUpdatedRows)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun delete(article: Article): Single<Int> {
        return Single
            .create<Int> { emitter ->
                val numberOfDeletedRows = NewstimeDatabase.database.articleDao().delete(article)

                emitter.onSuccess(numberOfDeletedRows)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun deleteAll(): Single<Int> {
        return Single
            .create<Int> { emitter ->
                val numberOfDeletedRows = NewstimeDatabase.database.articleDao().deleteAll()

                emitter.onSuccess(numberOfDeletedRows)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}