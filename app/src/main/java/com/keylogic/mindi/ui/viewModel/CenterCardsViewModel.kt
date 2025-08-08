package com.keylogic.mindi.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keylogic.mindi.gamePlay.models.Card

class CenterCardsViewModel : ViewModel() {

    private val _centerCards = MutableLiveData(Pair(0,Card()))
    val centerCards: LiveData<Pair<Int, Card>> get() = _centerCards

    fun updateCenterCards(pair: Pair<Int, Card>) {
        _centerCards.value = pair
    }
}