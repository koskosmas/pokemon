package com.kosmas.pokedex.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kosmas.pokedex.model.MenuModel
import com.kosmas.pokedex.databinding.ItemMenuDrawerBinding

class MenuAdapter(
    val itemOnClick: (String, Int) -> Unit, val itemSubMenuOnClick: (String, Int) -> Unit
) : ListAdapter<MenuModel, MenuAdapter.MenuDrawerViewHolder>(diffUtil) {

    override fun onBindViewHolder(holder: MenuDrawerViewHolder, position: Int) =
        holder.bindMenuDrawer(getItem(position))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuDrawerViewHolder {
        return MenuDrawerViewHolder(
            ItemMenuDrawerBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        )
    }

    inner class MenuDrawerViewHolder constructor(
        private val binding: ItemMenuDrawerBinding
    ) : ViewHolder(binding.root) {

        fun bindMenuDrawer(menu: MenuModel) {
            binding.apply {
                root.setOnClickListener {
                    itemOnClick.invoke(menu.title, adapterPosition)
                }
                tvMenuDrawer.apply {
                    text = menu.title
                    when (menu.isActive) {
                        true -> {
                            setTextColor(
                                AppCompatResources.getColorStateList(
                                    context, com.kosmas.core_ui.R.color.yellow
                                )
                            )
                            typeface = ResourcesCompat.getFont(
                                root.context,
                                com.kosmas.core_ui.R.font.poppins_bold
                            )
                        }
                        else -> {
                            setTextColor(
                                AppCompatResources.getColorStateList(
                                    context, com.kosmas.core_ui.R.color.darkgrey
                                )
                            )
                            typeface = ResourcesCompat.getFont(
                                root.context,
                                com.kosmas.core_ui.R.font.poppins_regular
                            )
                        }

                    }

                    if (menu.title == "Pokemon Type" && menu.isActive) {
                        rvSubMenu.isVisible = true
                        rvSubMenu.adapter = SubMenuAdapter { typeName, index ->
                            itemSubMenuOnClick.invoke(typeName, index)
                        }.apply {
                            submitList(menu.subMenu)
                        }
                    } else {
                        rvSubMenu.isVisible = false
                    }
                }
            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<MenuModel>() {

            override fun areItemsTheSame(oldItem: MenuModel, newItem: MenuModel): Boolean =
                oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: MenuModel, newItem: MenuModel): Boolean =
                oldItem == newItem
        }
    }
}