package com.app.java.trackingrunningapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.model.entities.User
import com.app.java.trackingrunningapp.model.repositories.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userLiveData = MutableLiveData<User?>()
    val userLiveData: LiveData<User?> = _userLiveData


    fun upsertUserInfo(
        name: String?,
        age: Int?,
        height: Float?,
        weight: Double = 50.0,
        metricPreference: String? = User.KILOGRAM,
        unit: String? = User.UNIT_KM
    ) {
        viewModelScope.launch {
            val user = User(
                userId = 1,
                name = name,
                age = age,
                height = height,
                weight = weight,
                metricPreference = metricPreference,
                unit = unit
            )
            userRepository.upsertUserInfo(user)
            fetchUserInfo()
        }
    }

    private fun fetchUserInfo() {
        viewModelScope.launch {
            val user = userRepository.getUserInfo()
            if (user != null ) {
                _userLiveData.postValue(user)
            }
        }
    }
}
