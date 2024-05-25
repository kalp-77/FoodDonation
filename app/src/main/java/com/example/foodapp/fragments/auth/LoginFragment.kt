package com.example.foodapp.fragments.auth
import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentLoginBinding
import com.example.foodapp.utils.CheckInternet
import com.example.foodapp.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import com.example.foodapp.fragments.auth.viewmodel.LoginViewModel
import com.example.foodapp.fragments.splash.SplashFragment
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.tasks.Task

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel : LoginViewModel by viewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    companion object {
        private const val REQUEST_CHECK_SETTINGS = 1001
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1002
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        //disable dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        (activity as AppCompatActivity).supportActionBar?.hide()



        binding.registerTv.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
        binding.forgotPasswordTv.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_resetPasswordFragment)
        }

        binding.btnLogin.setOnClickListener {
            Log.d("TAG", "login button clicked")
            val email = binding.emailTinputLayout.editText?.text.toString()
            val password = binding.passwordInputLayout.editText?.text.toString()

            when {
                email.isEmpty() -> {
                    binding.emailTinputLayout.error = "Email is required"
                    binding.emailTinputLayout.isErrorEnabled = true
                    return@setOnClickListener
                }
                password.isEmpty() -> {
                    binding.passwordInputLayout.error = "Password is required"
                    binding.passwordInputLayout.isErrorEnabled = true
                    return@setOnClickListener
                }

                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.emailTinputLayout.error = "Invalid email format"
                    binding.emailTinputLayout.isErrorEnabled = true
                    return@setOnClickListener
                }
                else -> {
                    binding.emailTinputLayout.isErrorEnabled = false
                    binding.passwordInputLayout.isErrorEnabled = false
                    binding.btnLogin.isEnabled = false

                    if (CheckInternet.isConnected(requireActivity())) {
                        //Toast.makeText(activity, "Internet is available", Toast.LENGTH_SHORT).show()
                        binding.emailTinputLayout.isEnabled = false
                        binding.passwordInputLayout.isEnabled = false
                        binding.btnLogin.isEnabled = true
                        //binding.btnLogin.text = "Loading..."
                        viewModel.login(email, password)
                        viewModel.loginRequest.observe(viewLifecycleOwner){
                            when(it){
                                is Resource.Loading -> {
                                    binding.progressCircular.isVisible = true
                                }
                                is Resource.Error -> {
                                    binding.progressCircular.isVisible = false
                                    binding.emailTinputLayout.isEnabled = true
                                    binding.passwordInputLayout.isEnabled = true
                                    Toast.makeText(requireContext(), it.string, Toast.LENGTH_SHORT).show()
                                }
                                is Resource.Success -> {
                                    binding.progressCircular.isVisible = false
                                    val result = it.data
                                    //UserType(result)
                                    val sharedPref = requireActivity().getSharedPreferences("userType", Context.MODE_PRIVATE)
                                    val editor = sharedPref.edit()
                                    editor.putString("user_type", result)
                                    editor.apply()

                                    if (result == "Organization"){
                                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                                        Toast.makeText(requireContext(), "Logged in as Organization", Toast.LENGTH_SHORT).show()
                                    }else if (result == "Restaurant"){
                                        //Navigate to Donors View
                                        findNavController().navigate(R.id.action_loginFragment_to_donorsHomeFragment)
                                        Toast.makeText(requireContext(), "Logged in as Restaurant", Toast.LENGTH_SHORT).show()
                                    }else if(result == "Admin"){
                                        //Navigate to Admin
                                        findNavController().navigate(R.id.action_loginFragment_to_adminHomeFragment)
                                        Toast.makeText(requireContext(), "Logged in as Admin", Toast.LENGTH_SHORT).show()
                                    }else{
                                        Toast.makeText(requireContext(), "You are not registered yet or an error occurred", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }



                    } else {
                        Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT)
                            .show()
                        binding.progressCircular.isVisible = false
                        binding.btnLogin.isEnabled = true
                        binding.btnLogin.text = "Login"
                    }

                }
            }
        }
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        checkLocationSettings()
    }
    private fun checkLocationSettings() {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client = LocationServices.getSettingsClient(requireContext())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            // Location settings are satisfied
            if (!isLocationPermissionGranted()) {
                requestLocationPermission()
            }
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(requireActivity(),
                        LoginFragment.REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error
                }
            } else {
                // Location settings are not satisfied
                showLocationTurnOnDialog()
            }
        }
    }
    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LoginFragment.LOCATION_PERMISSION_REQUEST_CODE
        )
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LoginFragment.LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, do nothing as we already handled it in checkLocationSettings
                } else {
                    Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLocationTurnOnDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Turn On Location")
            .setMessage("Location services are required for this app. Please turn on location.")
            .setCancelable(false)
            .setPositiveButton("Turn On") { dialog, which ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
            .setNegativeButton("Exit App") { dialog, which ->
                requireActivity().finish()
            }
            .show()
    }
}