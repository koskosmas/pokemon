package com.kosmas.pokedex.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.kosmas.core_ui.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import com.kosmas.pokedex.R
import com.kosmas.pokedex.databinding.ActivityMainBinding
import com.kosmas.pokedex.model.MenuModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.fetchPokemonTypes()
        initObserver()
    }

    private fun initObserver() {
        viewModel.menus.observe(this) { data -> initView(data) }
        viewModel.errorMessage.observe(this) {
            it?.let { showMessage("Error", it) }
        }
    }

    private fun showMessage(title: String, message: String) {
        binding.root.showSnackBar(title, message)
    }

    private fun initView(data: List<MenuModel>) {
        binding.apply {
            initAdapter(data)
            toolbar.btnOpenDrawer.setOnClickListener {
                toggleShowDrawer(drawerLayout)
            }
            onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                        toggleShowDrawer(drawerLayout)
                    } else {
                        isEnabled = false
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
            })
        }
    }

    private fun initAdapter(data: List<MenuModel>) {
        binding.apply {
            val drawerLayout: DrawerLayout = drawerLayout
            val navView: NavigationView = navView
            val navController = findNavController(R.id.nav_host_fragment_content_main)
            navView.setupWithNavController(navController)

            rvMenu.adapter = MenuAdapter(itemOnClick = { titleMenuDrawer, _ ->
                viewModel.resetMenuState(titleMenuDrawer)
                rvMenu.adapter?.notifyDataSetChanged()
                toggleShowDrawer(drawerLayout)

                if (titleMenuDrawer == "Home") {
                    navigateToHome(navController)
                } else {
                    navigateToPokemonType(navController, titleMenuDrawer)
                }
            }, itemSubMenuOnClick = { titleMenuDrawer, _ ->
                viewModel.resetSubMenuState(titleMenuDrawer)
                rvMenu.adapter?.notifyDataSetChanged()
                toggleShowDrawer(drawerLayout)

                navigateToPokemonType(navController, titleMenuDrawer)
            }).apply { submitList(data) }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun resetMenuStateHome() {
        viewModel.resetMenuState("Home")
        binding.rvMenu.adapter?.notifyDataSetChanged()
    }

    private fun navigateToPokemonType(navController: NavController, titleMenuDrawer: String) {
        navigateToHome(navController)
        val args = if (titleMenuDrawer != "Pokemon Type") {
            Bundle().also {
                it.putString("pokemonType", titleMenuDrawer)
            }
        } else {
            Bundle().also {
                viewModel.resetSubMenuState("normal")
                it.putString(
                    "pokemonType",
                    "normal"
                )
            }
        }
        navController.navigate(R.id.action_homeFragment_to_pokemonTypeFragment, args)
    }

    private fun navigateToHome(navController: NavController){
        when (navController.currentDestination?.id) {
            R.id.detailFragment -> {
                navController.navigate(R.id.action_detailFragment_to_homeFragment)
            }
            R.id.detailDialogFragment -> {
                navController.navigate(R.id.action_detailDialogFragment_to_homeFragment)

            }
            R.id.pokemonTypeFragment -> {
                navController.navigate(R.id.action_pokemonTypeFragment_to_homeFragment)
            }
        }
    }

    private fun toggleShowDrawer(drawerLayout: DrawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END)
        } else {
            drawerLayout.openDrawer(GravityCompat.END)
            val btnCloseDrawer = drawerLayout.findViewById<ImageView>(R.id.btn_close_drawer)
            val imageLogo = drawerLayout.findViewById<ImageView>(R.id.img_logo_drawer)
            Glide.with(this).load(R.drawable.ic_logo).into(imageLogo)
            btnCloseDrawer.setOnClickListener {
                toggleShowDrawer(drawerLayout)
            }
        }
    }
}