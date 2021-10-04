package com.gerasimovd.rickmorty.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.gerasimovd.rickmorty.adapters.LocationsAdapter
import com.gerasimovd.rickmorty.databinding.LocationsFragmentBinding
import com.gerasimovd.rickmorty.viewmodel.LocationsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LocationsFragment : Fragment() {

    private lateinit var binding: LocationsFragmentBinding
    private val viewModel: LocationsViewModel by viewModels()
    private lateinit var recyclerAdapter: LocationsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LocationsFragmentBinding.inflate(inflater, container, false)

        initRecyclerView()


        return binding.root
    }

    private fun initRecyclerView() {
        recyclerAdapter = LocationsAdapter()
        binding.locationsRecycler.adapter = recyclerAdapter
    }

}