package com.gerasimovd.rickmorty.utils

import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gerasimovd.rickmorty.databinding.LoadingPlaceholderBinding

/** This placeholder sets when loading error occur according to recycler adapter LoadState */

object LoadingPlaceholder {

    fun attachToRecyclerAdapter(
        adapter: PagingDataAdapter<Any, RecyclerView.ViewHolder>?,
        binding: LoadingPlaceholderBinding
    ): PagingDataAdapter<Any, RecyclerView.ViewHolder>? {
        adapter?.addLoadStateListener { combinedLoadState ->
            val stateRefresh = combinedLoadState.refresh
            val isRecyclerEmpty = adapter.itemCount == 0
            binding.offlineMessage.isVisible = isRecyclerEmpty and (stateRefresh is LoadState.Error)
            binding.loadStateProgress.isVisible = stateRefresh is LoadState.Loading
        }
        return adapter
    }
}