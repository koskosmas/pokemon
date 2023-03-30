package com.kosmas.pokedex.model

data class MenuModel(
    val title: String,
    var isActive: Boolean,
    var subMenu: List<MenuModel> = arrayListOf()
)
