package com.kosmas.pokedex.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import com.kosmas.pokedex.databinding.FragmentOnboardingBinding

class OnBoardingFragment : Fragment() {

    var introFragmentListener: IntroFragmentListener? = null
    private var binding: FragmentOnboardingBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.btnCheckPokedex?.setOnClickListener {
            introFragmentListener?.onClickCheckPokeDex()
        }
    }

    interface IntroFragmentListener {
        fun onClickCheckPokeDex()
    }
}
