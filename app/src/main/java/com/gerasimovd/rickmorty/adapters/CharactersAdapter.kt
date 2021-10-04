package com.gerasimovd.rickmorty.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gerasimovd.rickmorty.R
import com.gerasimovd.rickmorty.databinding.CharacterItemBinding
import com.gerasimovd.rickmorty.model.entities.character.Character
import com.gerasimovd.rickmorty.utils.ItemClickListener


class CharactersAdapter(private val recyclerListener: ItemClickListener) :
    PagingDataAdapter<Character, CharactersAdapter.ViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Character>() {
            override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
                return oldItem == newItem
            }
        }
    }

    private lateinit var binding: CharacterItemBinding


    class ViewHolder(private val bindingView: CharacterItemBinding)
        : RecyclerView.ViewHolder(bindingView.root) {

        fun bind(item: Character, clickListener: ItemClickListener) {
            bindingView.apply {
                root.setOnClickListener { clickListener.onClick(item) }

                Glide.with(this.root)
                    .load(item.image)
                    .placeholder(root.resources.getDrawable(R.drawable.ic_image_128))
                    .error(root.resources.getDrawable(R.drawable.ic_image_128))
                    .into(this.characterImage)

                characterName.text = item.name
                characterSpecies.text = item.species
                characterGender.text = item.gender
                characterLastLocation.text = item.location.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = CharacterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        currentItem?.let { holder.bind(currentItem, recyclerListener) }
    }

}