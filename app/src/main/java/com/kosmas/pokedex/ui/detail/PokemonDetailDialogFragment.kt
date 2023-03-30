package com.kosmas.pokedex.ui.detail

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kosmas.core_ui.showSnackBar
import com.kosmas.domain.model.PokemonDetailDomainModel
import dagger.hilt.android.AndroidEntryPoint
import com.kosmas.pokedex.R
import com.kosmas.pokedex.databinding.FragmentPokemonDetailDialogBinding

@AndroidEntryPoint
class PokemonDetailDialogFragment : BottomSheetDialogFragment() {

    var binding: FragmentPokemonDetailDialogBinding? = null

    private val pokemonName: String by lazy {
        arguments?.getString("pokemonName").toString()
    }

    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPokemonDetailDialogBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchPokemonDetail(pokemonName)
        initObserver()
        initListener()
    }

    private fun initListener() {
        binding?.btnMoreDetail?.setOnClickListener {
            val args = Bundle().also {
                it.putString("pokemonName", pokemonName)
            }
            requireActivity().findNavController(R.id.nav_host_fragment_content_main).apply {
                navigateUp()
                navigate(R.id.action_homeFragment_to_detailFragment, args)
            }
        }
    }

    private fun initObserver() {
        viewModel.pokemonDetailDomainModel.observe(viewLifecycleOwner) { initView(it)}
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            it?.let { showMessage("Error", it) }
        }
    }

    private fun initView(data: PokemonDetailDomainModel){
        binding?.apply {
            progressBar.isVisible = false
            contentDetail.apply {
                tvName.text = data.name
                Glide.with(this@PokemonDetailDialogFragment).load(data.getImageUrl()).into(ivImage)

                tvWeightValue.text = data.getWeight()
                tvHeightValue.text = data.getHeight()

                rvTypes.adapter = TypeAdapter {}.apply {
                    submitList(data.types)
                }
                rvAbilities.adapter = AbilityAdapter().apply {
                    submitList(data.abilities)
                }
            }
        }
    }

    private fun showMessage(title: String, message: String) {
        binding?.root?.showSnackBar(title, message)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme).also {
            val metrics = resources.displayMetrics
            it.behavior.peekHeight = metrics.heightPixels
            it.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
}