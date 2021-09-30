package com.gerasimovd.rickmorty.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.gerasimovd.rickmorty.adapters.CharactersAdapter
import com.gerasimovd.rickmorty.databinding.CharactersFragmentBinding
import com.gerasimovd.rickmorty.model.server.dto.character.CharacterDto
import com.gerasimovd.rickmorty.utils.ItemClickListener
import com.gerasimovd.rickmorty.viewmodel.CharactersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CharactersFragment : Fragment(), ItemClickListener {

    private lateinit var binding: CharactersFragmentBinding
    private val viewModel: CharactersViewModel by viewModels()
    private lateinit var recyclerAdapter: CharactersAdapter



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CharactersFragmentBinding.inflate(inflater, container, false)

        initRecyclerView()
        loadData()

        return binding.root
    }

    override fun <T> onClick(item: T) {
        item as CharacterDto
        (requireActivity() as MainActivity).navigateTo(CharacterInfoFragment.newInstance(item.id))
    }

    private fun initRecyclerView() {
        recyclerAdapter = CharactersAdapter(this)
        binding.charactersRecycler.adapter = recyclerAdapter
    }

    private fun loadData() {
        lifecycleScope.launch {
            viewModel.charactersData.collect { pagingData ->
                recyclerAdapter.submitData(pagingData)
            }
        }
    }
}