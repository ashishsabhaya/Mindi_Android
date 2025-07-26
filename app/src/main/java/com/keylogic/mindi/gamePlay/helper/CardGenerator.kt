package com.keylogic.mindi.gamePlay.helper

import com.keylogic.mindi.enums.DeckType
import com.keylogic.mindi.enums.SuitType
import com.keylogic.mindi.gamePlay.models.Player
import com.keylogic.mindi.models.Card
import kotlin.collections.iterator

class CardGenerator {
    companion object {
        var INSTANCE = CardGenerator()
    }

    fun generateCard(deck: DeckType, numPlayers: Int): ArrayList<Card> {
        val deckIndex = deck.deckCount
        val allCardStack = mutableListOf<Card>()

        val deckConfig = getDeckConfiguration(deckIndex, numPlayers) ?: return ArrayList()

        fun getSuit(index: Int): SuitType {
            return when(index) {
                0 -> SuitType.SPADE
                1 -> SuitType.HEART
                2 -> SuitType.CLUB
                else -> SuitType.DIAMOND
            }
        }

        val suitStacks = mutableMapOf<SuitType, MutableList<Card>>()

        for (suitType in SuitType.entries) {
            suitStacks[suitType] = mutableListOf()
        }

        for ((rank, count) in deckConfig) {
            for (i in 0 until count) {
                val suit = getSuit(i % 4)
                val card = Card(suit, rank, count)
                suitStacks[suit]?.add(card)
            }
        }
        suitStacks.values.forEach { allCardStack.addAll(it) }

        // Shuffle remaining deck and distribute the rest of the cards randomly
        allCardStack.shuffle()
        return ArrayList(allCardStack)
    }

    fun distributeCards(totalPlayers: Int, allCardStack: ArrayList<Card>, playersList: ArrayList<Player>) {
        var cardIndex = 0
        val totalCards = allCardStack.size
        val cardsPerPlayer = totalCards / totalPlayers

        for (i in 0 until totalPlayers) {
            for (j in 0 until cardsPerPlayer) {
                if (cardIndex < totalCards) {
                    playersList[i].addCard(allCardStack[cardIndex++])
                }
            }
        }
    }

    private fun getDeckConfiguration(deckIndex: Int, numPlayers: Int): Map<Int, Int>? {
        return when {
            deckIndex == 1 && numPlayers == 4 -> (2..14).associateWith { 4 }
            deckIndex == 2 && numPlayers == 4 -> mapOf(7 to 4) + (8..14).associateWith { 8 }
            deckIndex == 2 && numPlayers == 6 -> mapOf(3 to 2) + (4..14).associateWith { 8 }
            deckIndex == 3 && numPlayers == 6 -> mapOf(7 to 6) + (8..14).associateWith { 12 }
            deckIndex == 3 && numPlayers == 8 -> (5..14).associateWith { 12 }
            deckIndex == 4 && numPlayers == 6 -> mapOf(9 to 10) + (10..14).associateWith { 16 }
            deckIndex == 4 && numPlayers == 8 -> mapOf(7 to 8) + (8..14).associateWith { 16 }
            else -> null
        }
    }

    fun getRandom24Cards(): ArrayList<Card> {
        val result = mutableListOf<Card>()

        for (suit in SuitType.entries) {
            if (suit != SuitType.NONE)
                result.add(Card(suit, 11, 1))
        }

        val cardPool = mutableListOf<Card>()
        for (rank in 2..14) {
            if (rank == 11) continue
            for (suit in SuitType.entries) {
                if (suit != SuitType.NONE)
                    cardPool.add(Card(suit, rank, 1))
            }
        }

        cardPool.shuffle()
        result.addAll(cardPool.subList(0, 20))
        result.shuffle()
        return ArrayList(result)
    }


}