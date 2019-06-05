package com.alex.newstime.repository.article

import com.alex.newstime.repository.api.article.ArticleRoutes
import com.alex.newstime.repository.database.article.ArticleTable
import io.reactivex.Completable

open class ArticleRepository {

    open fun getTopHeadlines(pageSize: Int = 10, page: Int = 1) = ArticleRoutes.getTopHeadlines(pageSize, page)
    open fun getEverything(pageSize: Int = 10, page: Int = 1) = ArticleRoutes.getEverything(pageSize, page)

    open fun getFavorites() = ArticleTable.getAll()

    open fun setFavorite(article: Article): Completable = ArticleTable.insert(article).ignoreElement()
    open fun deleteFavorite(article: Article): Completable = ArticleTable.delete(article).ignoreElement()
}