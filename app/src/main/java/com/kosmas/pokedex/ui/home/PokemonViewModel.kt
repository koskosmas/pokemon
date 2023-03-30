package com.kosmas.pokedex.ui.home

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.kosmas.data.model.data.PokemonItemData
import com.kosmas.domain.repository.DetailRepository
import com.kosmas.domain.repository.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val repository: HomeRepository,
    private val detailRepository: DetailRepository
) : ViewModel() {

    var isLoading = MutableLiveData(false)
    var errorMessage = MutableLiveData<String>(null)
    val pokemonItemDataList = MutableLiveData<List<PokemonItemData>>(emptyList())

    private val pokemonFetchingIndex: MutableStateFlow<Int> = MutableStateFlow(0)

    fun fetchPokemons() {
        viewModelScope.launch {
            repository.fetchPokemonList(
                page = pokemonFetchingIndex.value,
                onStart = { isLoading.postValue(true) },
                onComplete = { isLoading.postValue(false) },
                onError = { errorMessage.postValue(it) }
            ).collect {
                val pokemons = it
                for (i in pokemons.indices) {
                    detailRepository.fetchPokemonDetail(
                        pokemons[i].name,
                        onComplete = { isLoading.postValue(false) },
                        onError = { errorMessage.postValue(it) }
                    ).collect { pokemon ->
                        pokemons[i].types = pokemon.types
                    }
                }
                pokemonItemDataList.postValue(pokemons)
            }
        }
    }

    @MainThread
    fun fetchNextPokemonList() {
        if (isLoading.value != true) {
            pokemonFetchingIndex.value++
            fetchPokemons()
        }
    }
}
