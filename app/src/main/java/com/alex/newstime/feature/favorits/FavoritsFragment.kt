package com.alex.newstime.feature.favorits

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.newstime.databinding.FragmentFavoritsBinding
import com.alex.newstime.feature.base.BaseFragment
import com.alex.newstime.feature.favorits.adapter.FavoritsAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritsFragment : BaseFragment<FragmentFavoritsBinding>() {

    private val viewModel: FavoritsViewModel by viewModel()

    private val adapter by lazy { FavoritsAdapter(this, viewModel) }

    // ----------------------------------------------------------------------------

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): FragmentFavoritsBinding {
        return FragmentFavoritsBinding.inflate(inflater, container, false)
    }

    override fun setupView() {
        binding.recyclerView.also {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
            it.updatePadding(top = getStatusBarHeight())
        }
    }

    override fun bindViewModel() {
        viewModel.detailState.observe { article ->
            FavoritsFragmentDirections
                .actionToArticleFragment(article)
                .also { directions -> findNavController().navigate(directions) }
        }
    }

    // ----------------------------------------------------------------------------

    override fun onLifecycleResume() {
        super.onLifecycleResume()
        viewModel.loadArticles()
    }
}