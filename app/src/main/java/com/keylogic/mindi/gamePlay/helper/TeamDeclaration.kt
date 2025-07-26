package com.keylogic.mindi.gamePlay.helper

import android.widget.RelativeLayout
import com.keylogic.mindi.custom.PlayerProfileView
import com.keylogic.mindi.gamePlay.models.PlayerDetails
import com.keylogic.mindi.helper.DisplayHelper.Companion.cardHeight
import com.keylogic.mindi.helper.DisplayHelper.Companion.cardWidth
import com.keylogic.mindi.helper.DisplayHelper.Companion.screenHeight
import com.keylogic.mindi.helper.DisplayHelper.Companion.screenWidth
import com.keylogic.mindi.helper.PositionHelper
import com.keylogic.mindi.models.Card
import com.keylogic.mindi.models.CardView

class TeamDeclaration {
    companion object {
        val INSTANCE = TeamDeclaration()
    }

    fun getDeclaredTeamList(noOfPlayers: Int, randomCardList: ArrayList<Card>): ArrayList<Int> {
        val updatedList = ArrayList<Int>()
        val teamAList = ArrayList<Int>()
        val allPlayerList = (0 until noOfPlayers).toList()

        var counter = 0
        main@ for (currRound in 0 until (randomCardList.size / noOfPlayers) + 1) {
            for (playerIndex in 0 until noOfPlayers) {
                if (counter >= randomCardList.size || teamAList.size == noOfPlayers / 2)
                    break@main
                if (playerIndex !in teamAList) {
                    val card = randomCardList[counter++]
                    if (card.rank == 11) {
                        teamAList.add(playerIndex)
                        if (teamAList.size == noOfPlayers / 2 &&
                            playerIndex == teamAList.last()) {
                            val teamBList = allPlayerList.filter { it !in teamAList }.toMutableList()
                            for (i in 0 until noOfPlayers / 2) {
                                updatedList.add(teamAList.removeAt(0))
                                updatedList.add(teamBList.removeAt(0))
                            }
                            return updatedList
                        }
                    }
                }
            }
        }
        return updatedList
    }

    fun teamDeclare(
        parentLayout: RelativeLayout,
        randomCardList: ArrayList<Card>,
        playerViewList: ArrayList<PlayerProfileView>,
        playerDetailsList: ArrayList<PlayerDetails>,
        onAnimationComplete: () -> Unit
    ) {
        val centerXPos = (screenWidth - cardWidth) / 2f
        val centerYPos = (screenHeight - cardHeight) / 2f
        val newList = ArrayList<Pair<Int, PlayerDetails>>()

        //get index from (playerDetailsList) using (playerViewList)
        for (index in 0 until playerViewList.size) {
            val player = playerDetailsList[index]
//            val index = playerViewList.indexOfFirst { it.playerUniqueKey == player.uniqueKey }
//            newList.add(Pair(index, player))
        }

        val totalPlayers = playerDetailsList.size
        val speed = 500L
        val teamAList = ArrayList<PlayerDetails>()

        var counter = 0
        val animation = true
        main@
        for (i in 0 until (randomCardList.size / totalPlayers)+1) {
            for (playerPair in newList) {
                if (counter >= randomCardList.size || teamAList.size == totalPlayers/2)
                    break@main
                val isAlreadyInTheTeam = teamAList.find { it.uniqueKey == playerPair.second.uniqueKey }
                if (teamAList.size < totalPlayers / 2 && isAlreadyInTheTeam == null) {
                    val card = randomCardList[counter++]
                    val pos = PositionHelper.Companion.INSTANCE.getPlayerPosition(playerPair.first+1, totalPlayers)
                    val view = CardView(parentLayout.context, card, centerXPos, centerYPos)

                    if (card.rank == 11) {
                        teamAList.add(playerPair.second)
                    }
                    if (animation) {
                        view.animate().translationX(pos[0]).translationY(pos[1]).setDuration(speed)
                            .withStartAction {
                                parentLayout.addView(view)
                            }
                            .withEndAction {
                                if (card.rank != 11)
                                    parentLayout.removeView(view)
                                if (teamAList.size == totalPlayers/2 && playerPair.second.uniqueKey == teamAList.last().uniqueKey &&
                                    card.rank == 11) {
                                    onAnimationComplete()
                                }
                            }
                            .setStartDelay((speed/2) * counter).start()
                    }
                }
            }
        }

        if (!animation) {
            onAnimationComplete()
        }
    }

}