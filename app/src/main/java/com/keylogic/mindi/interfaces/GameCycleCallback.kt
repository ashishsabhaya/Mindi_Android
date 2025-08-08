package com.keylogic.mindi.interfaces

interface GameCycleCallback {
    fun onPlayerStart(playerIndex: Int)
    fun onPlayerTrigger(playerIndex: Int)
    fun onGameCycleComplete()
}
