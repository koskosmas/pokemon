package com.kosmas.data.model.response

import com.google.gson.annotations.SerializedName
import com.kosmas.data.model.data.PokemonData

data class PokemonNameResponse(
    @SerializedName("pokemon")
    val results: List<PokemonData>
)