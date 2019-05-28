package com.alex.newstime.feature.article

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.alex.newstime.R
import com.alex.newstime.databinding.ControllerArticleBinding
import com.alex.newstime.feature.base.BaseController
import com.alex.newstime.repository.article.Article
import com.alex.newstime.util.observe
import com.alex.newstime.util.plusAssign
import com.jakewharton.rxbinding2.view.clicks
import org.parceler.Parcels

class ArticleController(private var bundle: Bundle) : BaseController<ControllerArticleBinding>(R.layout.controller_article) {

    private val viewModel by lazy { viewModelProvider().get(ArticleViewModel::class.java) }

    // ----------------------------------------------------------------------------

    companion object {
        private const val KEY_ARTICLE = "KEY_ARTICLE"

        fun create(article: Article): ArticleController {
            val bundle = Bundle().apply {
                putParcelable(KEY_ARTICLE, Parcels.wrap(article))
            }

            return ArticleController(bundle)
        }
    }

    // ----------------------------------------------------------------------------

    override fun onSetupView() {
        viewModel.init(Parcels.unwrap<Article>(bundle.getParcelable(KEY_ARTICLE)))
    }

    override fun onSetupViewBinding() {
        disposables += binding.textViewTitle.clicks().subscribe {
            viewModel.handleLinkClick()
        }
    }

    override fun onSetupViewModelBinding() {
        viewModel.dataState.observe(this) { article ->
            binding.article = article
        }

        viewModel.linkState.observe(this) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
        }
    }
}