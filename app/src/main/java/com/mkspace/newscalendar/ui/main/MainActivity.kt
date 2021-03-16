package com.mkspace.newscalendar.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import com.funin.base.base.combine
import com.funin.base.funinbase.base.BaseActivity
import com.mkspace.newscalendar.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity(), ArticleAdapter.OnClickListener {

    @ExperimentalCoroutinesApi
    private val articleViewModel by viewModels<ArticleViewModel>()

    private lateinit var adapter: ArticleAdapter

    @FlowPreview
    @ExperimentalPagingApi
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = ArticleAdapter(this)

        setupViews()
        setupSearchView()
        bindViewModels()
    }

    private fun setupViews() {
        mainNewsRecycler.adapter = adapter
        mainNewsSwipeRefreshLayout?.setOnRefreshListener { adapter.refresh() }
    }

    @ExperimentalCoroutinesApi
    private fun setupSearchView() {
        mainQueryInput.setOnEditorActionListener { v, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    articleViewModel.setQuery(v.text.toString())
                    true
                }
                else -> false
            }
        }
    }

    @FlowPreview
    @ExperimentalPagingApi
    @ExperimentalCoroutinesApi
    private fun bindViewModels() {
        lifecycleScope.launchWhenCreated {
            articleViewModel.items.collectLatest {
                adapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest { loadStates ->
                if (loadStates.refresh !is LoadState.Loading) {
                    mainNewsSwipeRefreshLayout.isRefreshing = false
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { mainNewsRecycler?.scrollToPosition(0) }
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow
                .combine(
                    articleViewModel.isLocalEmpty.combine(articleViewModel.isRemoteEmpty)
                )
                .mapLatest { (loadStates, emptySet) ->
                    val (isLocalEmpty, isRemoteEmpty) = emptySet
                    loadStates.mediator?.refresh is LoadState.NotLoading
                            && isLocalEmpty && isRemoteEmpty
                }
                .distinctUntilChanged()
                .collectLatest { isActualEmpty -> mainQueryDivider?.isVisible = isActualEmpty }
        }
    }

    override fun onWebLinkClick(webUrl: String) {
        startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(webUrl)))
    }
}