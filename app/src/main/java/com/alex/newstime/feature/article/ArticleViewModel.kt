package com.alex.newstime.feature.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alex.newstime.feature.article.model.UiModelArticle
import com.alex.newstime.repository.models.RpModelArticle
import com.hadilq.liveevent.LiveEvent
import org.threeten.bp.Instant
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter

class ArticleViewModel(private val article: RpModelArticle) : ViewModel() {

    private val _dataState = MutableLiveData<UiModelArticle>()
    val dataState: LiveData<UiModelArticle> = _dataState

    private val _linkState = MutableLiveData<String>()
    val linkState: LiveData<String> = _linkState

    private val _closeState = LiveEvent<Unit>()
    val closeState: LiveData<Unit> = _closeState

    // ----------------------------------------------------------------------------

    init {
        article
            .run { UiModelArticle(urlToImage, source, formatPublishDate(publishedAt), title, content) }
            .also { _dataState.postValue(it) }
    }

    fun handleClickOnLink() {
        _linkState.postValue(article.url)
    }

    fun handleClickBack() {
        _closeState.postValue(Unit)
    }

    // ----------------------------------------------------------------------------

    private fun formatPublishDate(publishedAt: String): String {
        return Instant
            .parse(publishedAt)
            .atOffset(ZoneOffset.UTC)
            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    }
}