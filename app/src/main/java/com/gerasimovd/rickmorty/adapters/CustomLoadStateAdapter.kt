package com.gerasimovd.rickmorty.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gerasimovd.rickmorty.databinding.LoadingPlaceholderBinding


class CustomLoadStateAdapter
    :LoadStateAdapter<CustomLoadStateAdapter.LoadStateViewHolder>(){

    private lateinit var binding: LoadingPlaceholderBinding


    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        binding = LoadingPlaceholderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class LoadStateViewHolder(private val bindingView: LoadingPlaceholderBinding)
        : RecyclerView.ViewHolder(bindingView.root) {

        fun bind(loadState: LoadState) {
            bindingView.apply {
                offlineMessage.isVisible = loadState is LoadState.Error
                loadStateProgress.isVisible = loadState is LoadState.Loading
            }
        }
    }

}