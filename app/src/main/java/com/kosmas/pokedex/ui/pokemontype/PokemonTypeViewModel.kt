package com.kosmas.pokedex.ui.pokemontype

import androidx.lifecycle.*
import com.kosmas.domain.repository.DetailRepository
import com.kosmas.domain.repository.PokemonTypeRepository
import com.kosmas.data.model.data.PokemonData
import com.kosmas.pokedex.model.BaseResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonTypeViewModel @Inject constructor(
    private val repository: PokemonTypeRepository,
    private val detailRepository: DetailRepository
) : ViewModel() {

    var isLoading = MutableLiveData(true)
    var errorMessage = MutableLiveData<String>(null)
    val pokemon = MutableLiveData<BaseResource<List<PokemonData>>>()
    var pokemonType: String = ""

    fun getPokemonType(pokemonTypeTemp: String) {
        pokemonType = pokemonTypeTemp
        viewModelScope.launch {
            repository.fetchPokemonByType(
                pokemonTypeTemp,
                onComplete = { isLoading.postValue(false) },
                onError = { errorMessage.postValue(it) }
            ).collect {
                val pokemons = it
                for (i in pokemons.indices) {
                    detailRepository.fetchPokemonDetail(
                        pokemons[i].pokemonItemData.name,
                        onComplete = { isLoading.postValue(false) },
                        onError = { errorMessage.postValue(it) }
                    ).collect { pokemon ->
                        pokemons[i].pokemonItemData.types = pokemon.types
                    }
                }
                pokemon.postValue(BaseResource.Success(pokemons))
            }
        }
    }
}