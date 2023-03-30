package com.kosmas.pokedex.ui.pokemontype

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kosmas.data.model.data.PokemonData
import com.kosmas.pokedex.databinding.ItemTableBinding
import com.kosmas.pokedex.ui.detail.TypeAdapter

class PokemonAdapter(val onItemClick: (String) -> Unit, val onTypeClick: (String) -> Unit) :
    ListAdapter<PokemonData, PokemonAdapter.PokemonListViewHolder>(diffUtil) {

    override fun onBindViewHolder(holder: PokemonListViewHolder, position: Int) =
        holder.bindPokemonData(getItem(position))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonListViewHolder {
        return PokemonListViewHolder(
            ItemTableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    inner class PokemonListViewHolder constructor(
        private val binding: ItemTableBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindPokemonData(pokemonData: PokemonData) {
            binding.apply {
                root.setOnClickListener {
                    onItemClick.invoke(pokemonData.pokemonItemData.name)
                }
                Glide.with(root.context).load(pokemonData.pokemonItemData.getImageUrl()).into(ivImage)
                tvPokedexId.text = "# ${pokemonData.pokemonItemData.getId()}"
                tvPokedexName.text = pokemonData.pokemonItemData.name
                rvTypes.adapter = TypeAdapter{
                    onTypeClick.invoke(it)
                }.apply {
                    submitList(pokemonData.pokemonItemData.types)
                }
            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<PokemonData>() {

            override fun areItemsTheSame(oldItem: PokemonData, newItem: PokemonData): Boolean =
                oldItem.pokemonItemData.name == newItem.pokemonItemData.name


            override fun areContentsTheSame(oldItem: PokemonData, newItem: PokemonData): Boolean =
                oldItem == newItem
        }
    }
}