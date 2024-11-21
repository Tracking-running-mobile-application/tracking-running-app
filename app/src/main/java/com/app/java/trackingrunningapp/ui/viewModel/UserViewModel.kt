package com.app.java.trackingrunningapp.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.ui.data.entities.User
import com.app.java.trackingrunningapp.ui.data.repositories.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    fun upsertUserInfo(
        name: String?,
        age: Int?,
        height: Float?,
        weight: Float = 50f,
        metricPreference: String? = User.kilogram,
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
        }
    }
}
