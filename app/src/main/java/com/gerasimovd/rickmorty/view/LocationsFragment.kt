package com.gerasimovd.rickmorty.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.gerasimovd.rickmorty.adapters.CharactersAdapter
import com.gerasimovd.rickmorty.adapters.LocationsAdapter
import com.gerasimovd.rickmorty.databinding.LocationsFragmentBinding
import com.gerasimovd.rickmorty.viewmodel.LocationsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


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
        loadData()

        return binding.root
    }

    private fun initRecyclerView() {
        recyclerAdapter = LocationsAdapter()
        binding.locationsRecycler.adapter = recyclerAdapter
    }

    private fun loadData() {
        lifecycleScope.launch {
            viewModel.locationsData.collect { pagingData ->
                recyclerAdapter.submitData(pagingData)
            }
        }
    }
}