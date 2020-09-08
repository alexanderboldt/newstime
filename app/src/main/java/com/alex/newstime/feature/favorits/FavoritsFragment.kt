package com.alex.newstime.feature.favorits

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.newstime.R
import com.alex.newstime.databinding.FragmentFavoritsBinding
import com.alex.newstime.feature.article.ArticleFragment
import com.alex.newstime.feature.base.BaseFragment
import com.alex.newstime.feature.favorits.adapter.FavoritsAdapter
import org.koin.android.ext.android.inject

class FavoritsFragment : BaseFragment<FragmentFavoritsBinding>() {

    private val viewModel: FavoritsViewModel by inject()

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
            activity?.supportFragmentManager?.commit {
                add(R.id.frameLayout_fragments, ArticleFragment.create(article))
                addToBackStack(null)
            }
        }
    }

    // ----------------------------------------------------------------------------

    override fun onLifecycleResume() {
        super.onLifecycleResume()
        viewModel.loadArticles()
    }
}