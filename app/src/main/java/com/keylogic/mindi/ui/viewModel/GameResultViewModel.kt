package com.keylogic.mindi.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.keylogic.mindi.dialogs.GameResultFragment
import com.keylogic.mindi.gamePlay.models.Score
import com.keylogic.mindi.models.ResultProfile

class GameResultViewModel(
    private val state: SavedStateHandle
) : ViewModel() {

    private val gson = Gson()
    private val type = object : TypeToken<ArrayList<ResultProfile>>() {}.type

    private val _winnerList = MutableLiveData<List<ResultProfile>>()
    val winnerList: LiveData<List<ResultProfile>> get() = _winnerList

    private val _loserList = MutableLiveData<List<ResultProfile>>()
    val loserList: LiveData<List<ResultProfile>> get() = _loserList

    private val _winnerScore = MutableLiveData<Score>()
    val winnerScore: LiveData<Score> get() = _winnerScore

    private val _loserScore = MutableLiveData<Score>()
    val loserScore: LiveData<Score> get() = _loserScore

    init {
        loadDataFromArgs()
    }

    private fun loadDataFromArgs() {
        val wListJson: String = state[GameResultFragment.KEY_WINNER_LIST_JSON] ?: "[]"
        val lListJson: String = state[GameResultFragment.KEY_LOSER_LIST_JSON] ?: "[]"
        val wScoreJson: String = state[GameResultFragment.KEY_WINNER_SCORE_JSON] ?: ""
        val lScoreJson: String = state[GameResultFragment.KEY_LOSER_SCORE_JSON] ?: ""

        _winnerList.value = gson.fromJson(wListJson, type)
        _loserList.value = gson.fromJson(lListJson, type)

        _winnerScore.value = if (wScoreJson.isNotEmpty())
            gson.fromJson(wScoreJson, Score::class.java)
        else Score()

        _loserScore.value = if (lScoreJson.isNotEmpty())
            gson.fromJson(lScoreJson, Score::class.java)
        else Score()
    }
}