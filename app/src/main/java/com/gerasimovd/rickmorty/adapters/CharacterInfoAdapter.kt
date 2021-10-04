package com.gerasimovd.rickmorty.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gerasimovd.rickmorty.databinding.EpisodeItemBinding
import com.gerasimovd.rickmorty.model.entities.episode.Episode
import com.gerasimovd.rickmorty.utils.ItemClickListener


class CharacterInfoAdapter(private val recyclerListener: ItemClickListener) :
    PagingDataAdapter<Episode, CharacterInfoAdapter.ViewHolder>(diffCallback){

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Episode>() {
            override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
                return oldItem == newItem
            }
        }
    }

    private lateinit var binding: EpisodeItemBinding


    class ViewHolder(private val bindingView: EpisodeItemBinding): RecyclerView.ViewHolder(bindingView.root) {

        fun bind(item: Episode, clickListener: ItemClickListener) {
            bindingView.apply {
                root.setOnClickListener { clickListener.onClick(item) }
                episodeName.text = item.name
                episodeAirDate.text = item.air_date
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = EpisodeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        currentItem?.let { holder.bind(currentItem, recyclerListener) }
    }
}