package com.kosmas.domain.repository

import com.kosmas.data.di.NetworkModule
import com.kosmas.data.network.service.PokemonService
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import javax.inject.Inject
import javax.inject.Named

class PokemonTypeRepositoryImpl @Inject constructor(
    @Named(NetworkModule.AUTH_API_TYPE_SERVICE) private val pokemonService: PokemonService
) : PokemonTypeRepository {

    override suspend fun fetchPokemonType(
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ) = flow {
        val response = pokemonService.fetchPokemonType()
        try {
            if (response.isSuccessful) {
                response.body()?.let { emit(it.results) }
            } else {
                onError(response.message())
            }
        } catch (ex: java.lang.Exception) {
            onError(response.message())
        }
    }.onCompletion { onComplete() }.catch {
        onError(it.message)
    }

    override suspend fun fetchPokemonByType(
        name: String,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ) = flow {
        val response = pokemonService.fetchPokemonByType(name)
        try {
            if (response.isSuccessful) {
                response.body()?.let {
                    delay(1000)
                    emit(it.results)
                }
            } else {
                onError(response.message())
            }
        } catch (ex: java.lang.Exception) {
            onError(response.message())
        }
    }.onCompletion { onComplete() }.catch {
        onError(it.message)
    }
}