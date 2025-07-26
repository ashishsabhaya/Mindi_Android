package com.keylogic.mindi.gamePlay.models

data class PlayerDetails(
    val joinerIndex: Int = 0,
    val uniqueKey: String = "",
    val playerName: String = "",
    val profileName: String = "",
    val lastTime: Long = 0L,
    val isBot: Boolean = false,
    var isCreator: Boolean = false
)