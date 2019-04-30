package com.alex.newstime.feature.article

import android.os.Bundle
import com.alex.newstime.R
import com.alex.newstime.databinding.ControllerArticleBinding
import com.alex.newstime.feature.BaseController
import com.alex.newstime.repository.news.Article
import com.alex.newstime.util.observe
import org.parceler.Parcels

class ArticleController(private var bundle: Bundle) : BaseController<ControllerArticleBinding>(R.layout.controller_article) {

    private val viewModel by lazy { viewModelProvider().get(ArticleViewModel::class.java) }

    // ----------------------------------------------------------------------------

    companion object {
        private const val KEY_ARTICLE = "KEY_ARTICLE"

        fun create(article: Article): ArticleController {
            val bundle = Bundle()
            bundle.putParcelable(KEY_ARTICLE, Parcels.wrap(article))

            return ArticleController(bundle)
        }
    }

    // ----------------------------------------------------------------------------}

    override fun onSetupView() {
        viewModel.init(Parcels.unwrap<Article>(bundle.getParcelable(KEY_ARTICLE)))
    }
    override fun onSetupViewBinding() {}

    override fun onSetupViewModelBinding() {
        viewModel.dataState.observe(this) { article ->
            binding.imageView.setImage(article.urlToImage)
        }
    }
}