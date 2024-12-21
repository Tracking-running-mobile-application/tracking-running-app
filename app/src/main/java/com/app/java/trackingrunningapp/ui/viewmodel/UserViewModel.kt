package com.app.java.trackingrunningapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.data.model.entity.User
import com.app.java.trackingrunningapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userLiveData = MutableLiveData<User?>()
    val userLiveData: LiveData<User?> = _userLiveData

    class Factory(
        private val userRepository: UserRepository
    ):ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(UserViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return UserViewModel(userRepository) as T
            }
            else{
                throw IllegalArgumentException("Unknown Viewmodel")
            }
        }
    }

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
