package com.keylogic.mindi.Ui.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keylogic.mindi.Helper.VIPStoreHelper
import com.keylogic.mindi.Models.Background

class BackgroundViewModel : ViewModel() {
    private val _backgrounds = MutableLiveData<List<Background>>()
    val backgrounds: LiveData<List<Background>> get() = _backgrounds

    init {
        loadBackgrounds()
    }

    private fun loadBackgrounds() {
        _backgrounds.value = VIPStoreHelper.backgroundList
    }
}
