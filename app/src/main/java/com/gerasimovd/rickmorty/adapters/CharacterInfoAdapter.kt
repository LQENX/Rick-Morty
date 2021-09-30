package com.gerasimovd.rickmorty.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gerasimovd.rickmorty.databinding.EpisodeItemBinding
import com.gerasimovd.rickmorty.model.server.dto.episode.EpisodeDto


class CharacterInfoAdapter(private val episodes: List<EpisodeDto>) :
    RecyclerView.Adapter<CharacterInfoAdapter.ViewHolder>(){

    private lateinit var binding: EpisodeItemBinding


    class ViewHolder(private val bindingView: EpisodeItemBinding): RecyclerView.ViewHolder(bindingView.root) {
        private lateinit var currentItem: EpisodeDto

        fun bind(item: EpisodeDto) {
            currentItem = item
            bindingView.episodeName.text = currentItem.name
            bindingView.episodeAirDate.text = currentItem.air_date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = EpisodeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(episodes.get(position))
    }

    override fun getItemCount(): Int = episodes.size
}