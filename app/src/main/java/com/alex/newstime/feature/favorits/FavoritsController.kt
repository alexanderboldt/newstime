package com.alex.newstime.feature.favorits

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.newstime.databinding.ControllerFavoritsBinding
import com.alex.newstime.feature.article.ArticleController
import com.alex.newstime.feature.base.BaseController
import com.alex.newstime.feature.favorits.adapter.FavoritsAdapter
import com.alex.newstime.util.pushDetailController

class FavoritsController : BaseController<ControllerFavoritsBinding>() {

    private val adapter by lazy { FavoritsAdapter(this, viewModel) }
    private val viewModel by lazy { getViewModel(FavoritsViewModel::class.java) }

    // ----------------------------------------------------------------------------

    override fun onCreateBinding(inflater: LayoutInflater, container: ViewGroup): ControllerFavoritsBinding {
        return ControllerFavoritsBinding.inflate(inflater, container, false)
    }

    override fun onSetupView() {
        binding.recyclerView.also {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
            it.updatePadding(top = getStatusBarHeight())
        }
    }

    override fun onViewModelBinding() {
        viewModel.detailState.observeNotNull {
            router.pushDetailController(ArticleController.create(it))
        }
    }

    // ----------------------------------------------------------------------------

    override fun onLifecycleResume() {
        super.onLifecycleResume()
        viewModel.loadArticles()
    }
}