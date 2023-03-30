package com.kosmas.domain.repository

import com.kosmas.data.model.data.PokemonItemData
import com.kosmas.data.model.data.PokemonData
import kotlinx.coroutines.flow.Flow

interface PokemonTypeRepository {
    suspend fun fetchPokemonType(
        onComplete: () -> Unit, onError: (String?) -> Unit
    ): Flow<List<PokemonItemData>>

    suspend fun fetchPokemonByType(
        name: String, onComplete: () -> Unit, onError: (String?) -> Unit
    ): Flow<List<PokemonData>>
}