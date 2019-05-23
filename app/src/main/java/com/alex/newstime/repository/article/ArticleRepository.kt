package com.alex.newstime.repository.article

import com.alex.newstime.repository.api.article.ArticleRoutes
import com.alex.newstime.repository.database.article.ArticleTable
import io.reactivex.Single

open class ArticleRepository {

    open fun getTopHeadlines(pageSize: Int = 10, page: Int = 1): Single<List<Article>> {
        return ArticleRoutes
            .getTopHeadlines(pageSize, page)
            .doOnSuccess {
                ArticleTable.insert(it).subscribe()
            }
//            .onErrorResumeNext {
//                ArticleTable.getAll().map {
//                    if (it.isEmpty()) {
//                        val article = Article()
//                        article.id = 87242323
//                        article.title = "Test Article"
//                        article.urlToImage = "http://icanbecreative.com/resources/files/articles/deadpool-movie-photoshop-tutorial/deadpool-movie-logo-photoshop-tutorial.jpg"
//
//                        listOf(article, article, article)
//                    } else {
//                        it
//                    }
//                }
//            }
    }

    open fun getEverything(pageSize: Int = 10, page: Int = 1): Single<List<Article>> {
        return ArticleRoutes
            .getEverything(pageSize, page)
            .doOnSuccess {
                ArticleTable.insert(it).subscribe()
            }
//            .onErrorResumeNext {
//                ArticleTable.getAll().map {
//                    if (it.isEmpty()) {
//                        val article = Article()
//                        article.id = 87242323
//                        article.title = "Test Article"
//                        article.urlToImage = "http://icanbecreative.com/resources/files/articles/deadpool-movie-photoshop-tutorial/deadpool-movie-logo-photoshop-tutorial.jpg"
//
//                        listOf(article, article, article)
//                    } else {
//                        it
//                    }
//                }
//            }
    }
}