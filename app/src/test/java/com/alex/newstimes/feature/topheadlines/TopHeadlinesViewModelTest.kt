package com.alex.newstimes.feature.topheadlines

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alex.newstime.feature.topheadlines.ArticleModel
import com.alex.newstime.feature.topheadlines.BaseModel
import com.alex.newstime.feature.topheadlines.TopHeadlinesViewModel
import com.alex.newstime.repository.article.Article
import com.alex.newstime.repository.article.ArticleRepository
import io.reactivex.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class TopHeadlinesViewModelTest {

    @Rule @JvmField val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: TopHeadlinesViewModel

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Mock lateinit var articleRepository: ArticleRepository

    @Mock lateinit var observerRecyclerLoadingStateMock: Observer<Boolean>
    @Mock lateinit var observerRecyclerMessageStateMock: Observer<String>
    @Mock lateinit var observerRecyclerArticlesStateMock: Observer<List<BaseModel>>
    @Mock lateinit var observerDetailState: Observer<Article>

    // ----------------------------------------------------------------------------

    @Before
    fun before() {
        // define another dispatcher just for testing, so Android-class will not be used
        Dispatchers.setMain(mainThreadSurrogate)

        MockitoAnnotations.initMocks(this)

        viewModel = TopHeadlinesViewModel()
        viewModel.articleRepository = articleRepository
        viewModel.recyclerLoadingSate.observeForever(observerRecyclerLoadingStateMock)
        viewModel.recyclerMessageState.observeForever(observerRecyclerMessageStateMock)
        viewModel.recyclerArticlesState.observeForever(observerRecyclerArticlesStateMock)
        viewModel.detailState.observeForever(observerDetailState)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }

    // ----------------------------------------------------------------------------

    @Test
    fun no_internet() {

        runBlockingTest {
            // mock the execution
            `when`(articleRepository.getTopHeadlines()).thenReturn(Single.create {
                it.onError(Throwable("No internet connection"))
            })
            // execute
            viewModel.loadInitArticles()
            // verify
            //verify(observerRecyclerLoadingStateMock, times(1)).onChanged(true)
            //verify(observerRecyclerLoadingStateMock, times(1)).onChanged(false)
            verify(observerRecyclerMessageStateMock, times(1)).onChanged("Could not load articles")
            verify(observerRecyclerArticlesStateMock, never()).onChanged(any())
            verify(observerDetailState, never()).onChanged(any())
        }


    }

    @Test
    fun empty_articles() {
        // mock the execution
        `when`(articleRepository.getTopHeadlines()).thenReturn(Single.create {
            it.onSuccess(listOf())
        })

        // execute
        viewModel.loadInitArticles()

        // verify
        //verify(observerRecyclerLoadingStateMock, times(1)).onChanged(true)
        //verify(observerRecyclerLoadingStateMock, times(1)).onChanged(false)
        verify(observerRecyclerMessageStateMock, times(1)).onChanged("Articles not available")
        verify(observerRecyclerArticlesStateMock, never()).onChanged(any())
        verify(observerDetailState, never()).onChanged(any())
    }

    @Test
    fun valid_articles() {
        // mock the execution
        `when`(articleRepository.getTopHeadlines()).thenReturn(Single.create {
            val article = Article()
            article.title = "title"
            article.urlToImage = "www.image.com"

            it.onSuccess(listOf(article, article, article))
        })

        // execute
        viewModel.loadInitArticles()

        // verify
        verify(observerRecyclerLoadingStateMock, times(1)).onChanged(true)
        verify(observerRecyclerLoadingStateMock, times(1)).onChanged(false)

        verify(observerRecyclerMessageStateMock, never()).onChanged(any())

        val article = ArticleModel(322342, "title", "www.image.com") as BaseModel
        verify(observerRecyclerArticlesStateMock, times(1)).onChanged(listOf(article, article, article))

        verify(observerDetailState, never()).onChanged(any())
    }
}