package com.example.foodapp.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.model.Donation
import com.example.foodapp.model.User
import com.example.foodapp.repository.MainRepository
import com.example.foodapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminHomeViewModel @Inject constructor(
    private val repository: MainRepository
): ViewModel() {

    private val _users = MutableLiveData<Resource<List<User>>>()
    val users = _users as LiveData<Resource<List<User>>>

    private val _totalDonations = MutableLiveData<Resource<Int>>()
    val totalDonations = _totalDonations as LiveData<Resource<Int>>

    private val _totalUser = MutableLiveData<Resource<Int>>()
    val totalUsers = _totalUser as LiveData<Resource<Int>>

    private val _totalDonors = MutableLiveData<Resource<Int>>()
    val totalDonors = _totalDonors as LiveData<Resource<Int>>

    private val _allDonations = MutableLiveData<Resource<List<Donation>>>()
    val allDonations = _allDonations as LiveData<Resource<List<Donation>>>

    private val _deleteUser = MutableLiveData<Resource<String>>()
    val deleteUser = _deleteUser as  LiveData<Resource<String>>

    private val _deleteDonation = MutableLiveData<Resource<String>>()
    val deleteDonation = _deleteDonation as  LiveData<Resource<String>>


    fun getAllUsers() {
        viewModelScope.launch {
            _users.value = Resource.Loading
            try {
                repository.getAllUsers { result ->
                    _users.value = result
                }
            }catch (e: Exception) {
                _users.value = Resource.Error(e.message.toString())
            }
        }
    }

    fun getAllUsersTotalNumber() {
        viewModelScope.launch {
            _totalUser.value = Resource.Loading
            try {
                repository.getAllUsersTotalNumber { result ->
                    _totalUser.value = result
                }
            }catch (e: Exception) {
                _totalUser.value = Resource.Error(e.message.toString())
            }
        }
    }

    fun getAllDonorsTotalNumber() {
        viewModelScope.launch {
            _totalDonors.value = Resource.Loading
            try {
                repository.getTotalDonors { result ->
                    _totalDonors.value = result
                }
            }catch (e: Exception) {
                _totalDonors.value = Resource.Error(e.message.toString())
            }
        }
    }

    fun getTotalDonations() {
        viewModelScope.launch {
            _totalDonations.value = Resource.Loading
            try {
                repository.getTotalDonations { result ->
                    _totalDonations.value = result
                }
            }catch (e: Exception) {
                _totalDonations.value = Resource.Error(e.message.toString())
            }
        }
    }

    fun getAllDonations() {
        viewModelScope.launch {
            _allDonations.value = Resource.Loading
            try {
                repository.getAllDonations { result ->
                    _allDonations.value = result
                }
            }catch (e: Exception) {
                _allDonations.value = Resource.Error(e.message.toString())
            }
        }
    }

    private fun deleteUser(userId: String) {
        viewModelScope.launch {
            _deleteUser.value = Resource.Loading
            try {
                repository.deleteUser(userId){
                    _deleteUser.value = it
                }
            }catch (e: Exception){
                _deleteUser.value = Resource.Error(e.message.toString())
            }
        }
    }

    private fun deleteDonation(donationId: String) {
        viewModelScope.launch {
            _deleteDonation.value = Resource.Loading
            try {
                repository.deleteDonation(donationId){
                    _deleteDonation.value = it
                }
            }catch (e: Exception){
                _deleteDonation.value = Resource.Error(e.message.toString())
            }
        }
    }



}