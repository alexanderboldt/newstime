package com.alex.newstimes.feature.article

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.runner.AndroidJUnit4
import com.alex.newstime.feature.article.ArticleViewModel
import com.alex.newstime.feature.article.UiArticle
import com.alex.newstime.repository.article.Article
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.lang.Exception

@RunWith(AndroidJUnit4::class)
class ArticleViewModelTest {

    @Rule @JvmField val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: ArticleViewModel

    @Mock lateinit var observerDataStateMock: Observer<UiArticle>
    @Mock lateinit var observerLinkState: Observer<String>

    // ----------------------------------------------------------------------------

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)

        viewModel = ArticleViewModel()
        viewModel.dataState.observeForever(observerDataStateMock)
        viewModel.linkState.observeForever(observerLinkState)
    }

    @Test
    fun it_should_display_the_article() {
        // prepare
        val article = Article().apply {
            id = 387428
            title = "Test Article"
            content = "Content"
            urlToImage = "url to image"
            url = "url to article"
        }

        // execute
        viewModel.init(article)

        // verify
        val uiArticle = UiArticle(387428, "Test Article", "url to image", "Content")

        verify(observerDataStateMock, times(1)).onChanged(uiArticle)
        verify(observerLinkState, never()).onChanged(any())
    }

    @Test
    fun it_should_crash_in_viewModel_du_missing_initialization() {
        // execute
        var exception: Exception? = null
        try {
            viewModel.handleLinkClick()
        } catch (_exception: Exception) {
            exception = _exception
        } finally {
            assertEquals(exception != null, true)
        }

        // verify
        val uiArticle = UiArticle(387428, "Test Article", "url to image", "Content")

        verify(observerDataStateMock, never()).onChanged(uiArticle)
        verify(observerLinkState, never()).onChanged(any())
    }

    @Test
    fun it_should_open_the_article_in_browser() {
        // prepare
        val article = Article().apply {
            id = 387428
            title = "Test Article"
            content = "Content"
            urlToImage = "url to image"
            url = "url to article"
        }

        // execute
        viewModel.init(article)
        viewModel.handleLinkClick()

        // verify
        val uiArticle = UiArticle(387428, "Test Article", "url to image", "Content")

        verify(observerDataStateMock, times(1)).onChanged(uiArticle)
        verify(observerLinkState, times(1)).onChanged("url to article")
    }
}