package com.kosmas.pokedex.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.kosmas.core_ui.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import com.kosmas.pokedex.R
import com.kosmas.pokedex.databinding.FragmentPokemonsBinding
import com.kosmas.data.model.data.PokemonItemData

@AndroidEntryPoint
class PokemonsFragment : Fragment() {

    private var binding: FragmentPokemonsBinding? = null
    private val viewModel: PokemonViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPokemonsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchPokemons()
        initScrollView()
        initObserver()
    }

    private fun initScrollView() {
        binding?.apply {
            pokedexListScrollView.viewTreeObserver?.addOnScrollChangedListener {
                val viewTemp =
                    pokedexListScrollView.getChildAt(pokedexListScrollView.childCount - 1)

                val diff =
                    viewTemp.bottom - (pokedexListScrollView.height + pokedexListScrollView.scrollY)

                if (diff == 0) {
                    viewModel.fetchNextPokemonList()
                }
            }
        }
    }

    private fun initObserver() {
        viewModel.pokemonItemDataList.observe(viewLifecycleOwner) { data -> initAdapter(data) }
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            it?.let { showMessage("Error", it) }
        }
    }

    private fun showMessage(title: String, message: String) {
        binding?.root?.showSnackBar(title, message)
    }

    private fun initAdapter(data: List<PokemonItemData>) {
        binding?.apply {
            rvPokemonList.adapter = PokemonAdapter { pokemonName ->
                val args = Bundle().also {
                    it.putString("pokemonName", pokemonName)
                }
                requireActivity().findNavController(R.id.nav_host_fragment_content_main)
                    .navigate(R.id.action_homeFragment_to_detailDialogFragment, args)
            }.apply {
                submitList(data)
            }
        }
    }

}