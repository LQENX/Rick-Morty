package com.gerasimovd.rickmorty.adapters.location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gerasimovd.rickmorty.databinding.LocationItemBinding
import com.gerasimovd.rickmorty.model.entities.location.Location
import com.gerasimovd.rickmorty.utils.ItemClickListener

class LocationsAdapter(private val recyclerListener: ItemClickListener) :
    PagingDataAdapter<Location, LocationsAdapter.ViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Location>() {
            override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
                return oldItem == newItem
            }
        }
    }

    private lateinit var binding: LocationItemBinding


    inner class ViewHolder(val bindingView: LocationItemBinding) :
        RecyclerView.ViewHolder(bindingView.root) {

        fun bind(item: Location, clickListener: ItemClickListener) {
            bindingView.apply {
                root.setOnClickListener { clickListener.onClick(item) }
                locationName.text = item.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = LocationItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        currentItem?.let { holder.bind(currentItem, recyclerListener) }
    }
}