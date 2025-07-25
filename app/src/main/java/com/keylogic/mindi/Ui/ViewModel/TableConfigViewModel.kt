package com.keylogic.mindi.Ui.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keylogic.mindi.Enum.DeckType

class TableConfigViewModel : ViewModel() {

    private val _deckType = MutableLiveData(DeckType.DECK1)
    val deckType: LiveData<DeckType> get() = _deckType
    private val _BetPrice = MutableLiveData(0L)
    val betPrice: LiveData<Long> get() = _BetPrice
    private val _isHideMode = MutableLiveData(false)
    val isHideMode: LiveData<Boolean> get() = _isHideMode

    private val _totalPlayers = MutableLiveData(4)
    val totalPlayers: LiveData<Int> get() = _totalPlayers

    private val _isCreateTable = MutableLiveData(true)
    val isCreateTable: LiveData<Boolean> get() = _isCreateTable

    private val _JoinCode = MutableLiveData("")
    val joinCode: LiveData<String> get() = _JoinCode


    fun setDeckType(deck: DeckType) {
        _deckType.value = deck
        // Update players based on deck selection
        _totalPlayers.value = when (deck) {
            DeckType.DECK1 -> 4
            DeckType.DECK2 -> 4
            DeckType.DECK3 -> 6
            DeckType.DECK4 -> 6
        }
    }

    fun updateCode(code: String) {
        _JoinCode.value = code
    }

    fun setGameMode(isHide: Boolean) {
        _isHideMode.value = isHide
    }

    fun setBetPrice(price: Long) {
        _BetPrice.value = price
    }

    fun setTotalPlayers(players: Int) {
        _totalPlayers.value = players
    }

    fun setCreateTable(isCreate: Boolean) {
        _isCreateTable.value = isCreate
    }
}
