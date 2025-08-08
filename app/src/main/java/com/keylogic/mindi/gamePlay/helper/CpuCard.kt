package com.keylogic.mindi.gamePlay.helper

import com.keylogic.mindi.enums.SuitType
import com.keylogic.mindi.gamePlay.helper.GameHelper.Companion.allEnteredCardMap
import com.keylogic.mindi.gamePlay.helper.GameHelper.Companion.enteredCardMap
import com.keylogic.mindi.gamePlay.models.Card
import com.keylogic.mindi.gamePlay.models.PlayerDetails


class CpuCard {
    companion object {
        val INSTANCE = CpuCard()
    }
    val totalPlayers = GameHelper.tableConfig.totalPlayers
    val currDeckType = GameHelper.tableConfig.deckType

    fun getValidCardByCPU(): Card {
        val isHide = GameHelper.tableConfig.isHideMode
        val currPlayer = GameHelper.getCurrentTurnPlayer()
        val isTrumpCardDeclared = GameHelper.trumpCardSuit.suit == SuitType.NONE
        val currRoundSuit = GameHelper.currRoundSuitType
        val hukum = GameHelper.trumpCardSuit
        val playersList = GameHelper.playerDetailsList
        var card: Card
//        totalCardsPerPlayer - playersList.last().cardList.size
        val lastPlayer = playersList.last()

        fun isRankEntered(rank: Int): Boolean {
            return enteredCardMap.values.any { it.rank == rank }
        }

        fun getHighestRankCardWhenMindiEntered(highestEnteredPlayerCard: Card, isHighestEnteredPlayerIsTeammate: Boolean = false): Card {
            val higherRankCard = currPlayer.playerGameDetails.getMaxCard(currRoundSuit)
            if (isHighestEnteredPlayerIsTeammate && highestEnteredPlayerCard.rank == 14) {
                return currPlayer.playerGameDetails.getMinCard(currRoundSuit, excludeCardRank = 10)
            }
            return if ((highestEnteredPlayerCard.rank == 14 && higherRankCard.rank >= highestEnteredPlayerCard.rank) ||
                higherRankCard.rank > highestEnteredPlayerCard.rank) {
                higherRankCard
            } else {
                currPlayer.playerGameDetails.getMinCard(currRoundSuit, excludeCardRank = 10)
            }
        }

        fun getMinimumSuitCard(suit: SuitType = SuitType.NONE): Card {
            var finalSuit = if (suit != SuitType.NONE) suit else currPlayer.playerGameDetails.getMinimumSuit(excludeSuit = hukum.suit)
            if (isTrumpCardDeclared && currPlayer.playerGameDetails.countCardsBySpecificSuitAndRank(finalSuit, 10) == currPlayer.playerGameDetails.countCardsBySuit(finalSuit)) {
                finalSuit = currPlayer.playerGameDetails.getMinimumSuit(excludeSuit = finalSuit, secondExcludeSuit = hukum.suit)
            }
            val highestRankCard = currPlayer.playerGameDetails.getMaxCard(finalSuit)

            if ((!isTrumpCardDeclared || suit != hukum.suit) &&
                isLastHighestCard(highestRankCard, compareWithCurrPlayersCard = true)) {
                return highestRankCard
            }

            return currPlayer.playerGameDetails.getMinCard(finalSuit, excludeCardRank = 10)
        }

        fun compareCardsAndGetHukum(): Card {
            val sortedList = currPlayer.playerGameDetails.getSortedSuitList(ascending = false)
            val highestSuit = sortedList[0]
            val secondHighestSuit = if (sortedList.size > 1) sortedList[1] else highestSuit
            var finalHukumSuit = highestSuit

            val totalCardsOfSingleSuit = (currDeckType.totalCards * totalPlayers) / 4

            val noOf1stHighestSuit = currPlayer.playerGameDetails.countCardsBySuit(highestSuit)
            val noOf1stHigherRankCards = currPlayer.playerGameDetails.countCardsByRank(highestSuit, 13, isHigher = true)
            val noOf1st10RankCards = currPlayer.playerGameDetails.countCardsByRank(highestSuit, 10, isEqual = true)
            val noOf1stAceRankCards = currPlayer.playerGameDetails.countCardsByRank(highestSuit, 1, isEqual = true)
            val noOf1stSuitRounds = countTotalRoundsOfSuit(highestSuit)
            val noOfRemaningCardOf1stSuit = totalCardsOfSingleSuit - (noOf1stSuitRounds * totalPlayers)

            val noOf2ndHighestSuit = currPlayer.playerGameDetails.countCardsBySuit(secondHighestSuit)
            val noOf2ndHigherRankCards = currPlayer.playerGameDetails.countCardsByRank(secondHighestSuit, 13, isHigher = true)
            val noOf2nd10RankCards = currPlayer.playerGameDetails.countCardsByRank(secondHighestSuit, 10, isEqual = true)
            val noOf2ndAceRankCards = currPlayer.playerGameDetails.countCardsByRank(secondHighestSuit, 1, isEqual = true)
            val noOf2ndSuitRounds = countTotalRoundsOfSuit(secondHighestSuit)
            val noOfRemaningCardOf2ndSuit = totalCardsOfSingleSuit - (noOf2ndSuitRounds * totalPlayers)

            //4 + 2 > 5 + 0
            if (noOf2ndHighestSuit + noOf2ndSuitRounds > noOf1stHighestSuit + noOf1stSuitRounds) {
                //♠ = 4,                           ♦ = 5
                //♠ = a = 0 , k = 2, 10 = 2        ♦ = a = 0, k = 2, 10 = 0
                if (noOf2ndHigherRankCards > noOf1stHigherRankCards) {// && noOf2ndAceRankCards >= noOf1stAceRankCards
                    finalHukumSuit = secondHighestSuit
                }
                else if (noOf2ndHigherRankCards != 0) {
                    if (noOf2nd10RankCards >= 1) {
                        finalHukumSuit = secondHighestSuit
                    }
                }
                else if (noOf1stHigherRankCards == 0) {
                    finalHukumSuit = secondHighestSuit
                }
            }
            //3 + 3 == 4 + 2
            else if (noOf2ndHighestSuit + noOf2ndSuitRounds == noOf1stHighestSuit + noOf1stSuitRounds) {// && noOf2ndHigherRankCards != 0
                //♠ = 3,                           ♦ = 4
                //♠ = a = 0 , k = 1, 10 = 2        ♦ = a = 2, k = 0, 10 = 1
                if (noOf2ndHigherRankCards > noOf1stHigherRankCards) {
                    finalHukumSuit = secondHighestSuit
                }
                else if (noOf2ndHigherRankCards == noOf1stHigherRankCards) {
                    if (noOf2ndAceRankCards > noOf1stAceRankCards) {
                        finalHukumSuit = secondHighestSuit
                    }
                    else if (noOf1stAceRankCards > noOf2ndAceRankCards) {
                        finalHukumSuit = highestSuit
                    }
                    else {
                        if (noOf2nd10RankCards > noOf1st10RankCards) {
                            finalHukumSuit = secondHighestSuit
                        }
                    }
                }
            }
            //2 + 3 < 4 + 2

            val minOrMindiCard = if (currPlayer.playerGameDetails.isCardPresent(finalHukumSuit, 10)) {
                currPlayer.playerGameDetails.getSpecificCard(finalHukumSuit, 10)!!
            }
            else {
                currPlayer.playerGameDetails.getMinCard(finalHukumSuit)
            }
            return minOrMindiCard
        }

        if (currRoundSuit == SuitType.NONE) {
            fun checkOpponentEmptySuitAndEnterRemainingSuitCard(): Card {
                val minimumSuitList = currPlayer.playerGameDetails.getSortedSuitList(excludeSuit = hukum.suit)
                for (suit in minimumSuitList) {
                    val playerThatDontHaveThisSuit = getLastPlayerThatDontHaveThatSuit(suit)
                    if (playerThatDontHaveThisSuit == null) {
                        return getMinimumSuitCard(suit)
                    }
                    else if (playerThatDontHaveThisSuit.isMyTeammate == currPlayer.isMyTeammate) {
                        return if (currPlayer.playerGameDetails.isCardPresent(suit, 10)) {
                            currPlayer.playerGameDetails.getSpecificCard(suit, 10)!!
                        }
                        else {
                            getMinimumSuitCard(suit)
                        }
                    }
                }

                for (suit in minimumSuitList) {
                    val minCard = currPlayer.playerGameDetails.getMinCard(suit, excludeCardRank = 10)
                    if (minCard.rank != 10)
                        return minCard
                }
                if (currPlayer.playerGameDetails.isSuitExist(hukum.suit)) {
                    val minCard = currPlayer.playerGameDetails.getMinCard(hukum.suit, excludeCardRank = 10)
                    if (minCard.rank != 10)
                        return minCard
                }
                return getMinimumSuitCard()
            }

            fun getMinimumCardSuitUsingPlayersCard(): Card {
                fun getAvailableSuitExcludeOpponent(availableSuits: List<SuitType>): Card? {
                    for (suit in availableSuits) {
                        if (currPlayer.playerGameDetails.countCardsBySuit(suit) <= 2) {
                            val highestCardOfSuit = currPlayer.playerGameDetails.getMaxCard(suit, excludeCardRank = 10)
                            val minCardOfSuit = currPlayer.playerGameDetails.getMinCard(suit, excludeCardRank = 10)
                            return if (isLastHighestCard(highestCardOfSuit))
                                highestCardOfSuit
                            else
                                minCardOfSuit
                        }
                    }
                    return null
                }

                val minSuitWithCount = currPlayer.playerGameDetails.getMinCardSuitWithCount()
                val teammates = allEnteredCardMap
                    .mapNotNull { it.entries.firstOrNull() }
                    .filter { it.key.isMyTeammate == currPlayer.isMyTeammate }

                val opponentTeammates = allEnteredCardMap
                    .mapNotNull { it.entries.firstOrNull() }
                    .filter { it.key.isMyTeammate != currPlayer.isMyTeammate }

                val minimumSuitList = currPlayer.playerGameDetails.getSortedSuitList(excludeSuit = hukum.suit)

                val maxSuitOfCurrPlayer = currPlayer.playerGameDetails.getMaximumSuit()
                val highestCardOfMaxSuit = currPlayer.playerGameDetails.getMaxCard(maxSuitOfCurrPlayer, excludeCardRank = 10)
                val lowestCardOfMaxSuit = currPlayer.playerGameDetails.getMinCard(maxSuitOfCurrPlayer, excludeCardRank = 10)

                // No teammate played yet
                if (teammates.isEmpty()) {
                    if (!isTrumpCardDeclared) {
                        val opponentSuits = opponentTeammates.map { it.key.playerGameDetails.enteredCardList[0].suit }
                        val availableSuits = minimumSuitList.filterNot { it in opponentSuits }
                        if (isHide) {
                            if (availableSuits.isNotEmpty()) {
                                val maxSuit = currPlayer.playerGameDetails.getMaximumSuit()
                                getAvailableSuitExcludeOpponent(availableSuits) ?: currPlayer.playerGameDetails.getMinCard(maxSuit, excludeCardRank = 10)
                            }
                            else {
                                if (isLastHighestCard(highestCardOfMaxSuit))
                                    highestCardOfMaxSuit
                                else
                                    lowestCardOfMaxSuit
                            }
                        }
                        else {
                            return if (availableSuits.isNotEmpty()) {
                                currPlayer.playerGameDetails.getMinCard(availableSuits[0], excludeCardRank = 10)
                            } else {
                                getMinimumSuitCard()
                            }
                        }
                    }
                    else {
                        return checkOpponentEmptySuitAndEnterRemainingSuitCard()
                    }
                }
                // One teammate has played
                else if (teammates.size == 1) {
                    if (!isTrumpCardDeclared) {
                        val firstPlayerCard = teammates.first().value
                        val opponentSuits = opponentTeammates.map { it.key.playerGameDetails.enteredCardList[0].suit }
                        val availableSuits = minimumSuitList.filterNot { it in opponentSuits }

                        if (isHide) {
                            if (currPlayer.playerGameDetails.countCardsBySuit(firstPlayerCard.suit) > 2) {
                                currPlayer.playerGameDetails.getMinCard(firstPlayerCard.suit, excludeCardRank = 10)
                            }
                            else {
                                if (availableSuits.isNotEmpty()) {
                                    getAvailableSuitExcludeOpponent(availableSuits) ?:
                                    if (currPlayer.playerGameDetails.isSuitExist(firstPlayerCard.suit))
                                        currPlayer.playerGameDetails.getMinCard(firstPlayerCard.suit, excludeCardRank = 10)
                                    else if (isLastHighestCard(highestCardOfMaxSuit))
                                        highestCardOfMaxSuit
                                    else
                                        lowestCardOfMaxSuit
                                }
                                else {
                                    if (isLastHighestCard(highestCardOfMaxSuit))
                                        highestCardOfMaxSuit
                                    else
                                        lowestCardOfMaxSuit
                                }
                            }
                        }
                        else {
                            return if (!currPlayer.playerGameDetails.isSuitExist(firstPlayerCard.suit) || minSuitWithCount.first == 1) {
                                if (availableSuits.isNotEmpty()) {
                                    currPlayer.playerGameDetails.getMinCard(availableSuits[0], excludeCardRank = 10)
                                } else {
                                    getMinimumSuitCard(minSuitWithCount.second)
                                }
                            } else {
                                getMinimumSuitCard(firstPlayerCard.suit)
                            }
                        }
                    }
                    else {
                        return checkOpponentEmptySuitAndEnterRemainingSuitCard()
                    }
                }
                // Two teammates have played
                else if (teammates.size == 2) {
                    val (firstPlayer, firstCard) = teammates[0]
                    val (secondPlayer, secondCard) = teammates[1]

                    if (!isTrumpCardDeclared) {
                        val allSameTeam = listOf(firstPlayer, secondPlayer).all { it.isMyTeammate == currPlayer.isMyTeammate }

                        if (isHide) {
                            if (currPlayer.playerGameDetails.countCardsBySuit(firstCard.suit) > 2) {
                                currPlayer.playerGameDetails.getMinCard(firstCard.suit, excludeCardRank = 10)
                            }
                            else {
                                if (currPlayer.playerGameDetails.isSuitExist(firstCard.suit))
                                    currPlayer.playerGameDetails.getMinCard(firstCard.suit, excludeCardRank = 10)
                                else if (currPlayer.playerGameDetails.isSuitExist(secondCard.suit))
                                    currPlayer.playerGameDetails.getMinCard(secondCard.suit, excludeCardRank = 10)
                                else if (isLastHighestCard(highestCardOfMaxSuit))
                                    highestCardOfMaxSuit
                                else
                                    lowestCardOfMaxSuit
                            }
                        }
                        else {
                            return if (allSameTeam) {
                                when {
                                    firstCard.suit == secondCard.suit && currPlayer.playerGameDetails.isSuitExist(firstCard.suit) -> getMinimumSuitCard(firstCard.suit)
                                    currPlayer.playerGameDetails.isSuitExist(secondCard.suit) -> getMinimumSuitCard(secondCard.suit)
                                    currPlayer.playerGameDetails.isSuitExist(firstCard.suit) -> getMinimumSuitCard(firstCard.suit)
                                    else -> getMinimumSuitCard(minSuitWithCount.second)
                                }
                            }
                            else {
                                val teammateCard = teammates.find { it.key.isMyTeammate == currPlayer.isMyTeammate }?.value
                                when {
                                    minSuitWithCount.first == 1 -> getMinimumSuitCard(minSuitWithCount.second)
                                    teammateCard != null && currPlayer.playerGameDetails.isSuitExist(teammateCard.suit) -> getMinimumSuitCard(teammateCard.suit)
                                    else -> getMinimumSuitCard(minSuitWithCount.second)
                                }
                            }
                        }
                    }
                    else {
                        return checkOpponentEmptySuitAndEnterRemainingSuitCard()
                    }
                }
                // More than two teammates have played
                else {
                    if (!isTrumpCardDeclared) {
                        val (firstPlayer, firstCard) = teammates[0]
                        val (secondPlayer, secondCard) = teammates[1]
                        val (thirdPlayer, thirdCard) = teammates[2]
                        val allSameTeam = listOf(firstPlayer, secondPlayer, thirdPlayer).all { it.isMyTeammate == currPlayer.isMyTeammate }

                        if (isHide) {
                            if (isHide) {
                                if (currPlayer.playerGameDetails.countCardsBySuit(firstCard.suit) > 2) {
                                    currPlayer.playerGameDetails.getMinCard(firstCard.suit, excludeCardRank = 10)
                                }
                                else {
                                    if (currPlayer.playerGameDetails.isSuitExist(firstCard.suit))
                                        currPlayer.playerGameDetails.getMinCard(firstCard.suit, excludeCardRank = 10)
                                    else if (currPlayer.playerGameDetails.isSuitExist(secondCard.suit))
                                        currPlayer.playerGameDetails.getMinCard(secondCard.suit, excludeCardRank = 10)
                                    else if (currPlayer.playerGameDetails.isSuitExist(thirdCard.suit))
                                        currPlayer.playerGameDetails.getMinCard(thirdCard.suit, excludeCardRank = 10)
                                    else if (isLastHighestCard(highestCardOfMaxSuit))
                                        highestCardOfMaxSuit
                                    else
                                        lowestCardOfMaxSuit
                                }
                            }
                        }
                        else {
                            return if (allSameTeam) {
                                when {
                                    //♠ ♠ ♠
                                    firstCard.suit == secondCard.suit && secondCard.suit == thirdCard.suit && currPlayer.playerGameDetails.isSuitExist(firstCard.suit) -> getMinimumSuitCard(firstCard.suit)
                                    //♠ ♠ ♥
                                    firstCard.suit == secondCard.suit && secondCard.suit != thirdCard.suit && currPlayer.playerGameDetails.isSuitExist(firstCard.suit) -> getMinimumSuitCard(firstCard.suit)
                                    //♠ ♥ ♥
                                    firstCard.suit != secondCard.suit && secondCard.suit == thirdCard.suit && currPlayer.playerGameDetails.isSuitExist(firstCard.suit) -> getMinimumSuitCard(firstCard.suit)
                                    //♠ ♥ ♠
                                    //♠ ♥ ♦
                                    firstCard.suit != secondCard.suit && secondCard.suit != thirdCard.suit && currPlayer.playerGameDetails.isSuitExist(secondCard.suit) -> getMinimumSuitCard(secondCard.suit)
                                    currPlayer.playerGameDetails.isSuitExist(secondCard.suit) -> getMinimumSuitCard(secondCard.suit)
                                    currPlayer.playerGameDetails.isSuitExist(firstCard.suit) -> getMinimumSuitCard(firstCard.suit)
                                    currPlayer.playerGameDetails.isSuitExist(thirdCard.suit) -> getMinimumSuitCard(thirdCard.suit)
                                    else -> getMinimumSuitCard(minSuitWithCount.second)
                                }
                            }
                            else {
                                val teammateCard = teammates.find { it.key.isMyTeammate == currPlayer.isMyTeammate }?.value
                                when {
                                    minSuitWithCount.first == 1 -> getMinimumSuitCard(minSuitWithCount.second)
                                    teammateCard != null && currPlayer.playerGameDetails.isSuitExist(teammateCard.suit) -> getMinimumSuitCard(teammateCard.suit)
                                    else -> getMinimumSuitCard(minSuitWithCount.second)
                                }
                            }
                        }
                    }
                    else {
                        return checkOpponentEmptySuitAndEnterRemainingSuitCard()
                    }
                }
                return getMinimumSuitCard()
            }

            card = getMinimumCardSuitUsingPlayersCard()
        }
        else {
            card = currPlayer.playerGameDetails.getMaxCard(currRoundSuit)
            val isCurrentSuitExist = currPlayer.playerGameDetails.isSuitExist(currRoundSuit)

            val enteredHighestCardPlayer = getHighestCardPlayer(enteredCardMap)
            enteredHighestCardPlayer.playerGameDetails.getMinimumCard().suit

            val lastPlayerThatDontHasThatSuit = getLastPlayerThatDontHaveThatSuit(currRoundSuit)
            val isOtherPlayerHasCurrRoundSuitOrEmptySuitPlayerIsTeammate = lastPlayerThatDontHasThatSuit == null || lastPlayerThatDontHasThatSuit.isMyTeammate == currPlayer.isMyTeammate

            val enteredHighestPlayerCard = enteredHighestCardPlayer.playerGameDetails.enteredCardList.last()

            val isHighestCardPlayerTeammate = enteredHighestCardPlayer.isMyTeammate == currPlayer.isMyTeammate
            val isLastPlayerIsCurrPlayer = lastPlayer.uId == currPlayer.uId
            val isLastPlayerTeammate = lastPlayer.isMyTeammate == currPlayer.isMyTeammate

            val hasTenOfCurrRoundSuit = currPlayer.playerGameDetails.isCardPresent(currRoundSuit, 10)
            val mindiOfCurrRoundSuit = currPlayer.playerGameDetails.getSpecificCard(currRoundSuit, 10)
            val currRoundHasMindi = isRankEntered(10)
            val isEnteredHighestPlayerCardIsLastHighestCard = isLastHighestCard(enteredHighestPlayerCard, compareWithCurrPlayersCard = true)

            val higherRankOfCurrRoundSuitCard = currPlayer.playerGameDetails.getNextCard(currRoundSuit, enteredHighestPlayerCard.rank, higherRank = true, excludeCardRank = 10)
            val highestRankOfCurrRoundSuitCardExclude10 = currPlayer.playerGameDetails.getMaxCard(currRoundSuit, excludeCardRank = 10)
            val smallestCardExclude10 = currPlayer.playerGameDetails.getMinCard(currRoundSuit, excludeCardRank = 10)

            if (isTrumpCardDeclared) {
                if (isCurrentSuitExist) {
                    fun getMindiCardIfPresentOrMin(suit: SuitType): Card {
                        return if (isOtherPlayerHasCurrRoundSuitOrEmptySuitPlayerIsTeammate && currPlayer.playerGameDetails.isCardPresent(suit, 10))
                            currPlayer.playerGameDetails.getSpecificCard(suit, 10)!!
                        else
                            currPlayer.playerGameDetails.getMinCard(suit, excludeCardRank = 10)
                    }

                    fun handleHighestCardPlayerIsTeammateWithSameSuit() {
                        card = when {
                            isLastPlayerIsCurrPlayer -> {
                                printMessage("--> same suit, suit exists, Teammate highest, last == current")
                                getMindiCardIfPresentOrMin(currRoundSuit)
                            }

                            isLastPlayerTeammate -> {
                                printMessage("--> same suit, suit exists, Teammate highest, last == teammate")
                                if (lastPlayerThatDontHasThatSuit == null) {
                                    if (currRoundHasMindi) {
                                        if (highestRankOfCurrRoundSuitCardExclude10.rank > enteredHighestPlayerCard.rank) {
                                            highestRankOfCurrRoundSuitCardExclude10
                                        } else {
                                            smallestCardExclude10
                                        }
                                    }
                                    else {
                                        smallestCardExclude10
                                    }
                                }
                                else if (lastPlayerThatDontHasThatSuit.isMyTeammate == currPlayer.isMyTeammate) {
                                    mindiOfCurrRoundSuit
                                        ?: if (highestRankOfCurrRoundSuitCardExclude10.rank > enteredHighestPlayerCard.rank) {
                                            highestRankOfCurrRoundSuitCardExclude10
                                        } else {
                                            smallestCardExclude10
                                        }
                                }
                                else {
                                    if (currRoundHasMindi) {
                                        if (highestRankOfCurrRoundSuitCardExclude10.rank > enteredHighestPlayerCard.rank) {
                                            highestRankOfCurrRoundSuitCardExclude10
                                        } else {
                                            smallestCardExclude10
                                        }
                                    }
                                    else {
                                        smallestCardExclude10
                                    }
                                }
                            }

                            else -> {
                                printMessage("--> same suit, suit exists, Teammate highest, last != teammate")
                                if (lastPlayerThatDontHasThatSuit == null) {
                                    if (currRoundHasMindi) {
                                        if (highestRankOfCurrRoundSuitCardExclude10.rank > enteredHighestPlayerCard.rank) {
                                            highestRankOfCurrRoundSuitCardExclude10
                                        }
                                        else {
                                            smallestCardExclude10
                                        }
                                    }
                                    else {
                                        smallestCardExclude10
                                    }
                                }
                                else if (lastPlayerThatDontHasThatSuit.isMyTeammate == currPlayer.isMyTeammate) {
                                    mindiOfCurrRoundSuit
                                        ?: if (highestRankOfCurrRoundSuitCardExclude10.rank > enteredHighestPlayerCard.rank) {
                                            highestRankOfCurrRoundSuitCardExclude10
                                        } else {
                                            smallestCardExclude10
                                        }
                                }
                                else {
                                    if (currRoundHasMindi) {
                                        if (highestRankOfCurrRoundSuitCardExclude10.rank > enteredHighestPlayerCard.rank) {
                                            highestRankOfCurrRoundSuitCardExclude10
                                        } else {
                                            smallestCardExclude10
                                        }
                                    }
                                    else {
                                        smallestCardExclude10
                                    }
                                }
                            }
                        }
                    }

                    fun handleHighestCardPlayerIsOpponentWithSameSuit() {
                        card = when {
                            isLastPlayerIsCurrPlayer -> {
                                printMessage("--> same suit, suit exists, opponent has highest card, last == current")
                                if (currRoundHasMindi && higherRankOfCurrRoundSuitCard.rank >= enteredHighestPlayerCard.rank) {
                                    higherRankOfCurrRoundSuitCard
                                }
                                else {
                                    smallestCardExclude10
                                }
                            }

                            isLastPlayerTeammate -> {
                                printMessage("--> same suit, suit exists, Opponent is highest, last == teammate")

                                if (lastPlayerThatDontHasThatSuit == null) {
                                    if (currRoundHasMindi) {
                                        if (highestRankOfCurrRoundSuitCardExclude10.rank >= enteredHighestPlayerCard.rank) {
                                            highestRankOfCurrRoundSuitCardExclude10
                                        } else {
                                            smallestCardExclude10
                                        }
                                    }
                                    else {
                                        if (higherRankOfCurrRoundSuitCard.rank >= enteredHighestPlayerCard.rank) {
                                            higherRankOfCurrRoundSuitCard
                                        } else {
                                            smallestCardExclude10
                                        }
                                    }
                                }
                                else if (lastPlayerThatDontHasThatSuit.isMyTeammate == currPlayer.isMyTeammate) {
                                    mindiOfCurrRoundSuit
                                        ?: if (highestRankOfCurrRoundSuitCardExclude10.rank >= enteredHighestPlayerCard.rank) {
                                            highestRankOfCurrRoundSuitCardExclude10
                                        } else {
                                            smallestCardExclude10
                                        }
                                }
                                else {
                                    if (highestRankOfCurrRoundSuitCardExclude10.rank >= enteredHighestPlayerCard.rank) {
                                        highestRankOfCurrRoundSuitCardExclude10
                                    } else {
                                        smallestCardExclude10
                                    }
                                }
                            }

                            else -> {
                                printMessage("--> same suit, suit exists, Opponent is highest, last != teammate")

                                if (lastPlayerThatDontHasThatSuit == null) {
                                    if (higherRankOfCurrRoundSuitCard.rank >= enteredHighestPlayerCard.rank) {
                                        higherRankOfCurrRoundSuitCard
                                    } else {
                                        smallestCardExclude10
                                    }
                                }
                                else if (lastPlayerThatDontHasThatSuit.isMyTeammate == currPlayer.isMyTeammate) {
                                    mindiOfCurrRoundSuit
                                        ?: if (higherRankOfCurrRoundSuitCard.rank >= enteredHighestPlayerCard.rank) {
                                            higherRankOfCurrRoundSuitCard
                                        } else {
                                            smallestCardExclude10
                                        }
                                }
                                else {
                                    if (currRoundHasMindi) {
                                        if (highestRankOfCurrRoundSuitCardExclude10.rank >= enteredHighestPlayerCard.rank) {
                                            highestRankOfCurrRoundSuitCardExclude10
                                        } else {
                                            smallestCardExclude10
                                        }
                                    }
                                    else {
                                        if (higherRankOfCurrRoundSuitCard.rank >= enteredHighestPlayerCard.rank) {
                                            higherRankOfCurrRoundSuitCard
                                        } else {
                                            smallestCardExclude10
                                        }
                                    }
                                }
                            }
                        }
                    }

                    fun handleHighestCardPlayerIsTeammateWithDiffSameSuit() {
                        card = when {
                            isLastPlayerIsCurrPlayer -> {
                                printMessage("--> suit diff, suit exists, Teammate is highest, last == current")
                                getMindiCardIfPresentOrMin(currRoundSuit)
                            }

                            isLastPlayerTeammate -> {
                                printMessage("--> suit diff, suit exists, Teammate is highest, last == teammate")

                                if (lastPlayerThatDontHasThatSuit == null) {
                                    mindiOfCurrRoundSuit ?: smallestCardExclude10
                                }
                                else if (lastPlayerThatDontHasThatSuit.isMyTeammate == currPlayer.isMyTeammate) {
                                    mindiOfCurrRoundSuit ?: smallestCardExclude10
                                }
                                else {
                                    smallestCardExclude10
                                }
                            }

                            else -> {
                                printMessage("--> suit diff, suit exists, Teammate is highest, last != teammate")
                                if (lastPlayerThatDontHasThatSuit == null) {
                                    mindiOfCurrRoundSuit ?: smallestCardExclude10
                                }
                                else if (lastPlayerThatDontHasThatSuit.isMyTeammate == currPlayer.isMyTeammate) {
                                    mindiOfCurrRoundSuit ?: smallestCardExclude10
                                }
                                else {
                                    smallestCardExclude10
                                }
                            }
                        }
                    }

                    fun handleHighestCardPlayerIsOpponentWithDiffSameSuit() {
                        card = when {
                            isLastPlayerIsCurrPlayer -> {
                                printMessage("--> suit diff, suit exist, Opponent is highest, last == current")
                                smallestCardExclude10
                            }

                            isLastPlayerTeammate -> {
                                printMessage("--> suit diff, suit exist, Opponent is highest, last == teammate")
                                if (lastPlayerThatDontHasThatSuit == null) {
                                    smallestCardExclude10
                                }
                                else if (lastPlayerThatDontHasThatSuit.isMyTeammate == currPlayer.isMyTeammate) {
                                    if (isEnteredHighestPlayerCardIsLastHighestCard) smallestCardExclude10 else (mindiOfCurrRoundSuit ?: smallestCardExclude10)
                                }
                                else {
                                    smallestCardExclude10
                                }
                            }

                            else -> {
                                printMessage("--> suit diff, suit exist, Opponent is highest, last != teammate")
                                smallestCardExclude10
                            }
                        }
                    }

                    if (enteredHighestPlayerCard.suit == currRoundSuit) {
                        if (isHighestCardPlayerTeammate) {
                            handleHighestCardPlayerIsTeammateWithSameSuit()
                        } else {
                            handleHighestCardPlayerIsOpponentWithSameSuit()
                        }
                    }
                    else {
                        if (isHighestCardPlayerTeammate) {
                            handleHighestCardPlayerIsTeammateWithDiffSameSuit()
                        } else {
                            handleHighestCardPlayerIsOpponentWithDiffSameSuit()
                        }
                    }
                }
                else {
                    val minSuit = currPlayer.playerGameDetails.getMinimumSuit(excludeSuit = hukum.suit)
                    val minCardOfMinSuitExclude10 = currPlayer.playerGameDetails.getMinCard(minSuit, excludeCardRank = 10)
                    fun getMinCardExcludeHukum(): Card {
                        val minSuitList = currPlayer.playerGameDetails.getSortedSuitList(ascending = true, excludeSuit = hukum.suit)
                        val maxSuitList = currPlayer.playerGameDetails.getSortedSuitList(ascending = false, excludeSuit = hukum.suit)
                        for (suit in minSuitList) {
                            val minCard = currPlayer.playerGameDetails.getMinCard(suit, excludeCardRank = 10)
                            //not 10 && <= Queen
                            val isOtherCard = currPlayer.playerGameDetails.getCardsNotMatchingAndNotHigherThan(suit, 10, 12)

                            if (currPlayer.playerGameDetails.isSingleCardOfThatSuit(suit) && minCard.rank != 10 && minCard.rank < 13) {
                                return minCard
                            }
                            if (isOtherCard != null) {
                                return minCard
                            }
                        }

                        for (suit in maxSuitList) {
                            val minCard = currPlayer.playerGameDetails.getMinCard(suit, excludeCardRank = 10)
                            //not 10 && <= Queen
                            val isOtherCard = currPlayer.playerGameDetails.getCardsNotMatchingAndNotHigherThan(suit, 10, 14)
                            if (currPlayer.playerGameDetails.isSingleCardOfThatSuit(suit) && minCard.rank != 10) {
                                return minCard
                            }
                            if (isOtherCard != null) {
                                return minCard
                            }
                        }

                        return minCardOfMinSuitExclude10
                    }

                    fun getOtherSuitMindi(): Card? {
                        val listOfMindi = currPlayer.playerGameDetails.getCardsListByRank(10, excludeSuit = hukum.suit)
                        return if (listOfMindi.isNotEmpty())
                            listOfMindi[0]
                        else
                            null
                    }

                    fun getHukumMindi(): Card? {
                        return if (currPlayer.playerGameDetails.isSuitExist(hukum.suit))
                            currPlayer.playerGameDetails.getSpecificCard(hukum.suit, 10)
                        else
                            null
                    }

                    val minRankCardOfHukumExcludeMindi = currPlayer.playerGameDetails.getMinCard(hukum.suit, excludeCardRank = 10)
                    val otherSuitMindiCard = currPlayer.playerGameDetails.getCardsListByRank(10, excludeSuit = hukum.suit).firstOrNull()
                    val hukumMindiCard = currPlayer.playerGameDetails.getSpecificCard(hukum.suit, 10)
                    val hasHukum = currPlayer.playerGameDetails.isTrumpCardExist
                    if (enteredHighestPlayerCard.suit == currRoundSuit) {
                        fun getHukumCard(): Card {
                            return if (currPlayer.playerGameDetails.isCardPresent(hukum.suit, 10))
                                currPlayer.playerGameDetails.getSpecificCard(hukum.suit, 10)!!
                            else
                                minRankCardOfHukumExcludeMindi
                        }

                        fun getHukumMinCardExclude10(): Card? {
                            return if (currPlayer.playerGameDetails.isSuitExist(hukum.suit)) {
                                val minCard = currPlayer.playerGameDetails.getMinCard(hukum.suit, excludeCardRank = 10)
                                if (minCard.rank < 10)
                                    minCard
                                else null
                            }
                            else
                                null
                        }

                        card = when {
                            isHighestCardPlayerTeammate -> {
                                when {
                                    isLastPlayerIsCurrPlayer -> {
                                        printMessage("--> same suit, suit not exist, teammate highest, last == currPlayer")
                                        otherSuitMindiCard ?: (hukumMindiCard ?: getMinCardExcludeHukum())
                                    }

                                    isLastPlayerTeammate -> {
                                        printMessage("--> same suit, suit not exist, teammate highest, last == teammate")
                                        if (lastPlayerThatDontHasThatSuit == null) {
                                            if (hasHukum)
                                                getHukumMindi() ?: getHukumCard()
                                            else
                                                getMinCardExcludeHukum()
                                        }
                                        else if (lastPlayerThatDontHasThatSuit.isMyTeammate == currPlayer.isMyTeammate) {
                                            otherSuitMindiCard
                                                ?: (hukumMindiCard ?: getMinCardExcludeHukum())
                                        }
                                        else {
                                            if (hasHukum) {
                                                if (currRoundHasMindi) {
                                                    val maxCard = currPlayer.playerGameDetails.getMaxCard(hukum.suit, excludeCardRank = 10)
                                                    val minCard = currPlayer.playerGameDetails.getMinCard(hukum.suit, excludeCardRank = 10)
                                                    if (maxCard.rank > 10)
                                                        maxCard
                                                    else if (minCard.rank < 10)
                                                        minCard
                                                    else
                                                        getMinCardExcludeHukum()
                                                }
                                                else {
                                                    val higherRankCard = currPlayer.playerGameDetails.getNextCard(hukum.suit, 11, higherRank = true)
                                                    if (higherRankCard.rank > 10)
                                                        higherRankCard
                                                    else
                                                        getMinCardExcludeHukum()
                                                }
                                            }
                                            else
                                                getMinCardExcludeHukum()
                                        }
                                    }

                                    else -> {
                                        printMessage("--> same suit, suit not exist, teammate highest, last != teammate")
                                        if (enteredHighestPlayerCard.rank <= 12) {
                                            if (currRoundHasMindi) {
                                                if (lastPlayerThatDontHasThatSuit == null)
                                                    getHukumMinCardExclude10() ?: getMinCardExcludeHukum()
                                                else if (lastPlayerThatDontHasThatSuit.isMyTeammate == GameHelper.getCurrentTurnPlayer().isMyTeammate)
                                                    getMinCardExcludeHukum()
                                                else {
                                                    val higherCard = currPlayer.playerGameDetails.getNextCard(hukum.suit, 11, higherRank = true, excludeCardRank = 10)
                                                    if (higherCard.rank >= 11)
                                                        higherCard
                                                    else
                                                        getMinCardExcludeHukum()
                                                }
                                            }
                                            else {
                                                if (lastPlayerThatDontHasThatSuit == null)
                                                    getHukumMinCardExclude10() ?: getMinCardExcludeHukum()
                                                else if (lastPlayerThatDontHasThatSuit.isMyTeammate == GameHelper.getCurrentTurnPlayer().isMyTeammate) {
                                                    val minSuit = currPlayer.playerGameDetails.getSortedSuitList(ascending = true, excludeSuit = hukum.suit)
                                                    if (currPlayer.playerGameDetails.isSingleCardOfThatSuit(minSuit[0]))
                                                        currPlayer.playerGameDetails.getMinCard(minSuit[0])
                                                    else
                                                        getMinCardExcludeHukum()
                                                }
                                                else {
                                                    val higherCard = currPlayer.playerGameDetails.getNextCard(hukum.suit, 11, higherRank = true, excludeCardRank = 10)
                                                    if (higherCard.rank >= 11)
                                                        higherCard
                                                    else
                                                        getMinCardExcludeHukum()
                                                }
                                            }
                                        }
                                        else
                                            getMinCardExcludeHukum()
                                    }
                                }
                            }
                            else -> { // Opponent has highest card
                                when {
                                    isLastPlayerIsCurrPlayer -> {
                                        printMessage("--> same suit, suit not exist, opponent highest, last == currPlayer")
                                        if (hasHukum) {
                                            if (currPlayer.playerGameDetails.isCardPresent(hukum.suit, 10))
                                                currPlayer.playerGameDetails.getSpecificCard(hukum.suit, 10)!!
                                            else {
                                                if (currRoundHasMindi)
                                                    getHukumCard()
                                                else
                                                    getMinCardExcludeHukum()
                                            }
                                        }
                                        else {
                                            getMinCardExcludeHukum()
                                        }
                                    }

                                    isLastPlayerTeammate -> {
                                        printMessage("--> same suit, suit not exist, opponent highest, last == teammate")
                                        if (lastPlayerThatDontHasThatSuit == null) {
                                            if (hasHukum)
                                                getHukumMindi() ?: getHukumCard()
                                            else
                                                getMinCardExcludeHukum()
                                        }
                                        else if (lastPlayerThatDontHasThatSuit.isMyTeammate == currPlayer.isMyTeammate) {
                                            if (hasHukum)
                                                getHukumMindi() ?: getOtherSuitMindi() ?: getHukumCard()
                                            else
                                                getOtherSuitMindi() ?: getMinCardExcludeHukum()
                                        }
                                        else {
                                            val otherSuitCard = getMinCardExcludeHukum()
                                            if (otherSuitCard.rank == 10)
                                                getHukumMinCardExclude10() ?: currPlayer.playerGameDetails.getMinimumCard()
                                            else
                                                otherSuitCard
                                        }
                                    }

                                    else -> {
                                        printMessage("--> same suit, suit not exist, opponent highest, last != teammate")
                                        if (lastPlayerThatDontHasThatSuit == null) {
                                            if (hasHukum)
                                                getHukumMindi() ?: getHukumCard()
                                            else
                                                getMinCardExcludeHukum()
                                        }
                                        else if (lastPlayerThatDontHasThatSuit.isMyTeammate == currPlayer.isMyTeammate) {
                                            if (hasHukum)
                                                getHukumMindi() ?: getOtherSuitMindi() ?: getHukumCard()
                                            else
                                                getOtherSuitMindi() ?: getMinCardExcludeHukum()
                                        }
                                        else {
                                            if (hasHukum) {
                                                if (currRoundHasMindi) {
                                                    val maxCard = currPlayer.playerGameDetails.getMaxCard(hukum.suit, excludeCardRank = 10)
                                                    val minCard = currPlayer.playerGameDetails.getMinCard(hukum.suit, excludeCardRank = 10)
                                                    if (maxCard.rank > 10)
                                                        maxCard
                                                    else if (minCard.rank < 10)
                                                        minCard
                                                    else
                                                        getMinCardExcludeHukum()
                                                }
                                                else {
                                                    val higherRankCard = currPlayer.playerGameDetails.getNextCard(hukum.suit, 11, higherRank = true)
                                                    if (higherRankCard.rank > 10)
                                                        higherRankCard
                                                    else
                                                        getMinCardExcludeHukum()
                                                }
                                            }
                                            else
                                                getMinCardExcludeHukum()
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else {
                        fun getHukumCardHigherThanGiven(rank: Int): Card? {
                            return if (currPlayer.playerGameDetails.isSuitExist(hukum.suit)) {
                                val higherCard = currPlayer.playerGameDetails.getNextCard(hukum.suit, rank, higherRank = true)
                                if (higherCard.rank >= enteredHighestPlayerCard.rank)
                                    higherCard
                                else
                                    null
                            }
                            else
                                null
                        }

                        fun playWhenTeammateHighest(): Card {
                            return when {
                                isLastPlayerIsCurrPlayer -> {
                                    printMessage("--> suit diff, suit not exist, teammate highest, lastPlayer == currPlayer")
                                    otherSuitMindiCard
                                        ?: (hukumMindiCard ?: getMinCardExcludeHukum())
                                }

                                isLastPlayerTeammate -> {
                                    printMessage("--> suit diff, suit not exist, teammate highest, lastPlayer == teammate")

                                    if (lastPlayerThatDontHasThatSuit == null) {
                                        otherSuitMindiCard ?: getMinCardExcludeHukum()
                                    }
                                    else if (lastPlayerThatDontHasThatSuit.isMyTeammate == currPlayer.isMyTeammate) {
                                        otherSuitMindiCard ?: getMinCardExcludeHukum()
                                    }
                                    else {
                                        if (hasHukum) {
                                            val maxCard = currPlayer.playerGameDetails.getMaxCard(hukum.suit, excludeCardRank = 10)
                                            if (currRoundHasMindi && (maxCard.rank >= 13 && enteredHighestPlayerCard.rank < 12)) {
                                                maxCard
                                            }
                                            else {
                                                getMinCardExcludeHukum()
                                            }
                                        }
                                        else
                                            getMinCardExcludeHukum()
                                    }
                                }

                                else -> {
                                    printMessage("--> suit diff, suit not exist, teammate highest, lastPlayer != teammate")

                                    if (lastPlayerThatDontHasThatSuit == null) {
                                        otherSuitMindiCard ?: getMinCardExcludeHukum()
                                    }
                                    else if (lastPlayerThatDontHasThatSuit.isMyTeammate == currPlayer.isMyTeammate) {
                                        otherSuitMindiCard ?: getMinCardExcludeHukum()
                                    }
                                    else {
                                        if (hasHukum) {
                                            val maxCard = currPlayer.playerGameDetails.getMaxCard(hukum.suit, excludeCardRank = 10)
                                            if (currRoundHasMindi && (maxCard.rank >= 13 && enteredHighestPlayerCard.rank < 12)) {
                                                maxCard
                                            }
                                            else {
                                                getMinCardExcludeHukum()
                                            }
                                        }
                                        else
                                            getMinCardExcludeHukum()

                                    }
                                }
                            }
                        }

                        fun playWhenOpponentHighest(): Card {
                            var isHigherCardPossible = false
                            if (lastPlayerThatDontHasThatSuit != null) {
                                for (card in lastPlayerThatDontHasThatSuit.playerGameDetails.enteredCardList) {
                                    if (card.suit == hukum.suit && card.rank >= 12) {
                                        if (!isLastHighestCard(card, compareWithCurrPlayersCard = true)) {
                                            isHigherCardPossible = true
                                            break
                                        }
                                    }
                                }
                            }

                            return when {
                                isLastPlayerIsCurrPlayer -> {
                                    printMessage("--> suit diff, suit not exist, opponent highest, lastPlayer == currPlayer")
                                    if (enteredHighestPlayerCard.rank < 11) {
                                        getHukumMindi() ?: getMinCardExcludeHukum()
                                    } else {
                                        if (currRoundHasMindi)
                                            getHukumCardHigherThanGiven(enteredHighestPlayerCard.rank) ?: getMinCardExcludeHukum()
                                        else {
                                            getMinCardExcludeHukum()
                                        }
                                    }
                                }

                                isLastPlayerTeammate -> {
                                    printMessage("--> suit diff, suit not exist, opponent highest, lastPlayer == teammate")
                                    if (lastPlayerThatDontHasThatSuit == null) {
                                        getHukumCardHigherThanGiven(enteredHighestPlayerCard.rank) ?: getMinCardExcludeHukum()
                                    }
                                    else if (lastPlayerThatDontHasThatSuit.isMyTeammate == currPlayer.isMyTeammate) {
                                        if (hasHukum) {
                                            getHukumCardHigherThanGiven(enteredHighestPlayerCard.rank) ?: if (isHigherCardPossible) getOtherSuitMindi() ?: getMinCardExcludeHukum() else getMinCardExcludeHukum()
                                        }
                                        else
                                            if (isHigherCardPossible) getOtherSuitMindi() ?: getMinCardExcludeHukum() else getMinCardExcludeHukum()
                                    }
                                    else {
                                        if (hasHukum) {
                                            val maxCard = currPlayer.playerGameDetails.getMaxCard(hukum.suit, excludeCardRank = 10)
                                            if (maxCard.rank != 10 && maxCard.rank >= enteredHighestPlayerCard.rank)
                                                maxCard
                                            else
                                                getMinCardExcludeHukum()
                                        }
                                        else
                                            getMinCardExcludeHukum()
                                    }
                                }

                                else -> {
                                    printMessage("--> suit diff, suit not exist, opponent highest, lastPlayer != teammate")
                                    if (lastPlayerThatDontHasThatSuit == null) {
                                        getHukumCardHigherThanGiven(enteredHighestPlayerCard.rank) ?: getMinCardExcludeHukum()
                                    }
                                    else if (lastPlayerThatDontHasThatSuit.isMyTeammate == currPlayer.isMyTeammate) {
                                        if (hasHukum) {
                                            getHukumCardHigherThanGiven(enteredHighestPlayerCard.rank) ?: if (isHigherCardPossible) getOtherSuitMindi() ?: getMinCardExcludeHukum() else getMinCardExcludeHukum()
                                        }
                                        else
                                            if (isHigherCardPossible) getOtherSuitMindi() ?: getMinCardExcludeHukum() else getMinCardExcludeHukum()
                                    }
                                    else {
                                        if (hasHukum) {
                                            val maxCard = currPlayer.playerGameDetails.getMaxCard(hukum.suit, excludeCardRank = 10)
                                            if (maxCard.rank != 10 && maxCard.rank >= enteredHighestPlayerCard.rank)
                                                maxCard
                                            else
                                                getMinCardExcludeHukum()
                                        }
                                        else
                                            getMinCardExcludeHukum()
                                    }
                                }
                            }
                        }

                        card = if (isHighestCardPlayerTeammate) playWhenTeammateHighest() else playWhenOpponentHighest()
                    }
                }
            }
            else {
                if (isCurrentSuitExist) {
                    val minRankCard = getMinimumSuitCard(currRoundSuit)
                    fun getCardWhenTeammateIsHighest(): Card {
                        when {
                            isLastPlayerIsCurrPlayer -> {
                                printMessage("--> !hukum.isDeclared, Teammate highest, last player is current player")
                                return if (hasTenOfCurrRoundSuit)
                                    currPlayer.playerGameDetails.getSpecificCard(currRoundSuit, 10)!!
                                else
                                    minRankCard
                            }

                            isLastPlayerTeammate -> {
                                printMessage("--> !hukum.isDeclared, Teammate highest, last player is teammate")
                                if (currRoundHasMindi) {
                                    return if ((enteredHighestPlayerCard.rank == 14 && isEnteredHighestPlayerCardIsLastHighestCard && currPlayer.playerGameDetails.isCardPresent(currRoundSuit, 10))) {
                                        currPlayer.playerGameDetails.getSpecificCard(currRoundSuit, 10)!!
                                    } else {
                                        if (highestRankOfCurrRoundSuitCardExclude10.rank > enteredHighestPlayerCard.rank) {
                                            highestRankOfCurrRoundSuitCardExclude10
                                        } else {
                                            smallestCardExclude10
                                        }
                                    }
                                }
//                                else if (hasTenOfCurrRoundSuit)
//                                    return return if (isEnteredHighestPlayerCardIsLastHighestCard && currPlayer.playerGameDetails.isCardPresent(currRoundSuit, 10)) {
//                                        currPlayer.playerGameDetails.getSpecificCard(currRoundSuit, 10)!!
//                                    }
//                                    else {
//                                        smallestCardExclude10
//                                    }
                                else
                                    return minRankCard
                            }

                            else -> {
                                printMessage("--> !hukum.isDeclared, Teammate highest, last player is opponent")
                                return if (currRoundHasMindi)
                                    getHighestRankCardWhenMindiEntered(enteredHighestPlayerCard, isHighestEnteredPlayerIsTeammate = true)
                                else
                                    minRankCard
                            }
                        }
                    }

                    fun getCardWhenOpponentIsHighest(): Card {
                        return when {
                            isLastPlayerIsCurrPlayer -> {
                                printMessage("--> !hukum.isDeclared, Opponent is highest, last player is current player")
                                if (higherRankOfCurrRoundSuitCard.rank >= enteredHighestPlayerCard.rank)
                                    higherRankOfCurrRoundSuitCard
                                else
                                    smallestCardExclude10
                            }

                            isLastPlayerTeammate -> {
                                printMessage("--> !hukum.isDeclared, Opponent is highest, last player is teammate")
                                if (currRoundHasMindi) {
                                    getHighestRankCardWhenMindiEntered(enteredHighestPlayerCard)
                                }
                                else {
                                    if (enteredHighestPlayerCard.rank == 14 && higherRankOfCurrRoundSuitCard.rank >= enteredHighestPlayerCard.rank) {
                                        higherRankOfCurrRoundSuitCard
                                    }
                                    else {
                                        if (hasTenOfCurrRoundSuit && !isEnteredHighestPlayerCardIsLastHighestCard && currPlayer.playerGameDetails.isCardPresent(currRoundSuit, 10)) {
                                            currPlayer.playerGameDetails.getSpecificCard(currRoundSuit, 10)!!
                                        }
                                        else {
                                            val nextHighestCard = currPlayer.playerGameDetails.getNextCard(currRoundSuit, enteredHighestPlayerCard.rank, higherRank = true, excludeCardRank = 10)
                                            if (nextHighestCard.rank >= enteredHighestPlayerCard.rank) {
                                                nextHighestCard
                                            }
                                            else {
                                                smallestCardExclude10
                                            }
                                        }
                                    }
                                }
                            }

                            else -> {
                                printMessage("--> !hukum.isDeclared, Opponent is highest, last player is opponent")
                                if (currRoundHasMindi) {
                                    getHighestRankCardWhenMindiEntered(enteredHighestPlayerCard)
                                } else {
                                    if (higherRankOfCurrRoundSuitCard.rank >= enteredHighestPlayerCard.rank)
                                        higherRankOfCurrRoundSuitCard
                                    else
                                        minRankCard
                                }
                            }
                        }
                    }

                    card = if (isHighestCardPlayerTeammate) {
                        getCardWhenTeammateIsHighest()
                    }
                    else {
                        getCardWhenOpponentIsHighest()
                    }
                }
                else {
                    printMessage("-->> hukum Empty, currRoundSuit notExist --> setHukum")
                    card = compareCardsAndGetHukum()
                }
            }
        }

        return card
    }

    fun getHighestCardPlayer(decide1stTurnList: LinkedHashMap<PlayerDetails, Card>, excludeTrumpCard: Boolean = false): PlayerDetails {
        val hukum = GameHelper.trumpCardSuit
        var highestCard: Card? = null
        var highestPlayer: PlayerDetails? = null

        val firstCardSuit = decide1stTurnList.entries.first().value.suit

        val trumpCards = decide1stTurnList.filterValues { it.suit == hukum.suit }
        val sameSuitCards = decide1stTurnList.filterValues { it.suit == firstCardSuit }

        val candidateCards = if (excludeTrumpCard) {
            when {
                sameSuitCards.isNotEmpty() -> { sameSuitCards }
                else -> { decide1stTurnList } // If no trump or same suit cards, take all
            }
        }
        else {
            when {
                trumpCards.isNotEmpty() -> { trumpCards }
                sameSuitCards.isNotEmpty() -> { sameSuitCards }
                else -> { decide1stTurnList } // If no trump or same suit cards, take all
            }
        }

        candidateCards.entries.forEach { (player, card) ->
            val currentHighest = highestCard?.rank ?: -1

            // Choose the highest-ranked card, or if same rank, choose the last entered
            if (card.rank >= currentHighest) {
                highestCard = card
                highestPlayer = player
            }
        }
        return highestPlayer!!
    }

    fun printMessage(message: String) {
//        val lastPlayerThatDontHasThatSuit = getLastPlayerThatDontHaveThatSuit(currRoundSuit)
//        if (lastPlayerThatDontHasThatSuit != null) {
//            Off_DesignGame.INSTANCE.selectLastPlayerThatDontHasCurrRoundSuit(lastPlayerThatDontHasThatSuit.playerIndex)
//        }
//        MainHelper.INSTANCE.printMessage(message)
    }

    fun getLastPlayerThatDontHaveThatSuit(suit: SuitType, playerIndex: Int = GameHelper.getCurrTurnIndex()): PlayerDetails? {
        val playersList = GameHelper.playerDetailsList
        val listOfPlayer = ArrayList<PlayerDetails>()
        for ((index, player) in playersList.withIndex()) {
            if (index > playerIndex) {
                if (player.playerGameDetails.emptySuitList.find { it == suit } != null && player.playerGameDetails.isTrumpCardExist) {
                    listOfPlayer.add(player)
                }
            }
        }
        return if (listOfPlayer.isNotEmpty())
            listOfPlayer.last()
        else
            null
    }

    fun countTotalRoundsOfSuit(suit: SuitType): Int {
        return GameHelper.allEnteredCardMap.count { round: LinkedHashMap<PlayerDetails, Card> ->
            round.values.first().suit == suit
        }
    }

    fun isLastHighestCard(targetCard: Card, compareWithCurrPlayersCard: Boolean = false): Boolean {
        val currPlayer = GameHelper.getCurrentTurnPlayer()
        fun getComparableRank(rank: Int): Int = if (rank == 1) 14 else rank

        val targetSuit = targetCard.suit
        val targetRank = getComparableRank(targetCard.rank)
        val deckCount = currDeckType.deckCount

        // 1. Check higher-ranked cards of the same suit
        val higherRanks = (2..13).plus(1)
            .map { getComparableRank(it) }
            .filter { it > targetRank }

        for (rank in higherRanks) {
            val actualRank = if (rank == 14) 1 else rank
            val count = GameHelper.allEnteredCardMap.sumOf { round ->
                round.values.count { it.suit == targetSuit && it.rank == actualRank }
            }
            if (count < deckCount) return false // Not all higher cards are used up
        }

        // 2. Check if current card has been entered (deckCount - 1) times
        val enteredCardCount = GameHelper.allEnteredCardMap.sumOf { round ->
            round.values.count { it.suit == targetSuit && it.rank == targetCard.rank }
        }
        if (compareWithCurrPlayersCard) {
            val numberOfHighestRankCard = currPlayer.playerGameDetails.getCardsListByRank(targetCard.rank, suit = targetCard.suit).size
            return enteredCardCount + numberOfHighestRankCard == currDeckType.deckCount
        }

        return enteredCardCount == deckCount - 1
    }


}