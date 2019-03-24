package com.alex.newstime.feature.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alex.newstime.databinding.ControllerArticleBinding
import com.alex.newstime.feature.AbstractController
import com.alex.newstime.repository.news.Article
import com.alex.newstime.util.observe
import org.parceler.Parcels

class ArticleController(private var bundle: Bundle) : AbstractController() {

    private lateinit var binding: ControllerArticleBinding
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        binding = ControllerArticleBinding.inflate(inflater, container, false)

        viewModel.init(Parcels.unwrap<Article>(bundle.getParcelable(KEY_ARTICLE)))

        return binding.root
    }

    override fun onAttach(view: View) {
        setupViewModelBinding()
    }

    // ----------------------------------------------------------------------------}

    private fun setupViewModelBinding() {
        viewModel.dataState.observe(this) { article ->
            binding.imageView.setImage(article.urlToImage)
        }
    }
}