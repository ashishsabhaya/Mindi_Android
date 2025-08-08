package com.keylogic.mindi.gamePlay.models

import com.keylogic.mindi.enums.SuitType

data class TrumpCard(
    val card: Card = Card(),
    val suit: SuitType = SuitType.NONE,
    val setterUID: String = "",
    val inWhichSuit: SuitType = SuitType.NONE,
    val isDeclared: Boolean = false
)