package com.alex.newstime.feature.topheadlines

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.alex.newstime.R
import com.alex.newstime.databinding.FragmentTopHeadlinesBinding
import com.alex.newstime.feature.article.ArticleFragment
import com.alex.newstime.feature.base.BaseFragment
import com.alex.newstime.feature.topheadlines.adapter.TopHeadlinesAdapter
import com.alex.newstime.util.plusAssign
import com.jakewharton.rxbinding4.swiperefreshlayout.refreshes
import com.jakewharton.rxbinding4.view.clicks
import org.koin.android.ext.android.inject

class TopHeadlinesFragment : BaseFragment<FragmentTopHeadlinesBinding>() {

    private val viewModel: TopHeadlinesViewModel by inject()

    private val adapter by lazy { TopHeadlinesAdapter(this, viewModel) }

    private val bottomSheetDialog by lazy {
        BottomSheetDialog(requireContext()).apply { setContentView(bottomSheetDialogFavorites) }
    }

    private val bottomSheetDialogFavorites by lazy {
        layoutInflater.inflate(R.layout.view_add_to_favorites, null)
    }

    // ----------------------------------------------------------------------------

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): FragmentTopHeadlinesBinding {
        return FragmentTopHeadlinesBinding.inflate(inflater, container, false)
    }

    override fun setupView() {
        binding.recyclerView.also {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
            it.updatePadding(top = getStatusBarHeight())
        }
    }

    override fun bindView() {
        disposables += binding.swipeRefreshLayout.refreshes().subscribe {
            viewModel.onSwipeRefreshLayout()
        }

        disposables += bottomSheetDialogFavorites.clicks().subscribe {
            viewModel.clickAddToFavorites()
        }
    }

    override fun bindViewModel() {
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
        }

        viewModel.detailState.observe { article ->
            findNavController().navigate(R.id.articleFragment, ArticleFragment.bundle(article))
        }

        viewModel.recyclerScrollState.observe {
            binding.recyclerView.smoothScrollToPosition(it)
        }

        viewModel.messageState.observe {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.bottomSheetDialogState.observe { state ->
            bottomSheetDialog.apply { if (state) show() else hide() }
        }

        viewModel.init()
    }
}