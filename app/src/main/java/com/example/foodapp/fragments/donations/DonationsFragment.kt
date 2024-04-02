package com.example.foodapp.fragments.donations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.foodapp.databinding.FragmentDonationsBinding
import com.example.foodapp.utils.Resource
import com.example.foodapp.viewmodel.DonationsViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.example.foodapp.adapter.DonationsAdapter


@AndroidEntryPoint
class DonationsFragment : Fragment() {
    private lateinit var binding: FragmentDonationsBinding
    private val viewModel: DonationsViewModel by viewModels()
    private val adapter by lazy { DonationsAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDonationsBinding.inflate(inflater, container, false)
        val view = binding.root


        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.getDonations()
        }

        viewModel.donations.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> {
                    binding.progressCircular.isVisible = true
                }
                is Resource.Success -> {
                    binding.progressCircular.isVisible = false
                    adapter.submitList(state.data)
                }
                is Resource.Error -> {
                    binding.progressCircular.isVisible = false
                    Toast.makeText(requireContext(), "An error occurred", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }

}