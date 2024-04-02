package com.example.foodapp.fragments.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.repository.AuthRepository
import com.example.foodapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {
    private val _loginRequest = MutableLiveData<Resource<String>>()
    val loginRequest = _loginRequest as LiveData<Resource<String>>

    fun login(email: String, password: String){
        viewModelScope.launch {
            _loginRequest.value = Resource.Loading
            try {
                repository.login(email, password){
                    _loginRequest.value = it
                }
            }catch (e: Exception){
                _loginRequest.value = Resource.Error(e.message.toString())
            }
        }
    }
}