package com.alex.newstime.feature.article

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Observer
import com.alex.newstime.databinding.ControllerArticleBinding
import com.alex.newstime.feature.base.BaseController
import com.alex.newstime.repository.article.Article
import com.alex.newstime.util.plusAssign
import com.jakewharton.rxbinding3.view.clicks
import org.parceler.Parcels

class ArticleController(private var bundle: Bundle) : BaseController<ControllerArticleBinding>() {

    private val viewModel by lazy { getViewModel(ArticleViewModel::class.java) }

    // ----------------------------------------------------------------------------

    companion object {
        private const val KEY_ARTICLE = "KEY_ARTICLE"

        fun create(article: Article) = ArticleController(bundleOf(KEY_ARTICLE to Parcels.wrap(article)))
    }

    // ----------------------------------------------------------------------------

    override fun onCreateBinding(inflater: LayoutInflater, container: ViewGroup): ControllerArticleBinding {
        return ControllerArticleBinding.inflate(inflater, container, false)
    }

    override fun onSetupView() {
        viewModel.init(Parcels.unwrap(bundle.getParcelable(KEY_ARTICLE)))

        binding.imageViewBack.updateLayoutParams<ConstraintLayout.LayoutParams> {
            topMargin += getStatusBarHeight()
        }
    }

    override fun onViewBinding() {
        disposables += binding.textViewTitle.clicks().subscribe {
            viewModel.handleClickOnLink()
        }

        disposables += binding.imageViewBack.clicks().subscribe {
            viewModel.handleClickBack()
        }
    }

    override fun onViewModelBinding() {
        viewModel.dataState.observeNotNull { article ->
            binding.apply {
                imageView.setImage(article.urlToImage)
                imageViewBlur?.setImage(article.urlToImage, true)
                textViewTitle.text = article.title
                textViewContent.text = article.content
            }
        }

        viewModel.linkState.observeNotNull {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
        }

        viewModel.closeState.observe(this, Observer {
            router.handleBack()
        })
    }
}