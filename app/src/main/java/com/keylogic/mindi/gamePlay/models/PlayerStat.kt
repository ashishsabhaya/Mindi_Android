package com.keylogic.mindi.gamePlay.models

import com.keylogic.mindi.enums.SuitType
import com.keylogic.mindi.models.Card

data class PlayerStat(
    val uniqueId: String,
    val name: String,
    val cardList: List<Card>,
    val enteredCardList: List<Card>,
    val emptySuitList: List<SuitType>,
    val isPlayersTeammate: Boolean,
    val isBot: Boolean,
    val isTrumpCardsExist: Boolean,
    val isCardHider: Boolean,
    val hiddenCard: Card,
    val playerCards: Map<String, PlayerStat> = emptyMap()
)