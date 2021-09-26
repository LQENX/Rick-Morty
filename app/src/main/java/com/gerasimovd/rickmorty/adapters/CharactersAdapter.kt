package com.gerasimovd.rickmorty.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gerasimovd.rickmorty.databinding.CharacterItemBinding
import com.gerasimovd.rickmorty.model.server.dto.character.CharacterDto


class CharactersAdapter :
    PagingDataAdapter<CharacterDto, CharactersAdapter.ViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<CharacterDto>() {
            override fun areContentsTheSame(oldItem: CharacterDto, newItem: CharacterDto): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areItemsTheSame(oldItem: CharacterDto, newItem: CharacterDto): Boolean {
                return oldItem == newItem
            }
        }
    }

    private lateinit var binding: CharacterItemBinding


    inner class ViewHolder(val bindingView: CharacterItemBinding)
        : RecyclerView.ViewHolder(bindingView.root)
        /*private lateinit var character: CharacterResponse

        fun bind(item: CharacterResponse) {
            character = item
            bindImage(character.image)
            bindingView.characterName.text = character.name
        }

        private fun bindImage(image: String) {
            Glide.with(bindingView.root)
                .load(image)
                .into(bindingView.characterImage)
        }*/



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = CharacterItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)

        holder.bindingView.apply {
            Glide.with(this.root)
                .load(currentItem?.image)
                .into(this.characterImage)

            characterName.text = currentItem?.name
        }
    }

}