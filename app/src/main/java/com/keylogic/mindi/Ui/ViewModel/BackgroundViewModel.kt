package com.keylogic.mindi.Ui.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keylogic.mindi.Helper.VIPStoreHelper
import com.keylogic.mindi.Models.StoreItem

class BackgroundViewModel : ViewModel() {
    private val _backgrounds = MutableLiveData<List<StoreItem>>()
    val backgrounds: LiveData<List<StoreItem>> get() = _backgrounds

    init {
        loadBackgrounds()
    }

    private fun loadBackgrounds() {
        _backgrounds.value = VIPStoreHelper.backgroundList
    }

    fun updateBackgrounds() {
        _backgrounds.value = VIPStoreHelper.backgroundList
    }

}
