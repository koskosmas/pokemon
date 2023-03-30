package com.kosmas.data.model.data

import com.google.gson.annotations.SerializedName

data class PokemonData(
    val pokemonIndex: Int,
    @SerializedName("pokemon") val pokemonItemData: PokemonItemData
)