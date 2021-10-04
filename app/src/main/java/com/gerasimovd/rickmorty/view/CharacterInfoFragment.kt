package com.gerasimovd.rickmorty.view

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
import com.gerasimovd.rickmorty.adapters.CharacterInfoAdapter
import com.gerasimovd.rickmorty.adapters.CustomLoadStateAdapter
import com.gerasimovd.rickmorty.databinding.CharacterInfoFragmentBinding
import com.gerasimovd.rickmorty.model.entities.character.Character
import com.gerasimovd.rickmorty.model.entities.episode.Episode
import com.gerasimovd.rickmorty.utils.ItemClickListener
import com.gerasimovd.rickmorty.utils.LoadingPlaceholder
import com.gerasimovd.rickmorty.utils.MessageToUser
import com.gerasimovd.rickmorty.utils.NetworkManager
import com.gerasimovd.rickmorty.viewmodel.CharactersInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalPagingApi
@AndroidEntryPoint
class CharacterInfoFragment : Fragment(), ItemClickListener {
    private lateinit var binding: CharacterInfoFragmentBinding
    private val viewModel: CharactersInfoViewModel by viewModels()
    private val characterId: Int by lazy { getCharacterId() }
    private lateinit var recyclerAdapter: CharacterInfoAdapter


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
        /*(requireActivity() as MainActivity).navigateTo(newInstance(item.id))*/ //todo: implement navigation
    }

    @JvmName("getCharacterId1")
    private fun getCharacterId() = arguments?.getInt(CHARACTER_ID_EXTRA)!!

    private fun fetchCharacter() {
        lifecycleScope.launch {
            var characterInfo: Character? = null
            viewModel.getCharacterById(characterId).collectLatest { character ->
                characterInfo = character
                setupData(characterInfo!!)
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

    private fun setupAdapter(): CharacterInfoAdapter {
        recyclerAdapter = CharacterInfoAdapter(this)
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