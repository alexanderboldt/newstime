package com.alex.newstime.feature.article

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.updateLayoutParams
import androidx.navigation.fragment.findNavController
import com.alex.newstime.databinding.FragmentArticleBinding
import com.alex.newstime.feature.base.BaseFragment
import com.alex.newstime.repository.article.Article
import com.alex.newstime.util.plusAssign
import com.jakewharton.rxbinding4.view.clicks
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArticleFragment : BaseFragment<FragmentArticleBinding>() {

    private val viewModel: ArticleViewModel by viewModel()

    // ----------------------------------------------------------------------------

    companion object {
        private const val KEY_ARTICLE = "KEY_ARTICLE"

        fun bundle(article: Article) = bundleOf(KEY_ARTICLE to article)
    }

    // ----------------------------------------------------------------------------

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): FragmentArticleBinding {
        return FragmentArticleBinding.inflate(inflater, container, false)
    }

    override fun setupView() {
        viewModel.init(requireArguments().getParcelable(KEY_ARTICLE)!!)

        binding.imageViewBack.updateLayoutParams<ConstraintLayout.LayoutParams> {
            topMargin += getStatusBarHeight()
        }
    }

    override fun bindView() {
        disposables += binding.textViewTitle.clicks().subscribe {
            viewModel.handleClickOnLink()
        }

        disposables += binding.imageViewBack.clicks().subscribe {
            findNavController().navigateUp()
        }
    }

    override fun bindViewModel() {
        viewModel.dataState.observe { article ->
            binding.apply {
                imageView.setImage(article.urlToImage)
                imageViewBlur?.setImage(article.urlToImage, true)
                textViewTitle.text = article.title
                textViewContent.text = article.content
            }
        }

        viewModel.linkState.observe {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
        }

        viewModel.closeState.observe {
            findNavController().navigateUp()
        }
    }
}