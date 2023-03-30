package com.kosmas.pokedex.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosmas.domain.model.EvolutionDomainModel
import com.kosmas.domain.model.PokemonDetailDomainModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val detailRepository: com.kosmas.domain.repository.DetailRepository
) : ViewModel() {

    var isLoading = MutableLiveData(true)
    var errorMessage = MutableLiveData<String?>(null)
    var pokemonDetailDomainModel = MutableLiveData<PokemonDetailDomainModel>()

    fun fetchPokemonDetail(pokemonName: String) {
        viewModelScope.launch {
            detailRepository.fetchPokemonDetail(
                name = pokemonName,
                onComplete = { isLoading.postValue(false) },
                onError = { errorMessage.postValue(it) }
            ).collect { it ->
                it.evolutionDomainModels.add(
                    EvolutionDomainModel(it.name, it.getImageUrl())
                )
                detailRepository.fetchPokemonEvolution(
                    id = it.id,
                    onComplete = { isLoading.postValue(false) },
                    onError = { errorMessage.postValue(it) }
                ).collect { evolutionData ->
                    for (evolution in evolutionData.evolutions) {
                        it.evolutionDomainModels.add(EvolutionDomainModel(evolution.name, evolution.url))
                    }
                    pokemonDetailDomainModel.postValue(it)
                }
            }
        }
    }
}