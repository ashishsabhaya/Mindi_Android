package com.keylogic.mindi.Ui.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keylogic.mindi.Helper.VIPStoreHelper
import com.keylogic.mindi.Models.Avatar

class AvatarViewModel : ViewModel() {
    private val _avatars = MutableLiveData<List<Avatar>>()
    val avatars: LiveData<List<Avatar>> get() = _avatars

    init {
        loadAvatars()
    }

    private fun loadAvatars() {
        _avatars.value = VIPStoreHelper.avatarList
    }
}
