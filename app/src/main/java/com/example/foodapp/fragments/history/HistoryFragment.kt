package com.example.foodapp.fragments.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.foodapp.databinding.FragmentHistoryBinding
import com.example.foodapp.utils.Resource
import com.example.foodapp.viewmodel.DonationsViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.example.foodapp.adapter.HistoryAdapter

@AndroidEntryPoint
class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private val viewModel by viewModels<DonationsViewModel>()
    private val adapter by lazy { HistoryAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.receiveRv.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.getHistory()

        }
        viewModel.history.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.submitList(it.data)
                }
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE

                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    it.string.let { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        return view
    }


}