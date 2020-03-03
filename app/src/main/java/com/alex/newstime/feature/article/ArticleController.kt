package com.alex.newstime.feature.article

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import com.alex.newstime.R
import com.alex.newstime.databinding.ControllerArticleBinding
import com.alex.newstime.feature.base.BaseController
import com.alex.newstime.repository.article.Article
import com.alex.newstime.util.plusAssign
import com.jakewharton.rxbinding3.view.clicks
import org.parceler.Parcels

class ArticleController(private var bundle: Bundle) : BaseController<ControllerArticleBinding>(R.layout.controller_article) {

    private val viewModel by lazy { getViewModel(ArticleViewModel::class.java) }

    // ----------------------------------------------------------------------------

    companion object {
        private const val KEY_ARTICLE = "KEY_ARTICLE"

        fun create(article: Article) = ArticleController(bundleOf(KEY_ARTICLE to Parcels.wrap(article)))
    }

    // ----------------------------------------------------------------------------

    override fun onSetupView() {
        viewModel.init(Parcels.unwrap(bundle.getParcelable(KEY_ARTICLE)))
    }

    override fun onSetupViewBinding() {
        disposables += binding.textViewTitle.clicks().subscribe {
            viewModel.handleClickOnLink()
        }

        disposables += binding.imageViewBack.clicks().subscribe {
            viewModel.handleClickBack()
        }
    }

    override fun onSetupViewModelBinding() {
        viewModel.dataState.observeNotNull { article ->
            binding.article = article
        }

        viewModel.linkState.observeNotNull {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
        }

        viewModel.closeState.observe(this, Observer {
            router.handleBack()
        })
    }
}