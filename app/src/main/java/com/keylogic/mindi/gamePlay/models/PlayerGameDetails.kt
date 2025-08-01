package com.keylogic.mindi.gamePlay.models

data class PlayerGameDetails(
    var originalPlayerIndex: Int = -1, // as per creator or 1st player
    var centerCardViewIndex: Int = -1,
    var cardList: ArrayList<Card> = ArrayList(),
    var isTrumpCardExist: Boolean = true,
    var isCardHider: Boolean = false,
    var hiddenCard: Card = Card()
)