package com.keylogic.mindi.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keylogic.mindi.gamePlay.helper.GameHelper
import com.keylogic.mindi.gamePlay.models.Card

class PlayerCardsViewModel : ViewModel() {

    private val _playerCards = MutableLiveData<List<Card>>()
    val playerCards: LiveData<List<Card>> = _playerCards

    fun initializeList() {
        _playerCards.value = GameHelper.getCurrentPlayer().playerGameDetails.cardList.toList()
    }

    fun addCard(card: Card) {
        val updatedList = _playerCards.value.orEmpty().toMutableList()
        updatedList.add(card)
        _playerCards.value = updatedList
    }

    fun removeCard(card: Card) {
        val updatedList = _playerCards.value.orEmpty().toMutableList()
        for ((index, listCard) in updatedList.withIndex()) {
            if (listCard.getTag() == card.getTag()) {
                updatedList.removeAt(index)
                break
            }
        }
        _playerCards.value = updatedList
    }

    fun updateCards(cards: List<Card>) {
        _playerCards.value = cards
    }
}
