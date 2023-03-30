package com.kosmas.domain.repository

import com.kosmas.domain.model.PokemonDetailDomainModel
import com.kosmas.domain.model.PokemonEvolutionDomainModel
import kotlinx.coroutines.flow.Flow

interface DetailRepository {
    suspend fun fetchPokemonDetail(
        name: String,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ): Flow<PokemonDetailDomainModel>

    suspend fun fetchPokemonEvolution(
        id: Int,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ): Flow<PokemonEvolutionDomainModel>
}