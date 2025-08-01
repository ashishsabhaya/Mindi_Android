package com.keylogic.mindi.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keylogic.mindi.gamePlay.models.Score

class ScoreConfigViewModel : ViewModel() {
    private val _leftScore = MutableLiveData(Score())
    private val _rightScore = MutableLiveData(Score())

    val leftScore: LiveData<Score> get() = _leftScore
    val rightScore: LiveData<Score> get() = _rightScore

    fun updateLeftScore(update: Score.() -> Score) {
        _leftScore.value = _leftScore.value?.update()
    }

    fun updateRightScore(update: Score.() -> Score) {
        _rightScore.value = _rightScore.value?.update()
    }

    fun resetScores() {
        _leftScore.value = Score()
        _rightScore.value = Score()
    }
}