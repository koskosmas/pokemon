package com.kosmas.domain.model

data class PokemonDetailDomainModel(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val abilities: List<String>,
    val types: List<String>,
    val sprites: List<String>,
    val statDomainModels: List<StatDomainModel>,
    val evolutionDomainModels: MutableList<EvolutionDomainModel> = ArrayList()
) {

    fun getWeight() = String.format("%.1f KG", weight.toFloat() / 10)
    fun getHeight() = String.format("%.1f M", height.toFloat() / 10)
    fun getImageUrl() =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
}