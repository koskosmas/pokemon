package com.kosmas.pokedex.ui.pokemontype

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.kosmas.core_ui.showSnackBar
import com.kosmas.pokedex.R
import com.kosmas.pokedex.databinding.FragmentPokemonTypeBinding
import com.kosmas.pokedex.model.PaginationModel
import com.kosmas.data.model.data.PokemonData
import com.kosmas.pokedex.model.PokemonModel
import com.kosmas.pokedex.model.PokemonPageModel
import com.kosmas.core_ui.utils.PokemonColorsUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class PokemonTypeFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var binding: FragmentPokemonTypeBinding? = null

    private val pokemonType by lazy { arguments?.getString("pokemonType") }

    private val viewModel: PokemonTypeViewModel by viewModels()
    private var pokemons: List<PokemonData> = mutableListOf()
    private var paginations: MutableList<PaginationModel> = mutableListOf()
    private var pokemonAdapter: PokemonAdapter? = null
    private var paginationAdapter: PaginationAdapter = PaginationAdapter { _, _ -> }
    private var pageIndexSelected: Int = 1
    private var pagePaginationIndexSelected: Int = 1
    private var pokemonCountPerPage: Int = 5
    private var startPage: Int = 1
    private var endPage: Int = 5
    private var pokemonPages: MutableList<PokemonModel> = mutableListOf()
    private var pokemonPagesPagination: MutableList<PokemonPageModel> = mutableListOf()
    private var spinnerAdapter: ArrayAdapter<CharSequence>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPokemonTypeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        viewModel.getPokemonType(pokemonType.toString())
    }

    private fun initObserver() {
        viewModel.pokemon.observe(viewLifecycleOwner) { initView(it.data) }
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            it?.let { showMessage("Error", it) }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initView(data: List<PokemonData>?) {
        data?.let {
            pokemons = it
            pageSeparations()
            initPagination()
            initPageSelection()
            binding?.apply {
                progressBar.isVisible = false
                pokemonType?.let {
                    tvPokemonTypeName.text = it
                    tvTotalPageValue.text = pokemons.size.toString()
                    ivBgTypeTop.setBackgroundTintColor(it)
                    ivBgTypeBottom.setBackgroundTintColor(it)
                }
                pokemonAdapter = PokemonAdapter({ pokemonName ->
                    navigateToPokemonDetail(Bundle().also {
                        it.putString("pokemonName", pokemonName)
                    })

                }, { type ->
                    ivBgTypeTop.setBackgroundTintColor(type)
                    ivBgTypeBottom.setBackgroundTintColor(type)
                })
                rvPokemon.adapter = pokemonAdapter
                pokemonAdapter?.submitList(pokemonPagesPagination[pagePaginationIndexSelected - 1].pages[0].pokemons)
                pokemonAdapter?.notifyDataSetChanged()
            }
        }
    }

    private fun navigateToPokemonDetail(type: Bundle) {
        requireActivity().findNavController(R.id.nav_host_fragment_content_main).apply {
            navigate(R.id.action_pokemonTypeFragment_to_detailFragment, type)
        }
    }

    private fun initPageSelection() {
        spinnerAdapter = ArrayAdapter.createFromResource(
            requireContext(), R.array.page_selection_array, R.layout.spinner_item
        ).also {
            it.setDropDownViewResource(R.layout.spinner_dropdown_item)
            binding?.spinnerPage?.apply {
                adapter = it
                val spinnerPosition = it.getPosition("5")
                setSelection(spinnerPosition)
                onItemSelectedListener = this@PokemonTypeFragment
            }
        }
    }

    private fun pageSeparations() {
        pokemonPages.clear()
        pokemonPagesPagination.clear()
        var indexPage = 0
        var indexPagePagination = 0
        while (indexPage * pokemonCountPerPage < pokemons.size) {
            pokemonPages.add(
                PokemonModel(
                    indexPage, generatePage(indexPage)
                )
            )
            indexPage++
        }

        while (indexPagePagination * 5 < pokemonPages.size) {
            pokemonPagesPagination.add(
                PokemonPageModel(
                    indexPagePagination, generatePagePagination(indexPagePagination)
                )
            )
            indexPagePagination++
        }
    }

    private fun generatePage(indexTemp: Int): List<PokemonData> {
        pokemons.filterIndexed { index, _ ->
            index in indexTemp * pokemonCountPerPage until ((indexTemp + 1) * pokemonCountPerPage)
        }.apply {
            return this
        }
    }

    private fun generatePagePagination(indexTemp: Int): List<PokemonModel> {
        pokemonPages.filterIndexed { index, _ ->
            index in indexTemp * 5 until ((indexTemp + 1) * 5)
        }.apply {
            return this
        }
    }

    private fun initPagination() {
        binding?.rvPagination?.apply {
            val layoutManagerTemp = FlexboxLayoutManager(context)
            layoutManagerTemp.flexDirection = FlexDirection.ROW
            layoutManagerTemp.justifyContent = JustifyContent.CENTER
            layoutManagerTemp.alignItems = AlignItems.CENTER
            layoutManager = layoutManagerTemp
            paginationAdapter = PaginationAdapter { item, index ->
                if (isNumberButton(item)) {
                    paginations.filter { page -> page.isActive }.apply {
                        if (this.isNotEmpty()) this[0].isActive = false
                    }
                    paginations[index].isActive = true
                    showPokemonBySelectedPage(paginations[index].pagination.toInt())
                } else {
                    onPaginationSelected(item)
                }
            }
            adapter = paginationAdapter
            generatePagination()
            pageIndexSelected = startPage
        }
    }

    private fun generatePagination(isStartPage: Boolean = true) {
        startPage = pokemonPagesPagination[pagePaginationIndexSelected - 1].pages.first().index + 1
        endPage = pokemonPagesPagination[pagePaginationIndexSelected - 1].pages.last().index + 1
        pageIndexSelected = if (isStartPage) startPage else endPage
        paginations = generateList(selectedPage = pageIndexSelected)
        setPaginationAdapterData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showPokemonBySelectedPage(page: Int) {
        pageIndexSelected = page
        pokemonAdapter?.submitList(
            pokemonPagesPagination[pagePaginationIndexSelected - 1].pages[getPagesIndex()].pokemons
        )
        pokemonAdapter?.notifyDataSetChanged()
        paginations = generateList(selectedPage = pageIndexSelected)
        setPaginationAdapterData()
    }

    private fun onPaginationSelected(item: String) {
        when (item) {
            "<<" -> previousPageValidation()
            ">>" -> nextPageValidation()
            "<" -> {
                if (getPagesIndex() == 0) previousPageValidation()
                else showPreviousPage()
            }
            ">" -> {
                if (pageIndexSelected.mod(5) == 0) nextPageValidation()
                else showNextPage()
            }
        }
    }

    private fun previousPageValidation() {
        if (pagePaginationIndexSelected == 1) {
            showMessage("Info", "Previous page not available")
        } else {
            moveToPreviousPage()
        }
    }

    private fun nextPageValidation() {
        if (pagePaginationIndexSelected == pokemonPagesPagination.size) {
            showMessage("Info", "Next page not available")

        } else {
            moveToNextPage()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showPreviousPage() {
        if (indexHasAvailable()) {
            pageIndexSelected--
            pokemonAdapter?.submitList(
                pokemonPagesPagination[pagePaginationIndexSelected - 1].pages[getPagesIndex()].pokemons
            )
            pokemonAdapter?.notifyDataSetChanged()
            paginations = generateList(selectedPage = pageIndexSelected)
            setPaginationAdapterData()
        } else {
            showMessage("Info", "Previous page not available")
        }
    }

    private fun indexHasAvailable(): Boolean {
        return pageIndexSelected in startPage..endPage
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showNextPage() {
        if (indexHasAvailable()) {
            pageIndexSelected++
            pokemonAdapter?.submitList(
                pokemonPagesPagination[pagePaginationIndexSelected - 1].pages[getPagesIndex()].pokemons
            )
            pokemonAdapter?.notifyDataSetChanged()
            paginations = generateList(selectedPage = pageIndexSelected)
            setPaginationAdapterData()
        } else {
            showMessage("Info", "Next page not available")
        }
    }

    private fun showMessage(title: String, message: String) {
        binding?.root?.showSnackBar(title, message)
    }

    private fun getPagesIndex(): Int {
        return abs((pageIndexSelected - ((pagePaginationIndexSelected - 1) * 5)) - 1)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun moveToPreviousPage() {
        pagePaginationIndexSelected--
        pokemonAdapter?.submitList(pokemonPagesPagination[pagePaginationIndexSelected - 1].pages[4].pokemons)
        pokemonAdapter?.notifyDataSetChanged()
        generatePagination(false)
        setPaginationAdapterData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun moveToNextPage() {
        pagePaginationIndexSelected++
        pokemonAdapter?.submitList(pokemonPagesPagination[pagePaginationIndexSelected - 1].pages[0].pokemons)
        pokemonAdapter?.notifyDataSetChanged()
        generatePagination()
        setPaginationAdapterData()
    }

    private fun isNumberButton(text: String): Boolean {
        return !listOf("<<", "<", ">", ">>").contains(text)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setPaginationAdapterData() {
        paginationAdapter.submitList(paginations)
        paginationAdapter.notifyDataSetChanged()
    }

    private fun generateList(selectedPage: Int): MutableList<PaginationModel> {
        val paginationData: MutableList<PaginationModel> = mutableListOf()
        paginationData.addAll(generatePreviousPage())
        paginationData.addAll(generatePageIndex(selectedPage))
        paginationData.addAll(generateNextPage())
        return paginationData
    }

    private fun generatePreviousPage(): MutableList<PaginationModel> {
        return mutableListOf(
            PaginationModel("<<"), PaginationModel("<")
        )
    }

    private fun generateNextPage(): MutableList<PaginationModel> {
        return mutableListOf(
            PaginationModel(">"), PaginationModel(">>")
        )
    }

    private fun generatePageIndex(selectedPage: Int): MutableList<PaginationModel> {
        val paginationData: MutableList<PaginationModel> = mutableListOf()
        for (i in startPage..endPage) {
            paginationData.add(PaginationModel((i.toString()), i == selectedPage))
        }
        return paginationData
    }

    private fun AppCompatImageView.setBackgroundTintColor(type: String) {
        this.setColorFilter(
            ContextCompat.getColor(
                context, PokemonColorsUtils.getTypeColor(type)
            ), android.graphics.PorterDuff.Mode.SRC_IN
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        try {
            pagePaginationIndexSelected = 1
            pokemonCountPerPage = parent?.getItemAtPosition(pos).toString().toInt()
            pageSeparations()
            pokemonAdapter?.submitList(pokemonPagesPagination[pagePaginationIndexSelected - 1].pages[0].pokemons)
            pokemonAdapter?.notifyDataSetChanged()
            initPagination()
        } catch (ex: Exception) {
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        val spinnerPosition = spinnerAdapter?.getPosition("5")
        spinnerPosition?.let { binding?.spinnerPage?.setSelection(it) }
    }
}