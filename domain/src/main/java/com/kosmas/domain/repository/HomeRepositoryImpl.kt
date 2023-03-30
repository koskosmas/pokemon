package com.kosmas.domain.repository

import com.kosmas.data.di.NetworkModule.Companion.AUTH_API_SERVICE
import com.kosmas.data.model.data.PokemonItemData
import com.kosmas.data.network.service.PokemonService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Named

class HomeRepositoryImpl @Inject constructor(
    @Named(AUTH_API_SERVICE) private val pokemonService: PokemonService
) : HomeRepository {

    val pokemonsTemp = mutableListOf<PokemonItemData>()

    override suspend fun fetchPokemonList(
        page: Int,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ) = flow {
        val response = pokemonService.fetchPokemonList(PAGING_SIZE, page * PAGING_SIZE)
        try {
            if (response.isSuccessful) {
                response.body()?.let {
                    val pokemons = it.results
                    pokemons.forEach { pokemon ->
                        pokemon.page = page
                        pokemon.totalData = it.count
                    }
                    pokemonsTemp.addAll(pokemons)
                    val newData = pokemonsTemp.toList()
                    delay(1000)
                    emit(newData)
                }
            } else {
                onError(response.message())
            }
        } catch (ex: java.lang.Exception) {
            onError(response.message())
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.catch {
        onError(it.message)
    }

    companion object {
        private const val PAGING_SIZE = 10
    }
}