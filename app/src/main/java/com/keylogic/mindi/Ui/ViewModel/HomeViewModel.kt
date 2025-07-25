package com.keylogic.mindi.Ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keylogic.mindi.Database.MyPreferences
import com.keylogic.mindi.Helper.CommonHelper
import com.keylogic.mindi.Helper.ProfileHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _profileName = MutableStateFlow("")
    val profileName: StateFlow<String> get() = _profileName

    private val _chipCount = MutableStateFlow("")
    val chipCount: StateFlow<String> get() = _chipCount

    private val _profileImageRes = MutableStateFlow(0)
    val profileImageRes: StateFlow<Int> get() = _profileImageRes

    fun loadProfileData(context: Context) {
        viewModelScope.launch {
            _profileName.value = ProfileHelper.profileName
            _chipCount.value = CommonHelper.INSTANCE.getTotalChip()
            _profileImageRes.value = ProfileHelper.INSTANCE.getProfileResource(context)
        }
    }

    fun addFreeChips(context: Context) {
        viewModelScope.launch {
            ProfileHelper.totalChips += ProfileHelper.freeChipCount
            _chipCount.value = CommonHelper.INSTANCE.getTotalChip()
            MyPreferences.INSTANCE.saveGameProfileDetails(context)
        }
    }
}
