package com.alex.newstime.repository.article

import com.alex.newstime.repository.api.article.ArticleRoutes
import com.alex.newstime.repository.database.article.ArticleTable
import com.alex.newstime.repository.database.article.DbArticle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

open class ArticleRepository {

    private val articleRoutes = ArticleRoutes()
    private val articleTable = ArticleTable()

    // ----------------------------------------------------------------------------

    open fun getTopHeadlines(pageSize: Int = 10, page: Int = 1): Single<List<Article>> {
        return articleRoutes
            .getTopHeadlines(pageSize, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { articles ->
                articles.map { article ->
                    Article().apply {
                        id = article.title.hashCode().toLong()
                        title = article.title
                        urlToImage = article.urlToImage
                        content = article.content
                        url = article.url
                    }
                }
            }
    }

    open fun getEverything(pageSize: Int = 10, page: Int = 1): Single<List<Article>> {
        return articleRoutes
            .getEverything(pageSize, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { articles ->
                articles.map { article ->
                    Article().apply {
                        id = article.title.hashCode().toLong()
                        title = article.title
                        urlToImage = article.urlToImage
                        content = article.content
                        url = article.url
                    }
                }
            }
    }

    open fun getFavorites(): Single<List<Article>> {
        return articleTable
            .getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { articles ->
                articles.map { article ->
                    Article().apply {
                        id = article.title.hashCode().toLong()
                        title = article.title
                        urlToImage = article.urlToImage
                        content = article.content
                        url = article.url
                    }
                }
        }
    }

    open fun setFavorite(article: Article) = articleTable.insert(DbArticle(article.id!!, article.title!!, article.urlToImage, article.content, article.url)).ignoreElement()
    open fun deleteFavorite(article: Article) = articleTable.delete(DbArticle(article.id!!, article.title!!, article.urlToImage, article.content, article.url)).ignoreElement()
}