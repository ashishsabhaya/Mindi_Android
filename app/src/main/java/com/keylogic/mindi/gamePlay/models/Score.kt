package com.keylogic.mindi.gamePlay.models

data class Score(
    val hands: Int = 0,
    val spades: Int = 0,
    val hearts: Int = 0,
    val clubs: Int = 0,
    val diamonds: Int = 0,
)