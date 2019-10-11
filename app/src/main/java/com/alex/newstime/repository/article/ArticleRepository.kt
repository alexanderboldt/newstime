package com.alex.newstime.repository.article

import com.alex.newstime.repository.api.article.ArticleRoutes
import com.alex.newstime.repository.database.article.ArticleTable
import com.alex.newstime.repository.sharedpreference.RxSharedPreferencesBase
import io.reactivex.Completable

open class ArticleRepository {

    private val articleRoutes = ArticleRoutes()
    private val articleTable = ArticleTable()

    // ----------------------------------------------------------------------------

    open fun getTopHeadlines(pageSize: Int = 10, page: Int = 1) = articleRoutes.getTopHeadlines(pageSize, page)
    open fun getEverything(pageSize: Int = 10, page: Int = 1) = articleRoutes.getEverything(pageSize, page)

    open fun getFavorites() = articleTable.getAll()

    open fun setFavorite(article: Article) = articleTable.insert(article).ignoreElement()
    open fun deleteFavorite(article: Article) = articleTable.delete(article).ignoreElement()
}