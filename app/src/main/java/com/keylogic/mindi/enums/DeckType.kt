package com.keylogic.mindi.enums

import com.keylogic.mindi.R

enum class DeckType(val resource: Int, val deckCount: Int, totalCards: Int) {
    DECK1(R.drawable.deck_1,1,13),
    DECK2(R.drawable.deck_2,2,15),
    DECK3(R.drawable.deck_3,3,15),
    DECK4(R.drawable.deck_4,4,15)
}