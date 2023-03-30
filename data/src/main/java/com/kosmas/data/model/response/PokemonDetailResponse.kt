package com.kosmas.data.model.response

import com.google.gson.annotations.SerializedName

data class PokemonDetailResponse(
    @SerializedName("abilities") val abilities: List<Abilities>,
    @SerializedName("base_experience") val baseExperience: Int,
    @SerializedName("forms") val forms: List<Forms>,
    @SerializedName("height") val height: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("types") val types: List<Types>,
    @SerializedName("sprites") val sprites: Sprites,
    @SerializedName("stats") var stats: List<Stats>,
    @SerializedName("weight") val weight: Int,
) {

    data class Abilities(
        @SerializedName("ability") var ability: Ability,
        @SerializedName("is_hidden") var isHidden: Boolean,
        @SerializedName("slot") var slot: Int
    )

    data class Ability(
        @SerializedName("name") var name: String,
        @SerializedName("url") var url: String
    )

    data class Forms(
        @SerializedName("name") var name: String,
        @SerializedName("url") var url: String
    )

    data class Types(
        @SerializedName("slot") var slot: Int,
        @SerializedName("type") var type: Type
    )

    data class Type(
        @SerializedName("name") var name: String,
        @SerializedName("url") var url: String
    )

    data class Sprites(
        @SerializedName("back_default") var backDefault: String? = null,
        @SerializedName("back_female") var backFemale: String? = null,
        @SerializedName("back_shiny") var backShiny: String? = null,
        @SerializedName("back_shiny_female") var backShinyFemale: String? = null,
        @SerializedName("front_default") var frontDefault: String? = null,
        @SerializedName("front_female") var frontFemale: String? = null,
        @SerializedName("front_shiny") var frontShiny: String? = null,
        @SerializedName("front_shiny_female") var frontShinyFemale: String? = null
    )

    data class Stats(
        @SerializedName("base_stat") var baseStat: Int? = null,
        @SerializedName("effort") var effort: Int? = null,
        @SerializedName("stat") var stat: Stat? = Stat()

    )

    data class Stat(
        @SerializedName("name") var name: String? = null,
        @SerializedName("url") var url: String? = null
    )
}
