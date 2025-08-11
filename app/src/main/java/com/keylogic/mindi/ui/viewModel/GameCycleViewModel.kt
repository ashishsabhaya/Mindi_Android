package com.keylogic.mindi.ui.viewModel

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import com.keylogic.mindi.gamePlay.helper.GameHelper
import com.keylogic.mindi.helper.CommonHelper
import com.keylogic.mindi.interfaces.GameCycleCallback
import kotlin.math.max
import kotlin.random.Random

class GameCycleViewModel : ViewModel() {

    private var gameCycleCallback: GameCycleCallback? = null
    private var countDownTimer: CountDownTimer? = null
    private var maxPlayers = 1

    fun initializeCallback(callback: GameCycleCallback) {
        gameCycleCallback = callback
    }

    // 2. Start a 5-second countdown
    fun startCountdown() {
        val playerIndex = GameHelper.getCurrTurnIndex()
        countDownTimer?.cancel()

//        CommonHelper.print("startTimer --> $maxPlayers > ${GameHelper.enteredCardMap.size}")
        if (GameHelper.enteredCardMap.size == maxPlayers) {
            gameCycleCallback?.onGameCycleComplete()
            return
        }
        gameCycleCallback?.onPlayerStart(playerIndex)

        var random = Random.nextInt(2, 4)
        if (GameHelper.currTurnPlayerUID == GameHelper.getCurrentPlayer().uId)
            random = 15
        countDownTimer = object : CountDownTimer(random * 1_000L, 1_000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                gameCycleCallback?.onPlayerTrigger(playerIndex)
            }
        }.start()
    }

    fun initializeTotalPlayers(totalPlayer: Int) {
        maxPlayers = totalPlayer
    }

    // 3. Stop the countdown
    fun stopCountdown() {
        countDownTimer?.cancel()
        countDownTimer = null
    }

    // Optional: Clear callback to avoid memory leaks
    override fun onCleared() {
        super.onCleared()
        stopCountdown()
        gameCycleCallback = null
    }
}
