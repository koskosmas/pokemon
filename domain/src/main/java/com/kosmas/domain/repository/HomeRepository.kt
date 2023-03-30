package com.kosmas.domain.repository

import com.kosmas.data.model.data.PokemonItemData
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun fetchPokemonList(
        page: Int,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ): Flow<List<PokemonItemData>>
}