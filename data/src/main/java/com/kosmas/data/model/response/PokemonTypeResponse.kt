package com.kosmas.data.model.response

import com.google.gson.annotations.SerializedName
import com.kosmas.data.model.data.PokemonItemData

data class PokemonTypeResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("results") val results: List<PokemonItemData>
)

