package com.kosmas.pokedex.model

import com.kosmas.data.model.data.PokemonData

data class PokemonModel(
    val index: Int,
    val pokemons: List<PokemonData>
)