package com.keylogic.mindi.gamePlay.animation

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.keylogic.mindi.databinding.GameLayoutBinding
import com.keylogic.mindi.gamePlay.helper.DisplayHelper
import com.keylogic.mindi.gamePlay.helper.PositionHelper
import com.keylogic.mindi.gamePlay.models.Card
import com.keylogic.mindi.gamePlay.models.CardView

class TeamDeclaration(val layout: GameLayoutBinding) {

    fun declareTeam(totalPlayers: Int, cardList: ArrayList<Card>, onAnimationEnd: (ArrayList<Int>, ArrayList<Int>) -> Unit) {
        val centerPosX = (DisplayHelper.screenWidth - DisplayHelper.cardWidth) / 2f
        val centerPosY = (DisplayHelper.screenHeight - DisplayHelper.cardHeight) / 2f
        val rela = layout.animationRelative
        rela.visibility = View.VISIBLE
        val context = rela.context

        val teamAList = ArrayList<Int>()
        val teamBList = ArrayList<Int>()
        val totalRounds = cardList.size / totalPlayers
        var counter = 0

        val delay = 200L
        val duration = 500L
        var lastRound = 0
        var lastIndex = 0
        main@
        for (roundIndex in 0 until totalRounds+1) {
            for (playerIndex in 0 until totalPlayers) {
                if (!teamAList.contains(playerIndex)) {
                    if (teamAList.size + teamBList.size == totalPlayers)
                        break@main
                    val card = cardList[counter]
                    val isJackCard = card.rank == 11
                    val pos =
                        PositionHelper.INSTANCE.getPlayerCardPosition(playerIndex + 1, totalPlayers)
                    val cardView = CardView(context, card, centerPosX, centerPosY)
                    rela.addView(cardView)
                    cardView.visibility = View.GONE

                    if (isJackCard) {
                        teamAList.add(playerIndex)
                        if (teamAList.size == totalPlayers / 2) {
                            for (index in 0 until totalPlayers) {
                                if (!teamAList.contains(index))
                                    teamBList.add(index)
                            }
                            lastRound = roundIndex
                            lastIndex = playerIndex
                        }
                    }
                    cardView.animate().translationX(pos[0]).translationY(pos[1])
                        .setDuration(duration).setStartDelay(delay * counter)
                        .withStartAction { cardView.visibility = View.VISIBLE }
                        .withEndAction {
                            if (!isJackCard)
                                rela.removeView(cardView)
                            if (lastRound == roundIndex && lastIndex == playerIndex) {
                                onAnimationEnd(teamAList, teamBList)
                            }
                        }
                    counter++
                }
            }
        }
    }

}