package com.app.java.trackingrunningapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.data.model.entity.User
import com.app.java.trackingrunningapp.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userLiveData = MutableLiveData<User?>()
    val userLiveData: LiveData<User?> = _userLiveData
    fun upsertUserInfo(
        name: String? = "",
        age: Int? = 0,
        height: Float? = 0.0f,
        weight: Double = 50.0,
        metricPreference: String? = User.UNIT_KM,
        unit: String? = User.KILOGRAM
    ) {
        viewModelScope.launch(Dispatchers.IO) {
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

     fun fetchUserInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userRepository.getUserInfo()
            if (user != null ) {
                _userLiveData.postValue(user)
                Log.d("userViewmodel","${user.height}")
            }
        }
    }
}
