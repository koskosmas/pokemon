package com.kosmas.domain.repository

import com.kosmas.domain.model.mapper.DataMapper.mapToDomainModel
import com.kosmas.data.network.service.PokemonService
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(
    private val pokemonService: PokemonService
) : DetailRepository, BaseRepository() {

    override suspend fun fetchPokemonDetail(
        name: String,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ) = flow {
        val response = pokemonService.fetchPokemonDetail(name = name)
        try {
            if (response.isSuccessful) {
                response.body()?.let { emit(it.mapToDomainModel()) }
            } else {
                onError(response.message())
            }
        } catch (ex: java.lang.Exception) {
            onError(response.message())
        }
    }.catch {
        onError(it.message)
    }.onCompletion { onComplete() }

    override suspend fun fetchPokemonEvolution(
        id: Int,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ) = flow {
        val response = pokemonService.fetchPokemonEvolution(id)
        try {
            if (response.isSuccessful) {
                response.body()?.let { emit(it.mapToDomainModel()) }
            } else {
                onError(response.message())
            }
        } catch (ex: java.lang.Exception) {
            onError(response.message())
        }
    }.catch {
        onError(it.message)
    }.onCompletion { onComplete() }
}
