package com.app.java.trackingrunningapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.data.model.entity.goal.PersonalGoal
import com.app.java.trackingrunningapp.data.repository.PersonalGoalRepository
import com.app.java.trackingrunningapp.data.repository.RunSessionRepository
import com.app.java.trackingrunningapp.ui.statistic.StatisticViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PersonalGoalViewModel(
    private val personalGoalRepository: PersonalGoalRepository
): ViewModel() {

    private val _personalGoals = MutableLiveData<List<PersonalGoal>>()
    val personalGoals:LiveData<List<PersonalGoal>> = _personalGoals

    private val _goalProgress = MutableStateFlow<Double?>(null)
    val goalProgress: LiveData<Double?> = _goalProgress.asLiveData()

    fun loadAllPersonalGoals(){
        _personalGoals.postValue(personalGoalRepository.getAllPersonalGoals())
    }
    fun deletePersonalGoal(goal: PersonalGoal) {
        viewModelScope.launch {
            personalGoalRepository.deletePersonalGoal(goal)
        }
    }

    class Factory(
        private val personalGoalRepository: PersonalGoalRepository,
        private val runSessionRepository: RunSessionRepository
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StatisticViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PersonalGoalViewModel(personalGoalRepository) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel")
            }
        }
    }
}
