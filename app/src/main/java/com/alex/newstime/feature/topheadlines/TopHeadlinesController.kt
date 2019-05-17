package com.alex.newstime.feature.topheadlines

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.newstime.R
import com.alex.newstime.databinding.ControllerTopHeadlinesBinding
import com.alex.newstime.feature.base.BaseController
import com.alex.newstime.feature.article.ArticleController
import com.alex.newstime.repository.news.NewsRepository
import com.alex.newstime.util.observe
import com.alex.newstime.util.plusAssign
import com.alex.newstime.util.pushDetailController
import com.jakewharton.rxbinding2.support.v4.widget.refreshes
import com.jakewharton.rxbinding2.view.clicks

class TopHeadlinesController : BaseController<ControllerTopHeadlinesBinding>(R.layout.controller_top_headlines) {

    private var adapter = TopHeadlinesAdapter()
    private val viewModel by lazy { viewModelProvider().get(TopHeadlinesViewModel::class.java) }

    private var fabMenuExpanded = false

    // ----------------------------------------------------------------------------

    override fun onSetupView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        viewModel.setNewsRepository(NewsRepository())
    }

    override fun onSetupViewBinding() {
        disposables += binding.swipeRefreshLayout.refreshes().subscribe {
            viewModel.refreshArticles()
        }

        disposables += binding.fabGermany.clicks().subscribe {
            viewModel.refreshArticles(TopHeadlinesViewModel.Types.GERMANY)
            fabMenuShow(false)
        }

        disposables += binding.fabWorldWide.clicks().subscribe {
            viewModel.refreshArticles(TopHeadlinesViewModel.Types.WORLD_WIDE)
            fabMenuShow(false)
        }

        disposables += binding.fabMenu.clicks().subscribe {
            fabMenuExpanded = !fabMenuExpanded
            fabMenuShow(fabMenuExpanded)
        }

        disposables += adapter.clickSubject.subscribe {
            when (it) {
                is ArticleModel -> viewModel.clickOnArticle(it)
                is LoadMoreModel -> viewModel.loadMoreArticles()
            }
        }
    }

    override fun onSetupViewModelBinding() {
        viewModel.recyclerLoadingSate.observe(this) {
            binding.swipeRefreshLayout.isRefreshing = it
        }

        viewModel.recyclerMessageState.observe(this) {
            binding.textViewMessage.text = it
            binding.textViewMessage.visibility = View.VISIBLE

            binding.recyclerView.visibility = View.GONE
        }

        viewModel.recyclerArticlesState.observe(this) { data ->
            binding.textViewMessage.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE

            adapter.setItems(data as ArrayList)
        }

        viewModel.detailState.observe(this) {
            router.pushDetailController(ArticleController.create(it))
        }

        viewModel.recyclerLoadMoreState.observe(this) {
            adapter.enableLoadMore(it)
        }

        viewModel.recyclerScrollState.observe(this) {
            binding.recyclerView.smoothScrollToPosition(it)
        }

        viewModel.loadInitArticles()
    }

    // ----------------------------------------------------------------------------

    private fun fabMenuShow(show: Boolean) {
        if (show) {
            binding.fabGermany.show()
            binding.fabWorldWide.show()
        } else {
            binding.fabGermany.hide()
            binding.fabWorldWide.hide()
        }
    }
}