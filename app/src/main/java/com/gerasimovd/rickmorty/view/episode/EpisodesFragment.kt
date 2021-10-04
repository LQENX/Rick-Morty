package com.gerasimovd.rickmorty.view.episode

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gerasimovd.rickmorty.R
import com.gerasimovd.rickmorty.adapters.CustomLoadStateAdapter
import com.gerasimovd.rickmorty.adapters.episode.EpisodesAdapter
import com.gerasimovd.rickmorty.databinding.EpisodesFragmentBinding
import com.gerasimovd.rickmorty.model.entities.episode.Episode
import com.gerasimovd.rickmorty.utils.ItemClickListener
import com.gerasimovd.rickmorty.utils.LoadingPlaceholder
import com.gerasimovd.rickmorty.utils.MessageToUser
import com.gerasimovd.rickmorty.utils.NetworkManager
import com.gerasimovd.rickmorty.view.MainActivity
import com.gerasimovd.rickmorty.viewmodel.episode.EpisodesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class EpisodesFragment : Fragment(), ItemClickListener {

    private lateinit var binding: EpisodesFragmentBinding
    private val viewModel: EpisodesViewModel by viewModels()
    private lateinit var recyclerAdapter: EpisodesAdapter



    @ExperimentalPagingApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EpisodesFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        initRecyclerView()
        setupSwipeToRefresh()
        fetchData()

        return binding.root
    }

    @ExperimentalPagingApi
    override fun <T> onClick(item: T) {
        item as Episode
        (requireActivity() as MainActivity).navigateTo(EpisodeInfoFragment.newInstance(item.id))
    }

    @ExperimentalPagingApi
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_options, menu)
        setupSearchListener(menu)
    }

    @ExperimentalPagingApi
    private fun setupSearchListener(menu: Menu) {
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { fetchData(newText) }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })
    }

    fun scrollToTop() {
        if (recyclerAdapter.itemCount != 0)
            binding.episodesRecycler.smoothScrollToPosition(0)
    }

    private fun initRecyclerView() {
        binding.episodesRecycler.apply {
            adapter = setupAdapter().withLoadStateFooter(footer = CustomLoadStateAdapter())
            setHasFixedSize(true)
        }
    }

    private fun setupAdapter(): EpisodesAdapter {
        recyclerAdapter = EpisodesAdapter(this)
        registerObserverToNetworkState()

        recyclerAdapter.apply {
            LoadingPlaceholder.attachToRecyclerAdapter(
                adapter = recyclerAdapter as PagingDataAdapter<Any, RecyclerView.ViewHolder>,
                binding = binding.loadingPlaceholder
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

    @ExperimentalPagingApi
    private fun fetchData(searchInputText: String = "") {
        lifecycleScope.launch {
            viewModel.getEpisodes(searchInputText).collectLatest {
                recyclerAdapter.submitData(it)
            }
        }
    }
}