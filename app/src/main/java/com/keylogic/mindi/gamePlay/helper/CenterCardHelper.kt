package com.keylogic.mindi.gamePlay.helper

import com.keylogic.mindi.databinding.GameLayoutBinding
import com.keylogic.mindi.enums.SuitType
import com.keylogic.mindi.gamePlay.models.Card
import com.keylogic.mindi.gamePlay.models.CardView
import kotlin.math.cos
import kotlin.math.sin

class CenterCardHelper(private val totalPlayers: Int, private val layout: GameLayoutBinding) {

    private val centerCardViewList = mutableListOf<CardView>()

    fun designCenterCards() {
        val playArea = layout.playAreaRelative

        val centerX = DisplayHelper.screenWidth / 2f
        val centerY = DisplayHelper.screenHeight / 2f
        val hRadius = DisplayHelper.cardHeight * 1f
        val vRadius = DisplayHelper.cardWidth * 1f
        val angleStep = 360f / totalPlayers
        val startAngle = -270f

        centerCardViewList.forEach { playArea.removeView(it) }
        centerCardViewList.clear()

        for (i in 0 until totalPlayers) {
            val card = Card()

            val angleDeg = startAngle + i * angleStep
            val angleRad = Math.toRadians(angleDeg.toDouble())

            val xPos = (centerX + hRadius * cos(angleRad)).toFloat() - (DisplayHelper.cardWidth / 2)
            val yPos = (centerY + vRadius * sin(angleRad)).toFloat() - (DisplayHelper.cardHeight / 2)

            val cardView = CardView(playArea.context, card, xPos, yPos)
            cardView.rotation = (angleDeg + 90f) % 360f
            playArea.addView(cardView)
            centerCardViewList.add(cardView)
        }
    }

    fun updateCenterCard(index: Int, card: Card) {
        if (card.suit == SuitType.NONE)
            centerCardViewList[index].z = 1f
        else
            centerCardViewList[index].z += 1f
        centerCardViewList[index].updateResource(card)
    }

    fun getCenterCardView(index: Int): CardView {
        return centerCardViewList[index]
    }

}
