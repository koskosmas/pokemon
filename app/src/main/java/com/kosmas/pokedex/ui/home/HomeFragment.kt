package com.kosmas.pokedex.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.kosmas.pokedex.R
import com.kosmas.pokedex.databinding.FragmentHomeBinding
import com.kosmas.pokedex.ui.MainActivity

class HomeFragment : Fragment(), OnBoardingFragment.IntroFragmentListener {

    var binding: FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            viewPager.isUserInputEnabled = false
            viewPager.adapter = ScreenSlidePagerAdapter(
                requireActivity()
            )
            Glide.with(this@HomeFragment).load(R.drawable.ic_background_home)
                .into(imgBackgroundHome)
        }
    }

    private inner class ScreenSlidePagerAdapter(
        fa: FragmentActivity
    ) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment =
            if (position == 0) OnBoardingFragment().apply {
                introFragmentListener = this@HomeFragment
            } else PokemonsFragment()

    }

    override fun onClickCheckPokeDex() {
        binding?.viewPager?.setCurrentItem(1, true)
    }

    override fun onResume() {
        super.onResume()

        if (isAdded) {
            (activity as MainActivity).resetMenuStateHome()
        }
    }
}