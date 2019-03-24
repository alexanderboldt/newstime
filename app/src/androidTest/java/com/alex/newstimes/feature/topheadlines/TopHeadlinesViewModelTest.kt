package com.alex.newstimes.feature.topheadlines

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import com.alex.newstime.feature.topheadlines.TopHeadlinesViewModel
import com.alex.newstime.repository.news.NewsRepository
import com.alex.newstime.util.observe
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock

class TopHeadlinesViewModelTest {

    @Test
    fun test() {
        val d = mock(LifecycleOwner::class.java)
        val lifecycle = LifecycleOwner {
            mock(Lifecycle::class.java)
        } //mock(LifecycleOwner::class.java))
        val viewModel = TopHeadlinesViewModel()
        val newsRepository = mock(NewsRepository::class.java)

        viewModel.dataState.observe(d) { data ->
            assertEquals(false, true)
        }

        viewModel.setNewsRepository(newsRepository)
        viewModel.getTopHeadlines()
    }
}