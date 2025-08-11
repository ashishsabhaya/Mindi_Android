package com.keylogic.mindi.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.util.copy
import com.keylogic.mindi.gamePlay.helper.GameHelper
import com.keylogic.mindi.gamePlay.models.Card
import com.keylogic.mindi.gamePlay.models.Score

class PlayAreaConfigViewModel : ViewModel() {

    private val _mindiCount = MutableLiveData(Pair(0,0))
    private val _leftScore = MutableLiveData(Score())
    private val _rightScore = MutableLiveData(Score())
    private val _betPrice = MutableLiveData(0L)
    private val _trumpCard = MutableLiveData(Card())

    val mindiCount: LiveData<Pair<Int, Int>> get() = _mindiCount
    val leftScore: LiveData<Score> get() = _leftScore
    val rightScore: LiveData<Score> get() = _rightScore
    val betPrice: LiveData<Long> get() = _betPrice
    val trumpCard: LiveData<Card> get() = _trumpCard
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    fun updateMessage(newMessage: String) {
        _message.value = newMessage
    }

    fun clearMessage() {
        _message.value = ""
    }

    fun updateMindiCount(pair: Pair<Int, Int>) {
        _mindiCount.value = pair
    }

    fun updateLeftScore(update: Score.() -> Score) {
        _leftScore.value = _leftScore.value?.update()
    }

    fun updateRightScore(update: Score.() -> Score) {
        _rightScore.value = _rightScore.value?.update()
    }

    fun updateBetPrice(newPrice: Long) {
        _betPrice.value = newPrice
    }

    fun updateTrumpCard(card: Card) {
        _trumpCard.value = card
    }

    fun resetScores() {
        _leftScore.value = Score()
        _rightScore.value = Score()
        _betPrice.value = 0
    }

    fun resetAll() {
        updateLeftScore {
            copy(hands = 0, spades = 0, hearts = 0, clubs = 0, diamonds = 0)
        }
        updateRightScore {
            copy(hands = 0, spades = 0, hearts = 0, clubs = 0, diamonds = 0)
        }
        updateMindiCount(Pair(0,0))
        updateBetPrice(0L)
        updateTrumpCard(Card())
        updateMessage("")
    }

}