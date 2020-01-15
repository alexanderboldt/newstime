package com.alex.newstimes.feature.topheadlines

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alex.newstime.feature.topheadlines.ArticleModel
import com.alex.newstime.feature.topheadlines.BaseModel
import com.alex.newstime.feature.topheadlines.LoadMoreModel
import com.alex.newstime.feature.topheadlines.TopHeadlinesViewModel
import com.alex.newstime.repository.article.Article
import com.alex.newstime.repository.article.ArticleRepository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
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
    @Mock lateinit var observerRecyclerLoadMoreStateMock: Observer<Boolean>
    @Mock lateinit var observerRecyclerScrollStateMock: Observer<Int>
    @Mock lateinit var observerMessageStateMock: Observer<String>
    @Mock lateinit var observerBottomSheetDialogStateMock: Observer<Boolean>
    @Mock lateinit var observerDetailState: Observer<Article>

    // ----------------------------------------------------------------------------

    @Before
    fun before() {
        // define another dispatcher just for testing, so Android-class will not be used
        Dispatchers.setMain(mainThreadSurrogate)

        MockitoAnnotations.initMocks(this)

        viewModel = TopHeadlinesViewModel()
        // this is a fallback
        // best way to use mocked repositories, is to do it with dagger
        viewModel.articleRepository = articleRepository
        viewModel.recyclerLoadingState.observeForever(observerRecyclerLoadingStateMock)
        viewModel.recyclerMessageState.observeForever(observerRecyclerMessageStateMock)
        viewModel.recyclerArticlesState.observeForever(observerRecyclerArticlesStateMock)
        viewModel.recyclerLoadMoreState.observeForever(observerRecyclerLoadMoreStateMock)
        viewModel.recyclerScrollState.observeForever(observerRecyclerScrollStateMock)
        viewModel.messageState.observeForever(observerMessageStateMock)
        viewModel.bottomSheetDialogState.observeForever(observerBottomSheetDialogStateMock)
        viewModel.detailState.observeForever(observerDetailState)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }

    // ----------------------------------------------------------------------------

    @Test
    fun init_with_no_internet() {
        // mock
        `when`(articleRepository.getTopHeadlines()).thenReturn(Single.create {
            it.onError(Throwable("No internet connection"))
        })

        // execute
        viewModel.init()

        // verify
        verify(observerRecyclerLoadingStateMock, times(1)).onChanged(true)
        verify(observerRecyclerLoadingStateMock, times(1)).onChanged(false)

        verify(observerRecyclerMessageStateMock, times(1)).onChanged("Could not load articles")
        verify(observerRecyclerArticlesStateMock, never()).onChanged(any())
        verify(observerRecyclerLoadMoreStateMock, never()).onChanged(any())
        verify(observerRecyclerScrollStateMock, never()).onChanged(any())
        verify(observerMessageStateMock, never()).onChanged(any())
        verify(observerBottomSheetDialogStateMock, never()).onChanged(any())
        verify(observerDetailState, never()).onChanged(any())
    }

    @Test
    fun init_with_empty_articles() {
        // mock
        `when`(articleRepository.getTopHeadlines()).thenReturn(Single.create {
            it.onSuccess(listOf())
        })

        // execute
        viewModel.init()

        // verify
        verify(observerRecyclerLoadingStateMock, times(1)).onChanged(true)
        verify(observerRecyclerLoadingStateMock, times(1)).onChanged(false)

        verify(observerRecyclerMessageStateMock, times(1)).onChanged("Articles not available")
        verify(observerRecyclerArticlesStateMock, never()).onChanged(any())
        verify(observerRecyclerLoadMoreStateMock, never()).onChanged(any())
        verify(observerRecyclerScrollStateMock, never()).onChanged(any())
        verify(observerMessageStateMock, never()).onChanged(any())
        verify(observerBottomSheetDialogStateMock, never()).onChanged(any())
        verify(observerDetailState, never()).onChanged(any())
    }

    @Test
    fun init_with_valid_articles() {
        // prepare
        val articles = listOf(
            Article().apply {
                id = 1
                title = "title"
                urlToImage = "www.image.com"
            },
            Article().apply {
                id = 2
                title = "title"
                urlToImage = "www.image.com"
            },
            Article().apply {
                id = 3
                title = "title"
                urlToImage = "www.image.com"
            })

        // mock
        `when`(articleRepository.getTopHeadlines()).thenReturn(Single.create { it.onSuccess(articles) })

        // execute
        viewModel.init()

        // verify
        verify(observerRecyclerLoadingStateMock, times(1)).onChanged(true)
        verify(observerRecyclerLoadingStateMock, times(1)).onChanged(false)

        verify(observerRecyclerMessageStateMock, never()).onChanged(any())

        val uiArticles = listOf(
            ArticleModel(1, "title", "www.image.com"),
            ArticleModel(2, "title", "www.image.com"),
            ArticleModel(3, "title", "www.image.com"),
            LoadMoreModel(true))

        verify(observerRecyclerArticlesStateMock, times(1)).onChanged(uiArticles)
        verify(observerRecyclerLoadMoreStateMock, never()).onChanged(any())
        verify(observerRecyclerScrollStateMock, never()).onChanged(any())
        verify(observerMessageStateMock, never()).onChanged(any())
        verify(observerBottomSheetDialogStateMock, never()).onChanged(any())
        verify(observerDetailState, never()).onChanged(any())
    }
}