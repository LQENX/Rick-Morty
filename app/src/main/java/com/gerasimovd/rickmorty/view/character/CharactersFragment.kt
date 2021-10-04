package com.gerasimovd.rickmorty.view.character

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
import com.gerasimovd.rickmorty.adapters.character.CharactersAdapter
import com.gerasimovd.rickmorty.adapters.CustomLoadStateAdapter
import com.gerasimovd.rickmorty.databinding.CharactersFragmentBinding
import com.gerasimovd.rickmorty.model.entities.character.Character
import com.gerasimovd.rickmorty.utils.ItemClickListener
import com.gerasimovd.rickmorty.utils.LoadingPlaceholder
import com.gerasimovd.rickmorty.utils.MessageToUser
import com.gerasimovd.rickmorty.utils.NetworkManager
import com.gerasimovd.rickmorty.view.MainActivity
import com.gerasimovd.rickmorty.viewmodel.character.CharactersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CharactersFragment : Fragment(), ItemClickListener {

    private lateinit var binding: CharactersFragmentBinding
    private val viewModel: CharactersViewModel by viewModels()
    private lateinit var recyclerAdapter: CharactersAdapter



    @ExperimentalPagingApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CharactersFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        initRecyclerView()
        setupSwipeToRefresh()
        fetchData()

        return binding.root
    }

    @ExperimentalPagingApi
    override fun <T> onClick(item: T) {
        item as Character
        (requireActivity() as MainActivity).navigateTo(CharacterInfoFragment.newInstance(item.id))
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
            binding.charactersRecycler.smoothScrollToPosition(0)
    }

    private fun initRecyclerView() {
        binding.charactersRecycler.apply {
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
            viewModel.getCharacters(searchInputText).collectLatest {
                recyclerAdapter.submitData(it)
            }
        }
    }
}