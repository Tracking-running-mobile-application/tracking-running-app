package com.app.java.trackingrunningapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.java.trackingrunningapp.data.repository.PersonalGoalRepository
import com.app.java.trackingrunningapp.data.repository.RunSessionRepository

class PersonalGoalViewModelFactory(
    private val personalGoalRepository: PersonalGoalRepository,
    private val runSessionRepository: RunSessionRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PersonalGoalViewModel::class.java)) {
            return PersonalGoalViewModel(personalGoalRepository, runSessionRepository) as T
        }
        throw IllegalArgumentException ("Unknown ViewModel class ${modelClass.name}")
    }
}