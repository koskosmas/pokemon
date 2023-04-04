package com.kosmas.domain.model

data class PokemonEvolutionDomainModel(val id: Int = 0, val evolutions: List<Species>) {
    data class Species(
        val name: String,
        val url: String
    )
}