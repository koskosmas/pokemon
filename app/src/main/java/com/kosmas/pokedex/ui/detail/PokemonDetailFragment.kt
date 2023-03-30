package com.kosmas.pokedex.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.kosmas.core_ui.showSnackBar
import com.kosmas.pokedex.R
import com.kosmas.pokedex.databinding.FragmentPokemonDetailBinding
import com.kosmas.domain.model.PokemonDetailDomainModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonDetailFragment : Fragment() {

    private var binding: FragmentPokemonDetailBinding? = null
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchPokemonDetail(arguments?.getString("pokemonName", "").toString())
        initObserver()
    }

    private fun initObserver() {
        viewModel.pokemonDetailDomainModel.observe(viewLifecycleOwner) { initView(it) }
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            it?.let { showMessage("Error", it) }
        }
    }

    private fun showMessage(title: String, message: String) {
        binding?.root?.showSnackBar(title, message)
    }

    private fun initView(data: PokemonDetailDomainModel) {
        binding?.apply {
            scrollView.scrollTo(0, 0)
            progressBar.isVisible = false
        }
        initViewDetail(data)
        initSprites(data)
        initStats(data)
        initEvolutions(data)
    }

    private fun initViewDetail(data: PokemonDetailDomainModel) {
        binding?.layoutDetail?.apply {
            tvName.text = data.name
            Glide.with(this@PokemonDetailFragment).load(data.getImageUrl()).into(ivImage)

            tvWeightValue.text = data.weight.toString()
            tvHeightValue.text = data.height.toString()

            rvTypes.adapter = TypeAdapter {
                navigateToPokemonType(it)
            }.apply {
                submitList(data.types)
            }
            rvAbilities.adapter = AbilityAdapter().apply {
                submitList(data.abilities)
            }
        }
    }

    private fun initSprites(data: PokemonDetailDomainModel) {
        binding?.layoutSprites?.rvSprites?.adapter = SpritesAdapter().apply {
            submitList(data.sprites)
        }
    }

    private fun initStats(data: PokemonDetailDomainModel) {
        binding?.layoutStats?.rvStat?.adapter = StatAdapter().apply {
            submitList(data.statDomainModels)
        }
    }

    private fun initEvolutions(data: PokemonDetailDomainModel) {
        binding?.layoutEvolutions?.rvEvolutions?.adapter = EvolutionAdapter {
            viewModel.fetchPokemonDetail(it)
        }.apply {
            submitList(data.evolutionDomainModels)
        }
    }

    private fun navigateToPokemonType(type: String) {
        val args = Bundle().also { it.putString("pokemonType", type) }
        requireActivity().findNavController(R.id.nav_host_fragment_content_main).apply {
            navigate(R.id.action_detailFragment_to_pokemonTypeFragment, args)
        }
    }
}