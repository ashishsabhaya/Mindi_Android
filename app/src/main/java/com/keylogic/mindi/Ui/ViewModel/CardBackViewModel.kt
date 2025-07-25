package com.keylogic.mindi.Ui.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keylogic.mindi.Helper.VIPStoreHelper
import com.keylogic.mindi.Models.StoreItem

class CardBackViewModel : ViewModel() {
    private val _cardBacks = MutableLiveData<List<StoreItem>>()
    val cardBacks: LiveData<List<StoreItem>> get() = _cardBacks

    init {
        loadCardBacks()
    }

    private fun loadCardBacks() {
        _cardBacks.value = VIPStoreHelper.cardBackList
    }

    fun updateCardBacks() {
        _cardBacks.value = VIPStoreHelper.cardBackList
    }
}
