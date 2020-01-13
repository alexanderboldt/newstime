package com.alex.newstime.feature.favorits

import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.newstime.R
import com.alex.newstime.databinding.ControllerFavoritsBinding
import com.alex.newstime.feature.article.ArticleController
import com.alex.newstime.feature.base.BaseController
import com.alex.newstime.util.plusAssign
import com.alex.newstime.util.pushDetailController

class FavoritsController : BaseController<ControllerFavoritsBinding>(R.layout.controller_favorits) {

    private val adapter by lazy { FavoritsAdapter() }
    private val viewModel by lazy { getViewModel(FavoritsViewModel::class.java) }

    // ----------------------------------------------------------------------------

    override fun onSetupView() {
        binding.lifecycleOwner = this
        binding.recyclerView.also {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
        }
    }

    override fun onSetupViewBinding() {
        disposables += adapter.clickSubject.subscribe {
            viewModel.clickOnArticle(it)
        }
    }

    override fun onSetupViewModelBinding() {
        viewModel.recyclerArticlesState.observe {
            adapter.setItems(it as ArrayList)
        }

        viewModel.detailState.observe {
            router.pushDetailController(ArticleController.create(it))
        }

        viewModel.loadArticles()
    }
}