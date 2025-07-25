package com.keylogic.mindi.Ui.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keylogic.mindi.Helper.ProfileHelper

class ProfileSelectionViewModel : ViewModel() {

    private val _selectedProfile = MutableLiveData(0)
    val selectedProfile: LiveData<Int> get() = _selectedProfile

    private val _profileName = MutableLiveData("")
    val profileName: LiveData<String> get() = _profileName

    fun updateSelectedProfile(position: Int) {
        _selectedProfile.value = position
    }

    fun updateProfileName(name: String) {
        _profileName.value = name
    }

    fun getProfileResource(context: Context): Int {
        return ProfileHelper.INSTANCE.getDefaultProfileResource(context, _selectedProfile.value ?: 0)
    }

    fun submitProfile() {
        val name = if (_profileName.value.isNullOrBlank()) {
            ProfileHelper.INSTANCE.getRandomProfileName()
        } else {
            _profileName.value!!
        }
        ProfileHelper.profileName = name
        ProfileHelper.defaultProfileId = _selectedProfile.value ?: 0
        ProfileHelper.profileUID = ProfileHelper.INSTANCE.generateUniqueKey()
    }
}
