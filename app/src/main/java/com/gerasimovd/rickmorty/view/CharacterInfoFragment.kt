package com.gerasimovd.rickmorty.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.gerasimovd.rickmorty.adapters.CharacterInfoAdapter
import com.gerasimovd.rickmorty.databinding.CharacterInfoFragmentBinding
import com.gerasimovd.rickmorty.model.server.api.ApiImpl
import com.gerasimovd.rickmorty.model.server.dto.character.CharacterDto
import com.gerasimovd.rickmorty.model.server.dto.episode.EpisodeDto
import com.gerasimovd.rickmorty.viewmodel.CharacterInfoViewModel
import kotlinx.coroutines.launch


class CharacterInfoFragment private constructor(): Fragment() {
    private lateinit var binding: CharacterInfoFragmentBinding
    private val viewModel: CharacterInfoViewModel by viewModels()
    private val characterId: Int by lazy { getCharacterId() }


    companion object {
        private const val  CHARACTER_ID_EXTRA = "com.gerasimovd.rickmorty.view.character_id_extra"

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

        loadData()

        return binding.root
    }

    @JvmName("getCharacterId1")
    private fun getCharacterId() = arguments?.getInt(CHARACTER_ID_EXTRA)!!

    private fun loadData() {
        lifecycleScope.launch {
            val characterInfo = ApiImpl.getCharacterById(characterId)
            setupData(characterInfo)

            val episodesId = getEpisodesId(characterInfo?.episodes ?: emptyList())
            val episodes = ApiImpl.getEpisodesById(episodesId) ?: emptyList()
            initRecycler(episodes)
        }
    }

    private fun setupData(characterInfo: CharacterDto?) {
        with(binding) {
            characterName.text = characterInfo?.name
            characterStatus.text = characterInfo?.status
            characterSpecies.text = characterInfo?.species
            characterGender.text = characterInfo?.gender
            characterOrigin.text = characterInfo?.origin?.name
            characterLocation.text = characterInfo?.location?.name

            Glide.with(requireActivity())
                .load(characterInfo?.image)
                .into(characterImage)
        }
    }

    private fun initRecycler(episodes: List<EpisodeDto>) {
        val adapter = CharacterInfoAdapter(episodes = episodes)
        binding.characterEpisodesRecycler.adapter = adapter
    }

    private fun getEpisodesId(episodesUrl: List<String>): String {
        val result = mutableListOf<String>()
        if (episodesUrl.size > 1) {
            for (episode in episodesUrl) {
                result.add(episode.filter { it.isDigit() })
            }

        } else if (episodesUrl.size == 1) {
            result.add(episodesUrl.first().filter { it.isDigit() })
        }

        return result.toString()
            .replace(" ", "")
            .replace("[","")
            .replace("]","")
    }
}