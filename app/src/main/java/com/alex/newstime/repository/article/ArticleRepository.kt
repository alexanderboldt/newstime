package com.alex.newstime.repository.article

import com.alex.newstime.repository.api.ApiClient
import com.alex.newstime.repository.models.RpModelResponse
import com.alex.newstime.util.mapping.ArticleMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ArticleRepository {

    suspend fun getTopHeadlines(pageSize: Int = 10, page: Int = 1): Flow<RpModelResponse> {
        return flow {
            ApiClient
                .routes
                .getTopHeadlines(pageSize, page)
                .let { response ->
                    RpModelResponse(
                        response.totalResults,
                        ArticleMapper.fromApiToRepository(response.articles))
                }
                .also { emit(it) }
        }.flowOn(Dispatchers.IO)
    }
}