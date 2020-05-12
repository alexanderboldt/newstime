package com.alex.newstime.feature.topheadlines

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.newstime.databinding.ControllerTopHeadlinesBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.alex.newstime.feature.article.ArticleController
import com.alex.newstime.R
import com.alex.newstime.feature.base.BaseController
import com.alex.newstime.feature.topheadlines.adapter.TopHeadlinesAdapter
import com.alex.newstime.util.plusAssign
import com.alex.newstime.util.pushDetailController
import com.jakewharton.rxbinding3.swiperefreshlayout.refreshes
import com.jakewharton.rxbinding3.view.clicks

class TopHeadlinesController : BaseController<ControllerTopHeadlinesBinding>() {

    private val adapter by lazy { TopHeadlinesAdapter(this, viewModel) }
    private val viewModel by lazy { getViewModel(TopHeadlinesViewModel::class.java) }

    private val bottomSheetDialog by lazy {
        BottomSheetDialog(context).apply { setContentView(bottomSheetDialogFavorites) }
    }

    private val bottomSheetDialogFavorites by lazy {
        activity!!.layoutInflater.inflate(R.layout.view_add_to_favorites, null)
    }

    // ----------------------------------------------------------------------------

    override fun onCreateBinding(inflater: LayoutInflater, container: ViewGroup): ControllerTopHeadlinesBinding {
        return ControllerTopHeadlinesBinding.inflate(inflater, container, false)
    }

    override fun onSetupView() {
        binding.recyclerView.also {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
            it.updatePadding(top = getStatusBarHeight())
        }
    }

    override fun onViewBinding() {
        disposables += binding.swipeRefreshLayout.refreshes().subscribe {
            viewModel.onSwipeRefreshLayout()
        }

        disposables += bottomSheetDialogFavorites.clicks().subscribe {
            viewModel.clickAddToFavorites()
        }
    }

    override fun onViewModelBinding() {
        viewModel.recyclerLoadingState.observeNotNull {
            binding.swipeRefreshLayout.isRefreshing = it
        }

        viewModel.recyclerMessageState.observeNotNull {
            binding.textViewMessage.apply {
                text = it
                isVisible = true
            }

            binding.recyclerView.isVisible = false
        }

        viewModel.recyclerArticlesState.observeNotNull { data ->
            binding.textViewMessage.isVisible = false
            binding.recyclerView.isVisible = true
        }

        viewModel.detailState.observeNotNull {
            router.pushDetailController(ArticleController.create(it))
        }

        viewModel.recyclerScrollState.observeNotNull {
            binding.recyclerView.smoothScrollToPosition(it)
        }

        viewModel.messageState.observeNotNull {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.bottomSheetDialogState.observeNotNull { state ->
            bottomSheetDialog.apply { if (state) show() else hide() }
        }
    }

    // ----------------------------------------------------------------------------

    override fun onLifecycleResume() {
        super.onLifecycleResume()
        viewModel.init()
    }
}