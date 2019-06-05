package com.alex.newstime.feature.favorits

import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.newstime.R
import com.alex.newstime.databinding.ControllerFavoritsBinding
import com.alex.newstime.feature.article.ArticleController
import com.alex.newstime.feature.base.BaseController
import com.alex.newstime.repository.article.ArticleRepository
import com.alex.newstime.util.observe
import com.alex.newstime.util.plusAssign
import com.alex.newstime.util.pushDetailController

class FavoritsController : BaseController<ControllerFavoritsBinding>(R.layout.controller_favorits) {

    private lateinit var adapter: FavoritsAdapter
    private val viewModel by lazy { viewModelProvider().get(FavoritsViewModel::class.java) }

    // ----------------------------------------------------------------------------

    override fun onSetupView() {
        binding.lifecycleOwner = this

        adapter = FavoritsAdapter()

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        viewModel.setArticleRepository(ArticleRepository())
    }

    override fun onSetupViewBinding() {
        disposables += adapter.clickSubject.subscribe {
            viewModel.clickOnArticle(it)
        }
    }

    override fun onSetupViewModelBinding() {
        viewModel.recyclerArticlesState.observe(this) {
            adapter.setItems(it as ArrayList)
        }

        viewModel.detailState.observe(this) {
            router.pushDetailController(ArticleController.create(it))
        }

        viewModel.loadArticles()
    }
}