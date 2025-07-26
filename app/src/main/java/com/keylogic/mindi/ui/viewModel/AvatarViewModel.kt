package com.keylogic.mindi.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keylogic.mindi.helper.VIPStoreHelper
import com.keylogic.mindi.models.StoreItem

class AvatarViewModel : ViewModel() {
    private val _avatars = MutableLiveData<List<StoreItem>>()
    val avatars: LiveData<List<StoreItem>> get() = _avatars

    init {
        loadAvatars()
    }

    private fun loadAvatars() {
        _avatars.value = VIPStoreHelper.avatarList
    }

    fun updateAvatars() {
        _avatars.value = VIPStoreHelper.avatarList
    }
}
