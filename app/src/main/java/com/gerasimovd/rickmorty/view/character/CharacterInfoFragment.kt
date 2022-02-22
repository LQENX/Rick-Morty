package com.gerasimovd.rickmorty.view.character

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
import com.bumptech.glide.Glide
import com.gerasimovd.rickmorty.R
import com.gerasimovd.rickmorty.adapters.CustomLoadStateAdapter
import com.gerasimovd.rickmorty.adapters.episode.EpisodesAdapter
import com.gerasimovd.rickmorty.databinding.CharacterInfoFragmentBinding
import com.gerasimovd.rickmorty.model.entities.character.Character
import com.gerasimovd.rickmorty.model.entities.episode.Episode
import com.gerasimovd.rickmorty.utils.ItemClickListener
import com.gerasimovd.rickmorty.utils.LoadingPlaceholder
import com.gerasimovd.rickmorty.utils.MessageToUser
import com.gerasimovd.rickmorty.utils.NetworkManager
import com.gerasimovd.rickmorty.view.MainActivity
import com.gerasimovd.rickmorty.view.episode.EpisodeInfoFragment
import com.gerasimovd.rickmorty.view.episode.EpisodesFragment
import com.gerasimovd.rickmorty.viewmodel.character.CharacterInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalPagingApi
@AndroidEntryPoint
class CharacterInfoFragment : Fragment(), ItemClickListener {
    private lateinit var binding: CharacterInfoFragmentBinding
    private val viewModel: CharacterInfoViewModel by viewModels()
    private val characterId: Int by lazy { getCharacterId() }
    private lateinit var recyclerAdapter: EpisodesAdapter


    companion object {
        private const val CHARACTER_ID_EXTRA = "com.gerasimovd.rickmorty.view.character_id_extra"

        fun newInstance(characterId: Int): CharacterInfoFragment {
            val args = Bundle()
            args.putInt(CHARACTER_ID_EXTRA, characterId)
            val fragment = CharacterInfoFragment()
            fragment.arguments = args

            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CharacterInfoFragmentBinding.inflate(inflater, container, false)

        initRecyclerView()
        setupSwipeToRefresh()
        fetchCharacter()
        fetchEpisodes()

        return binding.root
    }

    override fun <T> onClick(item: T) {
        item as Episode
        (requireActivity() as MainActivity).navigateTo(EpisodeInfoFragment.newInstance(item.id))
    }

    @JvmName("getCharacterId1")
    private fun getCharacterId() = arguments?.getInt(CHARACTER_ID_EXTRA)!!

    private fun fetchCharacter() {
        lifecycleScope.launch {
            viewModel.getCharacterById(characterId).collectLatest { character ->
                setupData(character)
            }
        }
    }

    @ExperimentalPagingApi
    private fun fetchEpisodes() {
        lifecycleScope.launch {
            viewModel.getEpisodes().collectLatest {
                recyclerAdapter.submitData(it)
            }
        }
    }

    private fun setupData(characterInfo: Character) {
        with(binding) {
            characterName.text = characterInfo.name
            characterStatus.text = characterInfo.status
            characterSpecies.text = characterInfo.species
            characterGender.text = characterInfo.gender
            characterOrigin.text = characterInfo.origin.name
            characterLocation.text = characterInfo.location.name

            Glide.with(requireActivity())
                .load(characterInfo.image)
                .into(characterImage)
        }
    }

    private fun initRecyclerView() {
        binding.characterEpisodesRecycler.apply {
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