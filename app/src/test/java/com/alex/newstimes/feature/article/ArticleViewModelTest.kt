package com.alex.newstimes.feature.article

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alex.newstime.feature.article.ArticleModel
import com.alex.newstime.feature.article.ArticleViewModel
import com.alex.newstime.repository.article.Article
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.lang.Exception

@RunWith(JUnit4::class)
class ArticleViewModelTest {

    @Rule @JvmField val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: ArticleViewModel

    @Mock lateinit var observerDataStateMock: Observer<ArticleModel>
    @Mock lateinit var observerLinkStateMock: Observer<String>
    @Mock lateinit var observerCloseStateMock: Observer<Unit>

    // ----------------------------------------------------------------------------

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)

        viewModel = ArticleViewModel().apply {
            dataState.observeForever(observerDataStateMock)
            linkState.observeForever(observerLinkStateMock)
            closeState.observeForever(observerCloseStateMock)
        }
    }

    @Test
    fun it_should_display_the_article() {
        // prepare
        val article = Article().apply {
            id = 387428
            title = "Test DbArticle"
            content = "Content"
            urlToImage = "url to image"
            url = "url to article"
        }

        // execute
        viewModel.init(article)

        // verify
        val uiArticle = ArticleModel(387428, "Test DbArticle", "url to image", "Content")

        verify(observerDataStateMock, times(1)).onChanged(uiArticle)
        verify(observerLinkStateMock, never()).onChanged(any())
        verify(observerCloseStateMock, never()).onChanged(Unit)
    }

    @Test
    fun it_should_crash_in_viewModel_due_missing_initialization() {
        // execute
        var exception: Exception? = null
        try {
            viewModel.handleClickOnLink()
        } catch (_exception: Exception) {
            exception = _exception
        } finally {
            assertEquals(exception != null, true)
        }

        // verify
        val uiArticle = ArticleModel(387428, "Test DbArticle", "url to image", "Content")

        verify(observerDataStateMock, never()).onChanged(uiArticle)
        verify(observerLinkStateMock, never()).onChanged(any())
        verify(observerCloseStateMock, never()).onChanged(Unit)
    }

    @Test
    fun it_should_open_the_article_in_browser() {
        // prepare
        val article = Article().apply {
            id = 387428
            title = "Test DbArticle"
            content = "Content"
            urlToImage = "url to image"
            url = "url to article"
        }

        // execute
        viewModel.init(article)
        viewModel.handleClickOnLink()

        // verify
        val uiArticle = ArticleModel(387428, "Test DbArticle", "url to image", "Content")

        verify(observerDataStateMock, times(1)).onChanged(uiArticle)
        verify(observerLinkStateMock, times(1)).onChanged("url to article")
        verify(observerCloseStateMock, never()).onChanged(Unit)
    }

    @Test
    fun it_should_go_back() {
        // execute
        viewModel.handleClickBack()

        // verify
        verify(observerDataStateMock, never()).onChanged(any())
        verify(observerLinkStateMock, never()).onChanged(any())
        verify(observerCloseStateMock, times(1)).onChanged(Unit)
    }
}