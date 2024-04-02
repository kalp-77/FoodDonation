package com.example.foodapp.fragments.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.foodapp.databinding.FragmentAboutUsBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AboutUsFragment : DialogFragment() {
    private lateinit var binding: FragmentAboutUsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAboutUsBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

}