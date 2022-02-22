package com.gerasimovd.rickmorty.view.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gerasimovd.rickmorty.R
import com.gerasimovd.rickmorty.adapters.CustomLoadStateAdapter
import com.gerasimovd.rickmorty.adapters.character.CharactersAdapter
import com.gerasimovd.rickmorty.databinding.LocationInfoFragmentBinding
import com.gerasimovd.rickmorty.model.entities.character.Character
import com.gerasimovd.rickmorty.model.entities.location.Location
import com.gerasimovd.rickmorty.utils.ItemClickListener
import com.gerasimovd.rickmorty.utils.LoadingPlaceholder
import com.gerasimovd.rickmorty.utils.MessageToUser
import com.gerasimovd.rickmorty.utils.NetworkManager
import com.gerasimovd.rickmorty.view.MainActivity
import com.gerasimovd.rickmorty.view.character.CharacterInfoFragment
import com.gerasimovd.rickmorty.viewmodel.location.LocationInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@ExperimentalPagingApi
@AndroidEntryPoint
class LocationInfoFragment : Fragment(), ItemClickListener {
    private lateinit var binding: LocationInfoFragmentBinding
    private val viewModel: LocationInfoViewModel by viewModels()
    private val locationId: Int by lazy { getLocationId() }
    private lateinit var recyclerAdapter: CharactersAdapter


    companion object {
        private const val LOCATION_ID_EXTRA = "com.gerasimovd.rickmorty.view.character_id_extra"

        fun newInstance(locationId: Int): LocationInfoFragment {
            val args = Bundle()
            args.putInt(LOCATION_ID_EXTRA, locationId)
            val fragment = LocationInfoFragment()
            fragment.arguments = args

            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LocationInfoFragmentBinding.inflate(inflater, container, false)

        initRecyclerView()
        setupSwipeToRefresh()
        fetchLocation()
        fetchCharacters()

        return binding.root
    }

    override fun <T> onClick(item: T) {
        item as Character
        (requireActivity() as MainActivity).navigateTo(CharacterInfoFragment.newInstance(item.id))
    }

    @JvmName("getLocationId1")
    private fun getLocationId() = arguments?.getInt(LOCATION_ID_EXTRA)!!

    private fun fetchLocation() {
        lifecycleScope.launch {
            viewModel.getLocationById(locationId).collectLatest { location ->
                setupData(location)
            }
        }
    }

    private fun fetchCharacters() {
        lifecycleScope.launch {
            viewModel.getCharacters().collectLatest {
                recyclerAdapter.submitData(it)
            }
        }
    }

    private fun setupData(locationInfo: Location) {
        with(binding) {
            locationName.text = locationInfo.name
            locationDimension.text = locationInfo.dimension
            locationType.text = locationInfo.type
        }
    }

    private fun initRecyclerView() {
        binding.locationCharactersRecycler.apply {
            adapter = setupAdapter().withLoadStateFooter(footer = CustomLoadStateAdapter())
            setHasFixedSize(true)
        }
    }

    private fun setupAdapter(): CharactersAdapter {
        recyclerAdapter = CharactersAdapter(this)
        registerObserverToNetworkState()

        recyclerAdapter.apply {
            LoadingPlaceholder.attachToRecyclerAdapter(
                adapter = recyclerAdapter as PagingDataAdapter<Any, RecyclerView.ViewHolder>,
                binding = binding.loadingPlaceholder,
                isMediator = true
            )
        }
        return recyclerAdapter
    }

    private fun registerObserverToNetworkState() {
        NetworkManager.IS_NETWORK_CONNECTED.observe(viewLifecycleOwner,
            { isNetworkConnected -> if (isNetworkConnected == true) recyclerAdapter.refresh() })
    }

    private fun setupSwipeToRefresh() {
        binding.swipeLayout.setOnRefreshListener {
            if (NetworkManager.IS_NETWORK_CONNECTED.value == true) {
                recyclerAdapter.refresh()
            } else {
                MessageToUser.sendToast(
                    context = requireContext(),
                    message = resources.getString(R.string.offline_status_message)
                )
            }
            binding.swipeLayout.isRefreshing = false
        }
    }

}