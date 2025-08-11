package com.keylogic.mindi.gamePlay.helper

import android.content.Context
import android.os.Bundle
import com.google.gson.Gson
import com.keylogic.mindi.R
import com.keylogic.mindi.database.MyPreferences
import com.keylogic.mindi.databinding.GameLayoutBinding
import com.keylogic.mindi.dialogs.GameResultFragment
import com.keylogic.mindi.gamePlay.models.PlayerProfileView
import com.keylogic.mindi.enums.SuitType
import com.keylogic.mindi.gamePlay.models.Card
import com.keylogic.mindi.gamePlay.models.CardHider
import com.keylogic.mindi.gamePlay.models.PlayerDetails
import com.keylogic.mindi.gamePlay.models.Score
import com.keylogic.mindi.gamePlay.models.TableConfig
import com.keylogic.mindi.gamePlay.models.TrumpCard
import com.keylogic.mindi.helper.CommonHelper
import com.keylogic.mindi.helper.ProfileHelper
import com.keylogic.mindi.models.ResultProfile
import com.keylogic.mindi.ui.viewModel.PlayAreaConfigViewModel
import kotlin.text.set

class GameHelper {
    companion object {
        val greenTeamIndex = 0
        val redTeamIndex = 1
        val enterCardSpeed = 250L
        lateinit var tableConfig: TableConfig
        var trumpCardSuit = TrumpCard()
        var currRoundSuitType = SuitType.NONE
        var cardHider = CardHider()
        var firstPlayerUID = ""
        var currTurnPlayerUID = ""

        var redTeamScore = Score()
        var greenTeamScore = Score()

        val playerDetailsList = ArrayList<PlayerDetails>()
        val allEnteredCardMap = ArrayList<LinkedHashMap<PlayerDetails, Card>>()
        val enteredCardMap = LinkedHashMap<PlayerDetails, Card>()

        fun getCurrTurnIndex(): Int {
            return getCurrentTurnPlayer().centerCardIndex
        }

        fun resetGameState() {
            val gameStatus = checkWinner()
            val lastGame1stPlayer = getPlayerDetails(firstPlayerUID)
            CommonHelper.print("reset --> ${lastGame1stPlayer.isMyTeammate} | $gameStatus")
            if (lastGame1stPlayer.isMyTeammate != gameStatus) {
                val index = playerDetailsList.indexOfFirst { it.uId == lastGame1stPlayer.uId }
                currTurnPlayerUID = if (index+1 == playerDetailsList.size)
                    playerDetailsList[0].uId
                else
                    playerDetailsList[index+1].uId
                firstPlayerUID = currTurnPlayerUID
            }
            else
                currTurnPlayerUID = firstPlayerUID

            val totalPlayers = tableConfig.totalPlayers
            val deckType = tableConfig.deckType
            val cards = CardGenerator.INSTANCE.generateCard(deckType, totalPlayers)
            reOrderPlayerList(currTurnPlayerUID)

            enteredCardMap.clear()
            allEnteredCardMap.clear()
            trumpCardSuit = TrumpCard()
            currRoundSuitType = SuitType.NONE
            cardHider = CardHider()

            for ((index, player) in playerDetailsList.withIndex()) {
                player.playerGameDetails.apply {
                    assignCards(cards[index])
                    isTrumpCardExist = true
                }
                CommonHelper.print("$index > ${player.name}")
            }

        }

        fun getCurrentPlayer(): PlayerDetails {
            val index = playerDetailsList.indexOfFirst { it.uId == ProfileHelper.profileUID }
            return playerDetailsList[index]
        }

        fun getPlayerDetails(uId: String): PlayerDetails {
            val index = playerDetailsList.indexOfFirst { it.uId == uId }
            return playerDetailsList[index]
        }

        fun getCurrentTurnPlayer(): PlayerDetails {
            val index = playerDetailsList.indexOfFirst { it.uId == currTurnPlayerUID }
            return playerDetailsList[index]
        }

        fun setNextPlayerTurn() {
            val index = playerDetailsList.indexOfFirst { it.uId == currTurnPlayerUID }
            if (index+1 < playerDetailsList.size)
                currTurnPlayerUID = playerDetailsList[index+1].uId
            CommonHelper.print("Next | ${playerDetailsList.size} ==> ${index+1} > ${playerDetailsList[(index+1).coerceAtMost(playerDetailsList.size-1)].name}")
        }

        fun is1stTurn(): Boolean {
            return enteredCardMap.isEmpty()
        }

        fun updatePlayerEnteredCards(card: Card) {
            getCurrentTurnPlayer().playerGameDetails.enteredCard(card)
            enteredCardMap[getCurrentTurnPlayer()] = card
        }

        fun reOrderPlayerList(uId: String) {
            val currentIndex = playerDetailsList.indexOfFirst { it.uId == uId }
            if (currentIndex < 0) {
                CommonHelper.print("reOrderPlayerList -> index = -1 ~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                return
            }

            val newList = playerDetailsList
            val orderedPlayers = newList.subList(currentIndex, newList.size) + newList.subList(0, currentIndex)
            playerDetailsList.clear()
            playerDetailsList.addAll(orderedPlayers)
        }

        fun checkAndSetTrumpOrCurrRoundSuit(card: Card?, onTrumpCardDeclared: () -> Unit) {
            if (enteredCardMap.isEmpty() && card != null) {
                currRoundSuitType = card.suit
            }
            else {
                if (tableConfig.isHideMode) {
                    if (currRoundSuitType != SuitType.NONE &&
                        !getCurrentTurnPlayer().playerGameDetails.isSuitExist() &&
                        !trumpCardSuit.isDeclared) {
                        trumpCardSuit = TrumpCard(
                            card = cardHider.card,
                            suit = cardHider.card.suit,
                            setterUID = cardHider.hiderUID,
                            inWhichSuit = currRoundSuitType,
                            isDeclared = true
                        )
                        val player = getPlayerDetails(cardHider.hiderUID)
                        player.playerGameDetails.addCard(cardHider.card)
                        onTrumpCardDeclared()
                    }
                }
                else {
                    if (card != null && currRoundSuitType != card.suit &&
                        !getCurrentTurnPlayer().playerGameDetails.isSuitExist() &&
                        !trumpCardSuit.isDeclared) {
                        trumpCardSuit = TrumpCard(
                            card = card,
                            suit = card.suit,
                            setterUID = currTurnPlayerUID,
                            inWhichSuit = currRoundSuitType,
                            isDeclared = true
                        )
                        onTrumpCardDeclared()
                    }
                }
            }
        }

        fun getHighestCardPlayer(decide1stTurnList: LinkedHashMap<PlayerDetails, Card> = enteredCardMap, excludeTrumpCard: Boolean = false): PlayerDetails {
            var highestCard: Card? = null
            var highestPlayer: PlayerDetails? = null

            val firstCardSuit = decide1stTurnList.entries.first().value.suit

            val trumpCards = decide1stTurnList.filterValues { it.suit == trumpCardSuit.suit }
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

        fun checkWinner(): Int {
            //0 = green
            //1 = red
            val greenTeam = greenTeamScore
            val redTeam = redTeamScore
            var status = -1
            val totalMindi = tableConfig.deckType.deckCount * 4
            val totalCards = tableConfig.deckType.totalCards

            val isUserCoat = greenTeam.spades == 0 && greenTeam.hearts == 0 && greenTeam.clubs == 0 && greenTeam.diamonds == 0
            val isCpuCoat = redTeam.spades == 0 && redTeam.hearts == 0 && redTeam.clubs == 0 && redTeam.diamonds == 0
            val totalOfUser = greenTeam.spades + greenTeam.hearts + greenTeam.clubs + greenTeam.diamonds
            val totalOfCpu = redTeam.spades + redTeam.hearts + redTeam.clubs + redTeam.diamonds

            if (isCpuCoat || isUserCoat) {
                status = if (totalOfUser == totalMindi)
                    greenTeamIndex
                else if (totalOfCpu == totalMindi)
                    redTeamIndex
                else
                    -1
            }
            else {
                if (totalOfUser > totalMindi / 2)
                    status = greenTeamIndex
                else if (totalOfCpu > totalMindi / 2)
                    status = redTeamIndex
                else if (totalOfUser == totalMindi / 2 && totalOfCpu == totalOfUser) {
                    if (greenTeam.hands > totalCards / 2)
                        status = greenTeamIndex
                    if (redTeam.hands > totalCards / 2)
                        status = redTeamIndex
                }
            }
//            CommonHelper.print("Check Winner --> ${(totalOfUser > totalMindi / 2)} | ${(totalOfCpu > totalMindi / 2)} " +
//                    "| ${(greenTeam.hands > totalCards / 2)} | ${(redTeam.hands > totalCards / 2)}")
            return status
        }

        fun isMindiCoat(score: Score): Boolean {
            return score.spades == 0 && score.hearts == 0 && score.clubs == 0 && score.diamonds == 0
        }

        fun updateScore(player: PlayerDetails, viewModel: PlayAreaConfigViewModel): Int {
//            CommonHelper.print(">>>>>>>>>> updateScore")
            val listOfMindi = enteredCardMap.values.filter { it.rank == 10 }
            if (player.isMyTeammate == getCurrentPlayer().isMyTeammate) {
                viewModel.updateLeftScore {
                    listOfMindi.fold(copy(hands = hands + 1)) { score, card ->
                        when (card.suit) {
                            SuitType.SPADE -> score.copy(spades = score.spades + 1)
                            SuitType.HEART -> score.copy(hearts = score.hearts + 1)
                            SuitType.CLUB -> score.copy(clubs = score.clubs + 1)
                            SuitType.DIAMOND -> score.copy(diamonds = score.diamonds + 1)
                            else -> score
                        }
                    }

                }
            }
            else {
                viewModel.updateRightScore {
                    listOfMindi.fold(copy(hands = hands + 1)) { score, card ->
                        when (card.suit) {
                            SuitType.SPADE -> score.copy(spades = score.spades + 1)
                            SuitType.HEART -> score.copy(hearts = score.hearts + 1)
                            SuitType.CLUB -> score.copy(clubs = score.clubs + 1)
                            SuitType.DIAMOND -> score.copy(diamonds = score.diamonds + 1)
                            else -> score
                        }
                    }

                }
            }
//            CommonHelper.print("---> teamIndex > ${player.isMyTeammate}")
            return listOfMindi.size
        }

        fun roundComplete(highestCardPlayer: PlayerDetails) {
            currTurnPlayerUID = highestCardPlayer.uId
            reOrderPlayerList(highestCardPlayer.uId)
            allEnteredCardMap.add(enteredCardMap)
            enteredCardMap.clear()
            currRoundSuitType = SuitType.NONE
        }

        fun getResultBundle(context: Context, gameStatus: Int): Bundle {
            val isCoat = isMindiCoat(if (gameStatus == greenTeamIndex) greenTeamScore else redTeamScore)
            val multiplyCounter = if (isCoat) 1 else 1

            val basePrice = tableConfig.betPrice * multiplyCounter

            val winnerList = ArrayList<ResultProfile>()
            val loserList = ArrayList<ResultProfile>()
            for (player in playerDetailsList) {
                val profile = ResultProfile(
                    name = player.name,
                    betAmound = tableConfig.betPrice,
                    isWinner = player.isMyTeammate == gameStatus,
                    profileImgId = player.profileId
                )
                if (gameStatus == player.isMyTeammate)
                    winnerList.add(profile)
                else
                    loserList.add(profile)
                if (ProfileHelper.profileUID == player.uId) {
                    if (profile.isWinner)
                        ProfileHelper.totalChips += basePrice + tableConfig.betPrice
//                    else if (isCoat)
//                        ProfileHelper.totalChips = (ProfileHelper.totalChips - tableConfig.betPrice).coerceAtLeast(0)
                    MyPreferences.INSTANCE.saveGameProfileDetails(context)
                }
            }

            val winnerScore = if (gameStatus == greenTeamIndex) greenTeamScore else redTeamScore
            val loserScore = if (gameStatus == greenTeamIndex) redTeamScore else greenTeamScore

            val gson = Gson()
            val bundle = Bundle().apply {
                putString(GameResultFragment.KEY_WINNER_LIST_JSON, gson.toJson(winnerList))
                putString(GameResultFragment.KEY_LOSER_LIST_JSON, gson.toJson(loserList))
                putString(GameResultFragment.KEY_WINNER_SCORE_JSON, gson.toJson(winnerScore))
                putString(GameResultFragment.KEY_LOSER_SCORE_JSON, gson.toJson(loserScore))
            }
            return bundle
        }

        fun resultScreen(context: Context, gameStatus: Int, includeLayouts: GameLayoutBinding, onAnimationEnd: () -> Unit) {
            val speed = 500L
            val winnerScore = if (gameStatus == greenTeamIndex) greenTeamScore else redTeamScore
            val totalMindi = winnerScore.spades + winnerScore.hearts + winnerScore.clubs + winnerScore.diamonds
            includeLayouts.winnerTitleImg.setImageResource(
                if (gameStatus == greenTeamIndex) R.drawable.you_are_winner else R.drawable.opponent_winner
            )

            val preFix = if (gameStatus == greenTeamIndex)
                context.getString(R.string.your)
            else
                context.getString(R.string.opponent)
            val message1 = context.getString(R.string.team_acquired_mindis, totalMindi.toString())
            val message2 = context.getString(R.string.total_hands, winnerScore.hands.toString())

            includeLayouts.winnerMessageTxt.text = "$preFix$message1 $message2"

            fun winnerTxtAnimation() {
                includeLayouts.winnerTitleImg.scaleX = 0f
                includeLayouts.winnerTitleImg.scaleY = 0f
                includeLayouts.winnerTitleImg.animate()
                    .scaleX(1.1f)
                    .scaleY(1.1f)
                    .alpha(1f)
                    .setDuration(speed)
                    .withEndAction {
                        includeLayouts.winnerTitleImg.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(speed)
                            .withEndAction {
                                onAnimationEnd()
                            }
                            .start()
                    }
                    .start()
            }

            val startXPos = DisplayHelper.screenWidth / 6f
            val startYPos = DisplayHelper.screenHeight - (DisplayHelper.screenHeight / 3f)

            includeLayouts.leftHandLayout.root.animate()
                .translationX(startXPos)
                .translationY(startYPos)
                .scaleX(1.1f)
                .scaleY(1.1f).start()

            includeLayouts.rightHandLayout.root.animate()
                .translationX(startXPos * 4f)
                .translationY(startYPos)
                .scaleX(1.1f)
                .scaleY(1.1f).start()

            includeLayouts.winnerLayout.animate().alpha(1f)
                .setDuration(speed)
                .withEndAction {
                    winnerTxtAnimation()
                }
                .start()
        }

    }

}