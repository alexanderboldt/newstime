package com.alex.newstime.feature.article

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.BlurTransformation
import com.alex.newstime.databinding.FragmentArticleBinding
import com.alex.newstime.feature.base.BaseFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.ldralighieri.corbind.view.clicks
import timber.log.Timber

class ArticleFragment : BaseFragment() {

    private val viewModel: ArticleViewModel by viewModel()

    private lateinit var binding: FragmentArticleBinding

    private val arguments: ArticleFragmentArgs by navArgs()

    // ----------------------------------------------------------------------------

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentArticleBinding.inflate(inflater, container, false)

        setupView()
        setupViewBinding()
        setupViewModel()

        return binding.root
    }

    // ----------------------------------------------------------------------------

    private fun setupView() {
        viewModel.init(arguments.article)

        binding.imageViewBack.updateLayoutParams<ConstraintLayout.LayoutParams> {
            topMargin += getStatusBarHeight()
        }
    }

    private fun setupViewBinding() {
        Timber.d("setupViewBinding")
        lifecycleScope.launch {
            binding.textViewTitle.clicks {
                viewModel.handleClickOnLink()
            }
        }

        lifecycleScope.launch {
            binding.imageViewBack.clicks {
                viewModel.handleClickBack()
            }
        }
    }

    private fun setupViewModel() {
        viewModel.dataState.observe { article ->
            binding.apply {
                if (article.urlToImage.isNullOrEmpty()) {
                    //imageViewPreview.isGone = true
                } else {
                    //imageViewPreview.isVisible = true
                    imageViewPreview.load(article.urlToImage)
                }

                imageViewPreview.load(article.urlToImage)
                textViewSource.text = article.source
                textViewDate.text = article.date

                imageViewBlur?.load(article.urlToImage) {
                    transformations(BlurTransformation(requireContext(), 25f, 20f))
                }
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