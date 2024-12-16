package com.app.java.trackingrunningapp.ui.intro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Delete
import com.app.java.trackingrunningapp.data.model.entity.user.User
import com.app.java.trackingrunningapp.data.repository.UserRepository

class UserViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    suspend fun insertOneUser(user: User) {
        userRepository.insertOneUser(user)
    }
    suspend fun getUserById(id: Int): User? {
        val newUser = userRepository.getUserById(id)
        _user.postValue(newUser)
        return newUser
    }

    suspend fun updateUserInfo(user:User){
         userRepository.updateUserInfo(user)
    }
    @Delete
    suspend fun deleteUser(user: User){
         userRepository.deleteUser(user)
    }
    class Factory(
        private val userRepository: UserRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UserViewModel(userRepository) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel")
            }
        }
    }
}
