package com.kosmas.domain.model.mapper

import com.kosmas.data.model.response.PokemonDetailResponse
import com.kosmas.data.model.response.PokemonEvolutionResponse
import com.kosmas.domain.model.PokemonDetailDomainModel
import com.kosmas.domain.model.PokemonEvolutionDomainModel
import com.kosmas.domain.model.StatDomainModel

object DataMapper {

    fun PokemonEvolutionResponse.mapToDomainModel(): PokemonEvolutionDomainModel {
        return try {
            PokemonEvolutionDomainModel(
                this.id,
                mutableListOf<PokemonEvolutionDomainModel.Species>().apply {
                this.add(
                    PokemonEvolutionDomainModel.Species(
                        chain.species.name, getImageUrl(chain.species.url)
                    )
                )
                this.add(
                    PokemonEvolutionDomainModel.Species(
                        chain.evolves_to[0].species.name,
                        getImageUrl(chain.evolves_to[0].species.url)
                    )
                )
                this.add(
                    PokemonEvolutionDomainModel.Species(
                        chain.evolves_to[0].evolves_to[0].species.name,
                        getImageUrl(chain.evolves_to[0].evolves_to[0].species.url)
                    )
                )
            })
        } catch (ex: java.lang.Exception) {
            PokemonEvolutionDomainModel(0, emptyList())
        }
    }

    fun PokemonDetailResponse.mapToDomainModel(): PokemonDetailDomainModel =
        PokemonDetailDomainModel(this.id, this.name, this.height, this.weight, mutableListOf<String>().apply {
            this@mapToDomainModel.abilities.forEach {
                this.add(it.ability.name)
            }
        }.toList(), mutableListOf<String>().apply {
            types.forEach {
                this.add(it.type.name)
            }
        }, mutableListOf<String>().apply {
            this@mapToDomainModel.sprites.backDefault?.let {
                this.add(it)
            }
            this@mapToDomainModel.sprites.backFemale?.let {
                this.add(it)
            }
            this@mapToDomainModel.sprites.backShiny?.let {
                this.add(it)
            }
            this@mapToDomainModel.sprites.backShinyFemale?.let {
                this.add(it)
            }
            this@mapToDomainModel.sprites.frontDefault?.let {
                this.add(it)
            }
            this@mapToDomainModel.sprites.frontFemale?.let {
                this.add(it)
            }
            this@mapToDomainModel.sprites.frontShiny?.let {
                this.add(it)
            }
            this@mapToDomainModel.sprites.frontShinyFemale?.let {
                this.add(it)
            }
        }, mutableListOf<StatDomainModel>().apply {
            this@mapToDomainModel.stats.forEach {
                this.add(StatDomainModel(it.stat?.name.orEmpty(), it.baseStat.toString()))
            }
        })

    private fun getImageUrl(url: String): String {
        val index = getId(url)
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$index.png"
    }

    private fun getId(url: String): String = url.split("/".toRegex()).dropLast(1).last()
}