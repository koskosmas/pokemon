package com.kosmas.data.network.service

import com.kosmas.data.model.response.PokemonEvolutionResponse
import com.kosmas.data.model.response.PokemonDetailResponse
import com.kosmas.data.model.response.PokemonNameResponse
import com.kosmas.data.model.response.PokemonResponse
import com.kosmas.data.model.response.PokemonTypeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonService {

    @GET("pokemon")
    suspend fun fetchPokemonList(
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0
    ): Response<PokemonResponse>

    @GET("pokemon/{name}")
    suspend fun fetchPokemonDetail(
        @Path("name") name: String
    ): Response<PokemonDetailResponse>

    @GET("type")
    suspend fun fetchPokemonType(): Response<PokemonTypeResponse>

    @GET("type/{name}")
    suspend fun fetchPokemonByType(@Path("name") name: String): Response<PokemonNameResponse>

    @GET("evolution-chain/{id}")
    suspend fun fetchPokemonEvolution(
        @Path("id") id: Int
    ): Response<PokemonEvolutionResponse>
}