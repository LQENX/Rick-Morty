package com.gerasimovd.rickmorty.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gerasimovd.rickmorty.databinding.LocationItemBinding
import com.gerasimovd.rickmorty.model.remote.dto.location.LocationDto
import com.gerasimovd.rickmorty.utils.Constants

class LocationsAdapter :
    PagingDataAdapter<LocationDto, LocationsAdapter.ViewHolder>(diffCallback){

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<LocationDto>() {
            override fun areContentsTheSame(oldItem: LocationDto, newItem: LocationDto): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areItemsTheSame(oldItem: LocationDto, newItem: LocationDto): Boolean {
                return oldItem == newItem
            }
        }
    }

    private lateinit var binding: LocationItemBinding


    inner class ViewHolder(val bindingView: LocationItemBinding)
        : RecyclerView.ViewHolder(bindingView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = LocationItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)

        holder.bindingView.apply {
            binding.locationName.text = currentItem?.name ?: Constants.UNKNOWN_DATA
            binding.locationDimension.text = currentItem?.dimension ?: Constants.UNKNOWN_DATA
        }
    }
}