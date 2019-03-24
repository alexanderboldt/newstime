package com.alex.newstime.feature.topheadlines

import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alex.newstime.databinding.ControllerTopHeadlinesBinding
import com.alex.newstime.feature.AbstractController
import com.alex.newstime.feature.article.ArticleController
import com.alex.newstime.repository.news.NewsRepository
import com.alex.newstime.util.observe
import com.alex.newstime.util.plusAssign
import com.alex.newstime.util.pushDetailController
import com.jakewharton.rxbinding2.support.v4.widget.refreshes

class TopHeadlinesController : AbstractController() {

    private lateinit var binding: ControllerTopHeadlinesBinding
    private var adapter = TopHeadlinesAdapter()
    private val viewModel by lazy { viewModelProvider().get(TopHeadlinesViewModel::class.java) }

    // ----------------------------------------------------------------------------

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        binding = ControllerTopHeadlinesBinding.inflate(inflater, container, false)

        setupView()

        return binding.root
    }

    override fun onAttach(view: View) {
        setupViewBinding()
        setupViewModelBinding()
    }

    // ----------------------------------------------------------------------------

    private fun setupView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        viewModel.setNewsRepository(NewsRepository())
    }

    private fun setupViewBinding() {
        disposables += binding.swipeRefreshLayout.refreshes().subscribe {
            viewModel.getTopHeadlines()
        }

        disposables += adapter.clickSubject.subscribe {
            viewModel.clickOnArticle(it)
        }
    }

    private fun setupViewModelBinding() {
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

            adapter.setItems(data)
        }

        viewModel.detailState.observe(this) {
            router.pushDetailController(ArticleController.create(it))
        }

        viewModel.getTopHeadlines()
    }
}