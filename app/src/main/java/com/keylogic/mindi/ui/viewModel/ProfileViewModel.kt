package com.keylogic.mindi.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keylogic.mindi.helper.ProfileHelper

class ProfileViewModel : ViewModel() {

    private val _profileName = MutableLiveData<String>()
    val profileName: LiveData<String> get() = _profileName

    private val _gameWin = MutableLiveData<Int>()
    val gameWin: LiveData<Int> get() = _gameWin

    private val _gameLost = MutableLiveData<Int>()
    val gameLost: LiveData<Int> get() = _gameLost

    private val _gamePlayed = MutableLiveData<Int>()
    val gamePlayed: LiveData<Int> get() = _gamePlayed

    private val _profileImageIndex = MutableLiveData<Int>()
    val profileImageIndex: LiveData<Int> get() = _profileImageIndex

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        _profileName.value = ProfileHelper.profileName
        _gameWin.value = ProfileHelper.gameWin
        _gameLost.value = ProfileHelper.gameLost
        _gamePlayed.value = ProfileHelper.gamePlayed
        _profileImageIndex.value = if (ProfileHelper.profileId != -1) ProfileHelper.profileId else ProfileHelper.defaultProfileId
    }

    fun updateProfileName(name: String) {
        _profileName.value = name
        ProfileHelper.profileName = name
    }
}
