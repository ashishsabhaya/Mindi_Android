package com.keylogic.mindi.gamePlay.models

import com.keylogic.mindi.enums.SuitType
import com.keylogic.mindi.gamePlay.models.PlayerStat
import com.keylogic.mindi.models.Card
import com.keylogic.mindi.models.CardView
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

data class Player(
    var uniqueId: String,
    var name: String
) {
    lateinit var cardList: ArrayList<Card>
    lateinit var enteredCardList: ArrayList<Card>
    lateinit var spadeCardList: ArrayList<Card>
    lateinit var heartCardList: ArrayList<Card>
    lateinit var clubCardList: ArrayList<Card>
    lateinit var diamondCardList: ArrayList<Card>
    var centerCard: CardView? = null

    lateinit var emptySuitList: ArrayList<SuitType>
    var isPlayersTeammate = false
    var isBot = true
    var isCurrPlayer = false
    var isTrumpCardsExist = true
    var playerIndex = -1
    var isCardHider = false
    var hiddenCard = getNullCard()
    var currRoundSuit = SuitType.NONE

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    fun getNullCard() : Card {
        return Card(SuitType.NONE, 0, 0)
    }

    fun assignCards(list: ArrayList<Card>) {
        cardList = ArrayList()
        enteredCardList = ArrayList()
        spadeCardList = ArrayList()
        heartCardList = ArrayList()
        clubCardList = ArrayList()
        diamondCardList = ArrayList()
        emptySuitList = ArrayList()

        cardList.addAll(list)
        distributeInSeparateList()
    }

    fun getPlayerIdentification(): String {
        return name
    }

    fun addCard(card: Card) {
        cardList.add(card)
        distributeInSeparateList()
    }

    fun removeCard(card: Card) {
        for (listCard in cardList) {
            if (listCard.getTag() == card.getTag()) {
                cardList.remove(card)
                distributeInSeparateList()
                return
            }
        }
    }

    fun getSuitList(): List<SuitType> = listOf(SuitType.SPADE, SuitType.HEART, SuitType.CLUB, SuitType.DIAMOND).shuffled()

    fun sortCards() {
        val suitOrder = listOf(
            SuitType.SPADE, SuitType.HEART,
            SuitType.CLUB, SuitType.DIAMOND
        )
        val rankOrder = listOf(1, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2)

        val list: ArrayList<Card> = cardList.sortedWith(
            compareBy<Card> { suitOrder.indexOf(it.suit) }
                .thenBy { rankOrder.indexOf(it.rank) }
        ).toMutableList() as ArrayList<Card> // Ensure it's mutable

        cardList.clear()
        cardList.addAll(list)
        distributeInSeparateList()
    }

    fun enteredCard(card: Card, hukumSuit: SuitType) {
        for (listCard in cardList) {
            if (listCard.getTag() == card.getTag()) {
                //MainHelper.INSTANCE.printMessage("Remove ~~~~~~~~~~~~~~~~ > ${getPlayerIdentification()} > ${card.getCard()}")
                cardList.remove(listCard)
                enteredCardList.add(listCard)
                distributeInSeparateList()

                if (currRoundSuit != SuitType.NONE && currRoundSuit != card.suit) {
                    emptySuitList.add(currRoundSuit)
                    if (currRoundSuit == hukumSuit) {
                        isTrumpCardsExist = false
                    }
                }
                break
            }
        }
    }

    fun distributeInSeparateList() {
        if (cardList.isEmpty())
            return
        spadeCardList.clear()
        heartCardList.clear()
        clubCardList.clear()
        diamondCardList.clear()
        for (i in cardList.size-1 downTo 0) {
            val card = cardList[i]
            when(card.suit) {
                SuitType.SPADE -> {
                    spadeCardList.add(card)
                }
                SuitType.HEART -> {
                    heartCardList.add(card)
                }
                SuitType.CLUB -> {
                    clubCardList.add(card)
                }
                SuitType.DIAMOND -> {
                    diamondCardList.add(card)
                }
                else -> {}
            }
        }
    }

    fun getMinSuitList(): ArrayList<Card> {
        return listOf(spadeCardList, heartCardList, clubCardList, diamondCardList)
            .minByOrNull { it.size } ?: cardList
    }

    private fun getMaxSuitList(): ArrayList<Card> {
        return listOf(spadeCardList, heartCardList, clubCardList, diamondCardList)
            .maxByOrNull { it.size } ?: cardList
    }

    private fun getListBySuit(suit: SuitType): ArrayList<Card> {
        val list: ArrayList<Card> = if (suit == SuitType.SPADE)
            return spadeCardList
        else if (suit == SuitType.HEART)
            return heartCardList
        else if (suit == SuitType.CLUB)
            return clubCardList
        else
            return diamondCardList

        return list
    }

    fun getEmptySuitList(): List<SuitType> {
        val list = ArrayList<SuitType>()
        if (spadeCardList.isEmpty())
            list.add(SuitType.SPADE)
        else if (heartCardList.isEmpty())
            list.add(SuitType.HEART)
        else if (clubCardList.isEmpty())
            list.add(SuitType.CLUB)
        else if (diamondCardList.isEmpty())
            list.add(SuitType.DIAMOND)
        return list
    }

    fun isCardRankPresent(rank: Int, excludedSuit: SuitType? = null): Boolean {
        return cardList.any {
            it.rank == rank && (excludedSuit == null || it.suit != excludedSuit)
        }
    }

    fun isCardPresent(suit: SuitType, rank: Int): Boolean {
        return cardList.any { it.suit == suit && it.rank == rank }
    }

    fun getSpecificCard(suit: SuitType, rank: Int): Card? {
        return cardList.find { it.suit == suit && it.rank == rank }
    }

    fun getSpecificRankCard(rank: Int, excludedSuit: SuitType? = null): Card? {
        return cardList.find {
            it.rank == rank && (excludedSuit == null || it.suit != excludedSuit)
        }
    }

    fun countCardsBySuit(suit: SuitType): Int {
        return cardList.count { it.suit == suit }
    }

    fun countCardsBySpecificSuitAndRank(suit: SuitType, rank: Int): Int {
        return cardList.count { it.suit == suit && it.rank == rank }
    }

    fun getCardsListByRank(rank: Int, excludeSuit: SuitType? = null, suit: SuitType? = null): List<Card> {
        if (suit != null) {
            return cardList.filter { it.rank == rank && it.suit == suit }
        }
        return cardList.filter { it.rank == rank && excludeSuit != it.suit }
    }

    fun getMinCardSuitWithCount(): Pair<Int, SuitType> {
        val suit = getMinimumSuit()
        val listOfMinSuit = getListBySuit(suit)
        return Pair(listOfMinSuit.size, suit)
    }

    fun getMaxCardSuitWithCount(): Pair<Int, SuitType> {
        if (cardList.isEmpty()) return Pair(0, SuitType.SPADE)

        val suitCount = cardList.groupingBy { it.suit }.eachCount()
        val maxEntry = suitCount.maxByOrNull { it.value } ?: return Pair(0, getMaxSuitList()[0].suit)

        return Pair(maxEntry.value, maxEntry.key)
    }

    fun getMissingSuitList(): List<SuitType> {
        val allSuits = getSuitList()

        val presentSuits = cardList.map { it.suit }.toSet()

        return allSuits.filter { it !in presentSuits }
    }

    fun getSortedSuitList(ascending: Boolean = true, excludeSuit: SuitType? = null): List<SuitType> {
        val suitLists = mapOf(
            SuitType.SPADE to spadeCardList,
            SuitType.HEART to heartCardList,
            SuitType.CLUB to clubCardList,
            SuitType.DIAMOND to diamondCardList
        )

        var filteredList = suitLists.filter { it.value.isNotEmpty() && excludeSuit != it.key }.toList()
        if (filteredList.isEmpty())
            filteredList = suitLists.filter { it.key == excludeSuit }.toList()
        val sortedList = if (ascending) {
            filteredList.sortedBy { it.second.size }
        } else {
            filteredList.sortedByDescending { it.second.size }
        }

        return sortedList.map { it.first }
    }

    fun isSuitExist(suit: SuitType = currRoundSuit): Boolean {
        return getListBySuit(suit).isNotEmpty()
    }

    fun isAllCardWithSameRank(suit: SuitType, compareRank: Int = 0): Boolean {
        val list = getListBySuit(suit)
        val rank = if (compareRank == 0) list[0].rank else compareRank
        return list.count { it.rank == rank } == list.size
    }

    fun isAllCardIsHigherThanGivenRank(suit: SuitType, compareRank: Int = 0): Boolean {
        val list = getListBySuit(suit)
        val rank = if (compareRank == 0) 11 else compareRank
        return list.count { it.rank > rank } == list.size
    }

    fun getCardsNotMatchingAndNotHigherThan(suit: SuitType, excludeRank: Int, compareRank: Int): Card? {
        val list = getListBySuit(suit)
        val filteredList = list.filter { it.rank != excludeRank && it.rank <= compareRank }
        return if (filteredList.isNotEmpty()) filteredList[0] else null
    }

    fun getMinimumCard(): Card {
        val suitLists = mapOf(
            SuitType.SPADE to spadeCardList,
            SuitType.HEART to heartCardList,
            SuitType.CLUB to clubCardList,
            SuitType.DIAMOND to diamondCardList
        )
        val shuffledSuitLists = suitLists.mapValues { (_, list) ->
            list.shuffled()
        }

        val nonEmptySortedBySize = shuffledSuitLists
            .filter { it.value.isNotEmpty() }
            .toList()
            .sortedBy { it.second.size }

        val smallestList = nonEmptySortedBySize.first().second

        return smallestList.minByOrNull { if (it.rank == 14) 1 else it.rank } ?: if (smallestList.isEmpty()) cardList[0] else smallestList[0]
    }

    fun getMinimumSuit(excludeSuit: SuitType? = null, secondExcludeSuit: SuitType? = null): SuitType {
        val minimumSuitList = getSortedSuitList()
        if (minimumSuitList.isEmpty())
            return excludeSuit!!
        else {
            if (minimumSuitList.size == 1)
                return minimumSuitList[0]
            else {
                for (suit in minimumSuitList) {
                    if (suit != excludeSuit && suit != secondExcludeSuit)
                        return suit
                }
            }
            return secondExcludeSuit ?: excludeSuit!!
        }
    }

    fun getMaximumSuit(excludeSuit: SuitType? = null): SuitType {
        val suitLists = mapOf(
            SuitType.SPADE to spadeCardList,
            SuitType.HEART to heartCardList,
            SuitType.CLUB to clubCardList,
            SuitType.DIAMOND to diamondCardList
        )

        val shuffledSuitLists = suitLists.mapValues { (_, list) ->
            list.shuffled()
        }

        return shuffledSuitLists
            .filter { it.value.isNotEmpty() && excludeSuit != it.key }
            .maxByOrNull { it.value.size }
            ?.key ?: (excludeSuit ?: cardList[0].suit)
    }

    fun getMinCard(suit: SuitType, excludeCardRank: Int = 0): Card {
        val list = getListBySuit(suit)
        if (list.isEmpty())
            return cardList[0]
        val filteredList = if (isSingleCardOfThatSuit(suit)) {
            list
        }
        else {
            if (excludeCardRank != 0) list.filter { it.rank != excludeCardRank } else list
        }

        return filteredList.minByOrNull { if (it.rank == 1) 14 else it.rank } ?: list[0]
    }

    fun getNextCard(suit: SuitType, rank: Int, higherRank: Boolean, excludeCardRank: Int = 0): Card {
        val list = getListBySuit(suit)

        val matchedCard = if (higherRank) {
            list.firstOrNull { it.rank >= rank && it.rank != excludeCardRank }
        } else {
            list.firstOrNull { it.rank <= rank && it.rank != excludeCardRank }
        }

        return matchedCard
            ?: if (list.isEmpty()) {
                val minSuit = getMinimumSuit(excludeSuit = suit)
                getMinCard(minSuit, excludeCardRank)
            } else {
                getMinCard(suit, excludeCardRank)
            }
    }

    fun countCardsByRank(suit: SuitType, rank: Int, isHigher: Boolean = false, isEqual: Boolean = false): Int {
        val list = getListBySuit(suit)

        return when {
            isEqual -> list.count { it.rank == rank }
            isHigher -> list.count { it.rank >= rank }
            else -> list.count { it.rank < rank }
        }
    }

    fun getMaxCard(suit: SuitType, excludeCardRank: Int = 0): Card {
        val list = getListBySuit(suit)
        val filteredList = list.filter { it.rank != excludeCardRank }
        return filteredList.maxByOrNull { it.rank } ?: if (filteredList.isEmpty() && list.isEmpty()) cardList[0] else list[0]
    }

    fun isSingleCardOfThatSuit(suit: SuitType): Boolean {
        val list = getListBySuit(suit)
        return list.size == 1
    }

    fun getHighestCardAtLeast(suit: SuitType, rank: Int): Card {
        val list = getListBySuit(suit)
        return if (isSingleCardOfThatSuit(suit))
            list[0]
        else {
            list
                .filter { (if (it.rank == 1) 14 else it.rank) >= rank }
                .maxByOrNull { if (it.rank == 1) 14 else it.rank }
                ?: if (getListBySuit(suit).isEmpty()) getMinimumCard() else getMinCard(suit)
        }
    }

    fun setJson(json: String) {
        val adapter = moshi.adapter(PlayerStat::class.java)
        val playerStat = adapter.fromJson(json) ?: return

        uniqueId = playerStat.uniqueId
        name = playerStat.name

        cardList.clear()
        cardList.addAll(playerStat.cardList)
        enteredCardList.clear()
        enteredCardList.addAll(playerStat.enteredCardList)
        emptySuitList.clear()
        emptySuitList.addAll(playerStat.emptySuitList)

        isPlayersTeammate = playerStat.isPlayersTeammate
        isBot = playerStat.isBot
        isTrumpCardsExist = playerStat.isTrumpCardsExist

        isCardHider = playerStat.isCardHider
        hiddenCard = playerStat.hiddenCard
    }

    fun getPlayerJson(): String {
        val playerStat = PlayerStat(
            uniqueId,
            name,
            cardList,
            enteredCardList,
            emptySuitList,
            isPlayersTeammate,
            isBot,
            isTrumpCardsExist,
            isCardHider,
            hiddenCard
        )
        val adapter = moshi.adapter(PlayerStat::class.java)
        return adapter.toJson(playerStat)
    }

}