package com.alex.newstimes.feature.topheadlines

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alex.newstime.R
import com.alex.newstime.feature.base.ResourceProvider
import com.alex.newstime.feature.topheadlines.model.ArticleModel
import com.alex.newstime.feature.topheadlines.model.BaseModel
import com.alex.newstime.feature.topheadlines.model.LoadMoreModel
import com.alex.newstime.feature.topheadlines.TopHeadlinesViewModel
import com.alex.newstime.repository.article.RpModelArticle
import com.alex.newstime.repository.article.ArticleRepository
import io.reactivex.rxjava3.core.Single
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

    @Mock lateinit var articleRepositoryMock: ArticleRepository
    @Mock lateinit var resourceProviderMock: ResourceProvider

    @Mock lateinit var observerRecyclerLoadingStateMock: Observer<Boolean>
    @Mock lateinit var observerRecyclerMessageStateMock: Observer<String>
    @Mock lateinit var observerRecyclerArticlesStateMock: Observer<List<BaseModel>>
    @Mock lateinit var observerRecyclerLoadMoreStateMock: Observer<Boolean>
    @Mock lateinit var observerRecyclerScrollStateMock: Observer<Int>
    @Mock lateinit var observerMessageStateMock: Observer<String>
    @Mock lateinit var observerBottomSheetDialogStateMock: Observer<Boolean>
    @Mock lateinit var observerDetailState: Observer<RpModelArticle>

    // ----------------------------------------------------------------------------

    @Before
    fun before() {
        // define another dispatcher just for testing, so Android-class will not be used
        Dispatchers.setMain(mainThreadSurrogate)

        MockitoAnnotations.initMocks(this)

        viewModel = TopHeadlinesViewModel(articleRepositoryMock, resourceProviderMock).apply {
            recyclerLoadingState.observeForever(observerRecyclerLoadingStateMock)
            recyclerMessageState.observeForever(observerRecyclerMessageStateMock)
            recyclerArticlesState.observeForever(observerRecyclerArticlesStateMock)
            recyclerLoadMoreState.observeForever(observerRecyclerLoadMoreStateMock)
            recyclerScrollState.observeForever(observerRecyclerScrollStateMock)
            messageState.observeForever(observerMessageStateMock)
            bottomSheetDialogState.observeForever(observerBottomSheetDialogStateMock)
            detailState.observeForever(observerDetailState)
        }

        `when`(resourceProviderMock.getString(R.string.top_headlines_error_load_articles)).thenReturn("Could not load articles!")
    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }

    // ----------------------------------------------------------------------------

    @Test
    fun init_with_no_internet() {
        // mock
        `when`(articleRepositoryMock.getTopHeadlines()).thenReturn(Single.error(Throwable("No internet connection")))

        // execute
        viewModel.init()

        // verify
        verify(observerRecyclerLoadingStateMock, times(1)).onChanged(true)
        verify(observerRecyclerLoadingStateMock, times(1)).onChanged(false)

        verify(observerRecyclerMessageStateMock, times(1)).onChanged("Could not load articles!")
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
        `when`(articleRepositoryMock.getTopHeadlines()).thenReturn(Single.just(listOf()))

        // execute
        viewModel.init()

        // verify
        verify(observerRecyclerLoadingStateMock, times(1)).onChanged(true)
        verify(observerRecyclerLoadingStateMock, times(1)).onChanged(false)

        verify(observerRecyclerMessageStateMock, times(1)).onChanged("No articles available!")
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
            RpModelArticle().apply {
                id = 1
                title = "title"
                urlToImage = "www.image.com"
            },
            RpModelArticle().apply {
                id = 2
                title = "title"
                urlToImage = "www.image.com"
            },
            RpModelArticle().apply {
                id = 3
                title = "title"
                urlToImage = "www.image.com"
            })

        // mock
        `when`(articleRepositoryMock.getTopHeadlines()).thenReturn(Single.just(articles))

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
            LoadMoreModel(true)
        )

        verify(observerRecyclerArticlesStateMock, times(1)).onChanged(uiArticles)
        verify(observerRecyclerLoadMoreStateMock, never()).onChanged(any())
        verify(observerRecyclerScrollStateMock, never()).onChanged(any())
        verify(observerMessageStateMock, never()).onChanged(any())
        verify(observerBottomSheetDialogStateMock, never()).onChanged(any())
        verify(observerDetailState, never()).onChanged(any())
    }
}