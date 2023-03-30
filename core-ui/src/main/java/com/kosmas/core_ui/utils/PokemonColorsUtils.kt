package com.kosmas.core_ui.utils

import com.kosmas.core_ui.R

object PokemonColorsUtils {

    fun getStatColor(stat: String): Int {
        return when (stat.lowercase()) {
            "attack" -> R.color.water
            "defense" -> R.color.md_orange_100
            "hp" -> R.color.fire
            "special-attack" -> R.color.yellow
            "special-defense" -> R.color.grass
            else -> R.color.ghost
        }
    }

    fun getTypeColor(type: String): Int {
        return when (type.lowercase()) {
            "bug" -> R.color.bug
            "dark" -> R.color.dark
            "dragon" -> R.color.dragon
            "electric" -> R.color.electric
            "fairy" -> R.color.fairy
            "fighting" -> R.color.fighting
            "fire" -> R.color.fire
            "flying" -> R.color.flying
            "ice" -> R.color.ice
            "ghost" -> R.color.ghost
            "grass" -> R.color.grass
            "ground" -> R.color.ground
            "poison" -> R.color.poison
            "psychic" -> R.color.psychic
            "rock" -> R.color.rock
            "steel" -> R.color.steel
            "water" -> R.color.water
            else -> R.color.darkgrey
        }
    }
}
