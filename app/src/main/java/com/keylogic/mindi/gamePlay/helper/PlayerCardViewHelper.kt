package com.keylogic.mindi.gamePlay.helper

import com.keylogic.mindi.gamePlay.models.PlayerProfileView
import com.keylogic.mindi.databinding.GameLayoutBinding
import com.keylogic.mindi.enums.SuitType
import com.keylogic.mindi.gamePlay.helper.DisplayHelper.Companion.cardWidth
import com.keylogic.mindi.gamePlay.helper.DisplayHelper.Companion.screenWidth
import com.keylogic.mindi.gamePlay.models.Card
import com.keylogic.mindi.gamePlay.models.CardView

class PlayerCardViewHelper(private val layout: GameLayoutBinding) {
    val playerCardViewList = ArrayList<CardView>()

    fun designPlayerCardView() {
        playerCardViewList.forEach { layout.playAreaRelative.removeView(it) }
        playerCardViewList.clear()

        val player = GameHelper.getCurrentPlayer()
        val playAreaRela = layout.playAreaRelative

        val yPos = PositionHelper.INSTANCE.getUserCardYPosition()
        val totalWidth = (player.playerGameDetails.cardList.size - 1) * PositionHelper.INSTANCE.getUserCardXDistance() + cardWidth
        val startX = (screenWidth - totalWidth) / 2

        playerCardViewList.clear()

        for ((index, card) in player.playerGameDetails.cardList.withIndex()) {
            val newX = startX + (PositionHelper.INSTANCE.getUserCardXDistance() * index)
            val cardView = CardView(playAreaRela.context, card, newX, yPos)
            cardView.tag = card.getTag()
            playAreaRela.addView(cardView)
            playerCardViewList.add(cardView)
        }
    }

    fun setCurrRoundCards() {
        playerCardViewList.forEach {
            it.isDefaultCard = it.card.suit == GameHelper.currRoundSuitType
        }
    }

    fun setCardViewClickEvents(playerProfileView: PlayerProfileView, onCardClick: (Card) -> Unit) {
        for (cardView in playerCardViewList) {
            cardView.setOnClickListener {
                val isCurrPlayerTurn = GameHelper.getCurrentPlayer().uId == GameHelper.currTurnPlayerUID
                if (isCurrPlayerTurn) {
                    if (playerProfileView.isTimerRunning &&
                            (GameHelper.currRoundSuitType == SuitType.NONE ||
                                    GameHelper.currRoundSuitType == cardView.card.suit ||
                                    !GameHelper.getCurrentPlayer().playerGameDetails.isSuitExist())
                    ) {
                        if (cardView.isCardSelected)
                            onCardClick(cardView.card)
                        else {
                            playerCardViewList.forEach { it.setSelectedCard(false) }
                            cardView.setSelectedCard(true, withColor = true)
                        }
                    }
                }
            }
        }
    }

    fun updateCardViews() {

    }

    fun deselectAllCards() {
        playerCardViewList.forEach { it.setSelectedCard(false) }
    }

}