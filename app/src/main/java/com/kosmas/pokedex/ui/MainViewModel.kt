package com.kosmas.pokedex.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.kosmas.domain.repository.PokemonTypeRepository
import com.kosmas.pokedex.model.MenuModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val pokemonTypeRepository: PokemonTypeRepository
) : ViewModel() {

    var isLoading = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()
    var menus = MutableLiveData(
        listOf(
            MenuModel("Home", true), MenuModel("Pokemon Type", false)
        )
    )

    fun fetchPokemonTypes(){
        viewModelScope.launch {
            pokemonTypeRepository.fetchPokemonType(onComplete = { isLoading.postValue(false) },
                onError = { errorMessage.postValue(it) }).collectLatest {
                menus.value?.get(1)?.subMenu = listOf()
                menus.value?.get(1)?.subMenu = mutableListOf<MenuModel>().apply {
                    it.forEach {
                        this.add(MenuModel(it.name, false))
                    }
                }
            }
        }
    }

    fun resetMenuState(title: String) {
        menus.value?.forEach {
            it.isActive = it.title == title
        }
    }

    fun resetSubMenuState(title: String) {
        menus.value?.get(1)?.subMenu?.forEach {
            it.isActive = it.title == title
        }
    }
}