package com.kosmas.pokedex.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kosmas.pokedex.R
import com.kosmas.pokedex.databinding.ItemAbilityBinding

class AbilityAdapter : ListAdapter<String, AbilityAdapter.AbilityViewHolder>(diffUtil) {

    override fun onBindViewHolder(holder: AbilityViewHolder, position: Int) =
        holder.bindAbility(getItem(position))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbilityViewHolder {
        return AbilityViewHolder(
            ItemAbilityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    inner class AbilityViewHolder constructor(
        private val binding: ItemAbilityBinding
    ) : ViewHolder(binding.root) {

        fun bindAbility(abilityName: String) {
            binding.apply {
                tvAbility.text = String.format(
                    root.context.getString(R.string.abilities_value), abilityName
                )
            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<String>() {

            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem
        }
    }
}