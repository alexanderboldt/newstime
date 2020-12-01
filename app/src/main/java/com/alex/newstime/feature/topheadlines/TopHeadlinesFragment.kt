package com.alex.newstime.feature.topheadlines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.newstime.bus.ConnectivityEvent
import com.alex.newstime.databinding.FragmentTopHeadlinesBinding
import com.alex.newstime.feature.base.BaseFragment
import com.alex.newstime.feature.topheadlines.model.RecyclerViewState
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.ldralighieri.corbind.swiperefreshlayout.refreshes

class TopHeadlinesFragment : BaseFragment() {

    private val viewModel: TopHeadlinesViewModel by viewModel()

    private lateinit var binding: FragmentTopHeadlinesBinding

    private lateinit var adapter: TopHeadlinesAdapter

    // ----------------------------------------------------------------------------

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTopHeadlinesBinding.inflate(inflater, container, false)

        setupView()
        setupViewBinding()
        setupViewModel()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    // ----------------------------------------------------------------------------

    @Subscribe
    fun onEventConnectivity(event: ConnectivityEvent) {
        if (event.isConnected) {
            viewModel.init()
        }
    }

    // ----------------------------------------------------------------------------

    private fun setupView() {
        adapter = TopHeadlinesAdapter(
            viewModel::clickOnArticle,
            viewModel::loadMoreArticles
        )

        binding.recyclerView.also {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
            it.updatePadding(top = getStatusBarHeight())
        }
    }

    private fun setupViewBinding() {
        lifecycleScope.launch {
            binding.swipeRefreshLayout.refreshes {
                viewModel.onSwipeRefreshLayout()
            }
        }
    }

    private fun setupViewModel() {
        viewModel.loadingState.observe {
            binding.swipeRefreshLayout.isRefreshing = it
        }

        viewModel.recyclerViewState.observe { state ->
            when (state) {
                is RecyclerViewState.ArticlesState -> {
                    binding.apply {
                        recyclerView.isVisible = true
                        textViewMessage.isGone = true
                    }
                    adapter.setItems(state.items)
                }
                is RecyclerViewState.MessageState -> {
                    binding.apply {
                        recyclerView.isInvisible = true
                        textViewMessage.isVisible = true
                        textViewMessage.text = state.message
                    }
                }
            }
        }

        viewModel.messageState.observe {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.detailState.observe { article ->
            TopHeadlinesFragmentDirections
                .actionToArticleFragment(article)
                .also { directions -> findNavController().navigate(directions) }
        }

        viewModel.init()
    }
}