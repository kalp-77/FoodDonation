package com.example.foodapp.fragments.splash

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.foodapp.databinding.FragmentSplashBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    @Inject lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        val view = binding.root
        //(activity as AppCompatActivity).supportActionBar?.hide()
        /*activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN*/

        val user = auth.currentUser

        Handler(Looper.getMainLooper()).postDelayed({
            if(isAdded) {
                if (user != null) {
                    val action = SplashFragmentDirections.actionSplashFragmentToHomeFragment()
                    findNavController().navigate(action)
                    // findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                    val sharedPrefs =
                        requireActivity().getSharedPreferences("userType", Context.MODE_PRIVATE)
                    val userType = sharedPrefs.getString("user_type", null)
                    if (userType == "Admin") {
                        val action =
                            SplashFragmentDirections.actionSplashFragmentToAdminHomeFragment()
                        findNavController().navigate(action)

                    } else if (userType == "Organization") {
                        val action = SplashFragmentDirections.actionSplashFragmentToHomeFragment()
                        findNavController().navigate(action)

                    } else if (userType == "Restaurant") {
                        val action =
                            SplashFragmentDirections.actionSplashFragmentToDonorsHomeFragment()
                        findNavController().navigate(action)

                    } else {
                        val action =
                            SplashFragmentDirections.actionSplashFragmentToLoginFragment()
                        findNavController().navigate(action)
                    }

                } else {
                    val action = SplashFragmentDirections.actionSplashFragmentToLoginFragment()
                    findNavController().navigate(action)
                    //findNavController().navigate(R.id.action_splashFragment_to_loginFragment)

                }
            }
        }, 5000)


        return view
    }
}