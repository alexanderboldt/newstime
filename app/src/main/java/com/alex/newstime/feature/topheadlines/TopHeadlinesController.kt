package com.alex.newstime.feature.topheadlines

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.core.util.isVisible
import com.alex.newstime.databinding.ControllerTopHeadlinesBinding
import com.alex.newstime.feature.base.BaseController
import com.alex.newstime.util.plusAssign
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jakewharton.rxbinding2.support.v4.widget.refreshes
import com.jakewharton.rxbinding2.view.clicks
import com.alex.newstime.feature.article.ArticleController
import com.alex.newstime.R
import com.alex.newstime.util.pushDetailController

class TopHeadlinesController : BaseController<ControllerTopHeadlinesBinding>(R.layout.controller_top_headlines) {

    private lateinit var adapter: TopHeadlinesAdapter
    private val viewModel by lazy { viewModelProvider().get(TopHeadlinesViewModel::class.java) }

    private var fabMenuExpanded = false

    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var bottomSheetDialogFavorites: View

    // ----------------------------------------------------------------------------

    override fun onSetupView() {
        binding.lifecycleOwner = this

        adapter = TopHeadlinesAdapter()

        binding.recyclerView.also {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
        }

        bottomSheetDialogFavorites = activity!!.layoutInflater.inflate(R.layout.view_add_to_favorites, null)
        bottomSheetDialog = BottomSheetDialog(context).apply {
            setContentView(bottomSheetDialogFavorites)
        }
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

        disposables += adapter.longClickSubject.subscribe {
            viewModel.longClickArticle(it)
        }

        disposables += bottomSheetDialogFavorites.clicks().subscribe {
            viewModel.clickAddToFavorites()
        }
    }

    override fun onSetupViewModelBinding() {
        viewModel.recyclerLoadingSate.observe {
            binding.swipeRefreshLayout.isRefreshing = it
        }

        viewModel.recyclerMessageState.observe {
            binding.textViewMessage.apply {
                text = it
                isVisible = true
            }

            binding.recyclerView.isVisible = false
        }

        viewModel.recyclerArticlesState.observe { data ->
            binding.textViewMessage.isVisible = false
            binding.recyclerView.isVisible = true

            adapter.setItems(data as ArrayList)
        }

        viewModel.detailState.observe {
            router.pushDetailController(ArticleController.create(it))
        }

        viewModel.recyclerLoadMoreState.observe {
            adapter.enableLoadMore(it)
        }

        viewModel.recyclerScrollState.observe {
            binding.recyclerView.smoothScrollToPosition(it)
        }

        viewModel.messageState.observe {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.bottomSheetDialogState.observe { state ->
            if (state) bottomSheetDialog.show() else bottomSheetDialog.hide()
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