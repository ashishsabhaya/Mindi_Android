package com.keylogic.mindi.gamePlay.models

import com.keylogic.mindi.enums.SuitType

data class CardHider(
    val card: Card = Card(),
    val hiderUID: String = ""
)