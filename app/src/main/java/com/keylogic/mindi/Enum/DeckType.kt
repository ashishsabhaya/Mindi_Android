package com.keylogic.mindi.Enum

import com.keylogic.mindi.R

enum class DeckType(val resource: Int, val deckCount: Int) {
    DECK1(R.drawable.deck_1,1),
    DECK2(R.drawable.deck_2,2),
    DECK3(R.drawable.deck_3,3),
    DECK4(R.drawable.deck_4,4)
}