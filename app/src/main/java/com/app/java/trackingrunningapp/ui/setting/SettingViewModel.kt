package com.app.java.trackingrunningapp.ui.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingViewModel:ViewModel() {
    private val _selectedLanguage = MutableLiveData<String>()
    val selectedLanguage = _selectedLanguage

    fun updateLanguage(lang:String){
        _selectedLanguage.postValue(lang)
    }
}