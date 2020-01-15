package com.alex.newstime.feature.topheadlines

import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.core.util.isVisible
import com.alex.newstime.databinding.ControllerTopHeadlinesBinding
import com.alex.newstime.feature.base.BaseController
import com.alex.newstime.util.plusAssign
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.alex.newstime.feature.article.ArticleController
import com.alex.newstime.R
import com.alex.newstime.util.pushDetailController
import com.jakewharton.rxbinding3.swiperefreshlayout.refreshes
import com.jakewharton.rxbinding3.view.clicks

class TopHeadlinesController : BaseController<ControllerTopHeadlinesBinding>(R.layout.controller_top_headlines) {

    private val adapter by lazy { TopHeadlinesAdapter() }
    private val viewModel by lazy { getViewModel(TopHeadlinesViewModel::class.java) }

    private val bottomSheetDialog by lazy {
        BottomSheetDialog(context).apply { setContentView(bottomSheetDialogFavorites) }
    }

    private val bottomSheetDialogFavorites by lazy {
        activity!!.layoutInflater.inflate(R.layout.view_add_to_favorites, null)
    }

    // ----------------------------------------------------------------------------

    override fun onSetupView() {
        binding.recyclerView.also {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
        }
    }

    override fun onSetupViewBinding() {
        disposables += binding.swipeRefreshLayout.refreshes().subscribe {
            viewModel.onSwipeRefreshLayout()
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
        viewModel.recyclerLoadingState.observe {
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

        viewModel.init()
    }
}