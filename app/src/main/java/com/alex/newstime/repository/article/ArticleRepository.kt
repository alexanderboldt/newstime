package com.alex.newstime.repository.article

import com.alex.newstime.repository.api.article.ArticleRoutes
import com.alex.newstime.repository.database.article.ArticleTable
import com.alex.newstime.repository.database.article.DbModelArticle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

open class ArticleRepository {

    private val articleRoutes = ArticleRoutes()
    private val articleTable = ArticleTable()

    // ----------------------------------------------------------------------------

    open fun getTopHeadlines(pageSize: Int = 10, page: Int = 1): Single<List<RpModelArticle>> {
        return articleRoutes
            .getTopHeadlines(pageSize, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { articles ->
                articles.map { article ->
                    RpModelArticle().apply {
                        id = article.title.hashCode().toLong()
                        title = article.title
                        urlToImage = article.urlToImage
                        content = article.content
                        url = article.url
                    }
                }
            }
    }

    open fun getEverything(pageSize: Int = 10, page: Int = 1): Single<List<RpModelArticle>> {
        return articleRoutes
            .getEverything(pageSize, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { articles ->
                articles.map { article ->
                    RpModelArticle().apply {
                        id = article.title.hashCode().toLong()
                        title = article.title
                        urlToImage = article.urlToImage
                        content = article.content
                        url = article.url
                    }
                }
            }
    }

    open fun getFavorites(): Single<List<RpModelArticle>> {
        return articleTable
            .getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { articles ->
                articles.map { article ->
                    RpModelArticle().apply {
                        id = article.title.hashCode().toLong()
                        title = article.title
                        urlToImage = article.urlToImage
                        content = article.content
                        url = article.url
                    }
                }
        }
    }

    open fun setFavorite(article: RpModelArticle) = articleTable.insert(DbModelArticle(article.id!!, article.title!!, article.urlToImage, article.content, article.url)).ignoreElement()
    open fun deleteFavorite(article: RpModelArticle) = articleTable.delete(DbModelArticle(article.id!!, article.title!!, article.urlToImage, article.content, article.url)).ignoreElement()
}