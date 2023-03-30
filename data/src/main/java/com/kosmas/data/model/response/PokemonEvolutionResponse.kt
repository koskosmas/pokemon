package com.kosmas.data.model.response

import com.google.gson.annotations.SerializedName

data class PokemonEvolutionResponse(
    @SerializedName("baby_trigger_item") val baby_trigger_item: Any,
    @SerializedName("chain") val chain: Chain,
    @SerializedName("id") val id: Int
) {

    data class Chain(
        @SerializedName("evolution_details") val evolution_details: List<Any>,
        @SerializedName("evolves_to") val evolves_to: List<EvolvesTo>,
        @SerializedName("is_baby") val is_baby: Boolean,
        @SerializedName("species") val species: Species
    )

    data class EvolvesTo(
        @SerializedName("evolution_details") val evolution_details: List<EvolutionDetail>,
        @SerializedName("evolves_to") val evolves_to: List<EvolvesToX>,
        @SerializedName("is_baby") val is_baby: Boolean,
        @SerializedName("species") val species: Species
    )

    data class EvolvesToX(
        @SerializedName("evolution_details") val evolution_details: List<EvolutionDetail>,
        @SerializedName("evolves_to") val evolves_to: List<Any>,
        @SerializedName("is_baby") val is_baby: Boolean,
        @SerializedName("species") val species: Species
    )

    data class Species(
        @SerializedName("name") val name: String, @SerializedName("url") val url: String
    )

    data class EvolutionDetail(
        @SerializedName("gender") val gender: Any,
        @SerializedName("held_item") val held_item: Any,
        @SerializedName("item") val item: Any,
        @SerializedName("known_move") val known_move: Any,
        @SerializedName("known_move_type") val known_move_type: Any,
        @SerializedName("location") val location: Any,
        @SerializedName("min_affection") val min_affection: Any,
        @SerializedName("min_beauty") val min_beauty: Any,
        @SerializedName("min_happiness") val min_happiness: Any,
        @SerializedName("min_level") val min_level: Int,
        @SerializedName("needs_overworld_rain") val needs_overworld_rain: Boolean,
        @SerializedName("party_species") val party_species: Any,
        @SerializedName("party_type") val party_type: Any,
        @SerializedName("relative_physical_stats") val relative_physical_stats: Any,
        @SerializedName("time_of_day") val time_of_day: String,
        @SerializedName("trade_species") val trade_species: Any,
        @SerializedName("trigger") val trigger: Trigger,
        @SerializedName("turn_upside_down") val turn_upside_down: Boolean
    )

    data class Trigger(
        @SerializedName("name") val name: String, @SerializedName("url") val url: String
    )
}