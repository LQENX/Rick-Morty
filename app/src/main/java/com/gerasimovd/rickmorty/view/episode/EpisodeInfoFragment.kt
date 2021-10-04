package com.gerasimovd.rickmorty.view.episode

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
import com.gerasimovd.rickmorty.adapters.episode.EpisodesAdapter
import com.gerasimovd.rickmorty.databinding.EpisodeInfoFragmentBinding
import com.gerasimovd.rickmorty.model.entities.character.Character
import com.gerasimovd.rickmorty.model.entities.episode.Episode
import com.gerasimovd.rickmorty.utils.ItemClickListener
import com.gerasimovd.rickmorty.utils.LoadingPlaceholder
import com.gerasimovd.rickmorty.utils.MessageToUser
import com.gerasimovd.rickmorty.utils.NetworkManager
import com.gerasimovd.rickmorty.view.MainActivity
import com.gerasimovd.rickmorty.view.character.CharacterInfoFragment
import com.gerasimovd.rickmorty.viewmodel.episode.EpisodeInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalPagingApi
@AndroidEntryPoint
class EpisodeInfoFragment : Fragment(), ItemClickListener {
    private lateinit var binding: EpisodeInfoFragmentBinding
    private val viewModel: EpisodeInfoViewModel by viewModels()
    private val episodeId: Int by lazy { getEpisodeId() }
    private lateinit var recyclerAdapter: CharactersAdapter


    companion object {
        private const val EPISODE_ID_EXTRA = "com.gerasimovd.rickmorty.view.character_id_extra"

        fun newInstance(characterId: Int): EpisodeInfoFragment {
            val args = Bundle()
            args.putInt(EPISODE_ID_EXTRA, characterId)
            val fragment = EpisodeInfoFragment()
            fragment.arguments = args

            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EpisodeInfoFragmentBinding.inflate(inflater, container, false)

        initRecyclerView()
        setupSwipeToRefresh()
        fetchEpisode()
        fetchCharacters()

        return binding.root
    }

    override fun <T> onClick(item: T) {
        item as Character
        (requireActivity() as MainActivity).navigateTo(CharacterInfoFragment.newInstance(item.id))
    }

    @JvmName("getEpisodeId1")
    private fun getEpisodeId() = arguments?.getInt(EPISODE_ID_EXTRA)!!

    private fun fetchEpisode() {
        lifecycleScope.launch {
            viewModel.getEpisodeById(episodeId).collectLatest { episode ->
                setupData(episode)
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

    private fun setupData(episodeInfo: Episode) {
        with(binding) {
            episodeName.text = episodeInfo.name
            episodeAirDate.text = episodeInfo.air_date
            episodeCode.text = episodeInfo.episode
        }
    }

    private fun initRecyclerView() {
        binding.episodeCharactersRecycler.apply {
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
}