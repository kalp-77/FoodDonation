package com.example.foodapp.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentDonorsHomeBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DonorsHomeFragment : Fragment() {
    private lateinit var binding: FragmentDonorsHomeBinding
    @Inject lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDonorsHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        (activity as AppCompatActivity).supportActionBar?.show()
        //(activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        binding.cardLogout.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_donorsHomeFragment_to_loginFragment)
        }
        binding.cardDonate.setOnClickListener {
            findNavController().navigate(R.id.action_donorsHomeFragment_to_donateFragment)
        }
        binding.cardHistory.setOnClickListener {
            findNavController().navigate(R.id.action_donorsHomeFragment_to_historyFragment)
        }
        binding.cardAboutus.setOnClickListener {
            findNavController().navigate(R.id.action_donorsHomeFragment_to_aboutUsFragment)
        }
        binding.cardContact.setOnClickListener {
            findNavController().navigate(R.id.action_donorsHomeFragment_to_contactUsFragment)
        }





        return view
    }

}