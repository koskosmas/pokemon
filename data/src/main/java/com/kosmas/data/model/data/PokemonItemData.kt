package com.kosmas.data.model.data

import com.google.gson.annotations.SerializedName

data class PokemonItemData(
    var totalData: Int = 0,
    var page: Int = 0,
    var types: List<String> = listOf(),
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
) {

    fun getImageUrl(): String {
        val index = getId()
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$index.png"
    }

    fun getId(): String = url.split("/".toRegex()).dropLast(1).last()
}
