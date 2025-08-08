package com.keylogic.mindi.gamePlay.models

data class PlayerDetails(
    val isCurrPlayer: Boolean,
    val name: String,
    val uId: String,
    val profileId: Int,
    val joinIndex: Int,
    val isBot: Boolean,
    var isOnline: Boolean,
    var isCreator: Boolean,
    var lastTimeStamp: Long,
    var isMyTeammate: Int,
    var centerCardIndex: Int,
    val playerGameDetails: PlayerGameDetails = PlayerGameDetails()
)