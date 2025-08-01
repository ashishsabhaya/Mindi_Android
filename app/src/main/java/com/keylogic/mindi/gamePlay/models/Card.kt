package com.keylogic.mindi.gamePlay.models

import com.keylogic.mindi.enums.Colors
import com.keylogic.mindi.enums.SuitType

class Card(
    val suit: SuitType = SuitType.NONE,
    val rank: Int = 0,
    val uniqueIndex: Int = 0) {

    var color = Colors.NONE
    init {
        color = when(suit) {
            SuitType.SPADE, SuitType.CLUB -> Colors.BLACK
            SuitType.HEART, SuitType.DIAMOND -> Colors.RED
            SuitType.NONE -> Colors.NONE
        }
    }

    fun getTag(): String {
        return "$suit$rank$uniqueIndex"
    }

    fun getCard(): String {
        return "${suit.suitName}${if (rank == 14) 1 else rank}"
    }

}