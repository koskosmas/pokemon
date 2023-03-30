package com.kosmas.pokedex.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kosmas.pokedex.R
import com.kosmas.pokedex.databinding.ItemDrawerSubmenuBinding
import com.kosmas.pokedex.model.MenuModel
import com.kosmas.core_ui.utils.PokemonColorsUtils

class SubMenuAdapter(val onItemClick: (String, Int) -> Unit) :
    ListAdapter<MenuModel, SubMenuAdapter.SubMenuViewHolder>(diffUtil) {

    override fun onBindViewHolder(holder: SubMenuViewHolder, position: Int) =
        holder.bindSubMenu(getItem(position), position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubMenuViewHolder {
        return SubMenuViewHolder(
            ItemDrawerSubmenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    inner class SubMenuViewHolder constructor(
        private val binding: ItemDrawerSubmenuBinding
    ) : ViewHolder(binding.root) {

        fun bindSubMenu(menu: MenuModel, position: Int) {
            binding.tvMenuDrawer.apply {
                text = menu.title
                typeface = if (menu.isActive) {
                    setTextColor(
                        AppCompatResources.getColorStateList(
                            context,
                            PokemonColorsUtils.getTypeColor(menu.title)
                        )
                    )
                    ResourcesCompat.getFont(context, com.kosmas.core_ui.R.font.poppins_bold)
                } else {
                    setTextColor(AppCompatResources.getColorStateList(context, com.kosmas.core_ui.R.color.darkgrey))
                    ResourcesCompat.getFont(context, com.kosmas.core_ui.R.font.poppins_regular)
                }
            }

            binding.root.setOnClickListener {
                onItemClick(menu.title, position)
            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<MenuModel>() {

            override fun areItemsTheSame(oldItem: MenuModel, newItem: MenuModel): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: MenuModel, newItem: MenuModel): Boolean =
                oldItem == newItem
        }
    }
}