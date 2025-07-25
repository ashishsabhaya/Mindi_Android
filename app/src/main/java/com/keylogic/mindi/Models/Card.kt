package com.keylogic.mindi.Models

import com.keylogic.mindi.Enum.Colors
import com.keylogic.mindi.Enum.SuitType

class Card(val suit: SuitType,val rank: Int,val uniqueIndex: Int) {
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