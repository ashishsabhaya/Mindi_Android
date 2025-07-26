package com.keylogic.mindi.models

import com.keylogic.mindi.enums.Colors
import com.keylogic.mindi.enums.SuitType

class Card(val suit: SuitType,val rank: Int,val uniqueIndex: Int = 0) {
    var color = Colors.NONE
    init {
        color = when(suit) {
            SuitType.SPADE, SuitType.CLUB -> Colors.BLACK
            SuitType.HEART, SuitType.DIAMOND -> Colors.RED
            SuitType.NONE -> Colors.NONE
        }
    }

//    fun getTag(): String {
//        return "$suit$rank$uniqueIndex"
//    }

    fun getCard(): String {
        return "${suit.suitName}${if (rank == 14) 1 else rank}"
    }

}