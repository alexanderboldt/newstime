package com.alex.newstimes.feature.topheadlines

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.runner.AndroidJUnit4
import com.alex.newstime.feature.topheadlines.TopHeadlinesViewModel
import com.alex.newstime.feature.topheadlines.UiArticle
import com.alex.newstime.repository.news.Article
import com.alex.newstime.repository.news.NewsRepository
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class TopHeadlinesViewModelTest {

    @Rule @JvmField val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: TopHeadlinesViewModel

    @Mock lateinit var newsRepository: NewsRepository

    @Mock lateinit var observerRecyclerLoadingStateMock: Observer<Boolean>
    @Mock lateinit var observerRecyclerMessageStateMock: Observer<String>
    @Mock lateinit var observerRecyclerArticlesStateMock: Observer<List<UiArticle>>
    @Mock lateinit var observerDetailState: Observer<Article>

    // ----------------------------------------------------------------------------

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)

        viewModel = TopHeadlinesViewModel()
        viewModel.setNewsRepository(newsRepository)
        viewModel.recyclerLoadingSate.observeForever(observerRecyclerLoadingStateMock)
        viewModel.recyclerMessageState.observeForever(observerRecyclerMessageStateMock)
        viewModel.recyclerArticlesState.observeForever(observerRecyclerArticlesStateMock)
        viewModel.detailState.observeForever(observerDetailState)
    }

    @Test
    fun no_internet() {
        // mock the execution
        `when`(newsRepository.getTopHeadlines()).thenReturn(Single.create {
            it.onError(Throwable("No internet connection"))
        })

        // execute
        viewModel.getTopHeadlines()

        // verify
        verify(observerRecyclerLoadingStateMock, times(1)).onChanged(true)
        verify(observerRecyclerLoadingStateMock, times(1)).onChanged(false)
        verify(observerRecyclerMessageStateMock, times(1)).onChanged("Could not load articles")
        verify(observerRecyclerArticlesStateMock, never()).onChanged(any())
        verify(observerDetailState, never()).onChanged(any())
    }

    @Test
    fun empty_articles() {
        // mock the execution
        `when`(newsRepository.getTopHeadlines()).thenReturn(Single.create {
            it.onSuccess(listOf())
        })

        // execute
        viewModel.getTopHeadlines()

        // verify
        verify(observerRecyclerLoadingStateMock, times(1)).onChanged(true)
        verify(observerRecyclerLoadingStateMock, times(1)).onChanged(false)
        verify(observerRecyclerMessageStateMock, times(1)).onChanged("Articles not available")
        verify(observerRecyclerArticlesStateMock, never()).onChanged(any())
        verify(observerDetailState, never()).onChanged(any())
    }

    @Test
    fun valid_articles() {
        // mock the execution
        `when`(newsRepository.getTopHeadlines()).thenReturn(Single.create {
            val article = Article()
            article.title = "title"
            article.urlToImage = "www.image.com"

            it.onSuccess(listOf(article, article, article))
        })

        // execute
        viewModel.getTopHeadlines()

        // verify
        verify(observerRecyclerLoadingStateMock, times(1)).onChanged(true)
        verify(observerRecyclerLoadingStateMock, times(1)).onChanged(false)

        verify(observerRecyclerMessageStateMock, never()).onChanged(any())

        val article = UiArticle("title", "www.image.com")
        verify(observerRecyclerArticlesStateMock, times(1)).onChanged(listOf(article, article, article))

        verify(observerDetailState, never()).onChanged(any())
    }
}