package com.keylogic.mindi.gamePlay.animation

import android.view.View
import android.widget.RelativeLayout
import com.keylogic.mindi.gamePlay.helper.CenterCardHelper
import com.keylogic.mindi.gamePlay.helper.GameHelper
import com.keylogic.mindi.gamePlay.helper.PlayerCardViewHelper
import com.keylogic.mindi.gamePlay.helper.PositionHelper
import com.keylogic.mindi.gamePlay.models.Card
import com.keylogic.mindi.gamePlay.models.CardView
import com.keylogic.mindi.gamePlay.models.PlayerDetails
import com.keylogic.mindi.helper.CommonHelper

class EnterPlayerCard {
    companion object {
        val INSTANCE = EnterPlayerCard()
    }

    fun enterPlayerCard(card: Card, centerCardHelper: CenterCardHelper, playerCardViewHelper: PlayerCardViewHelper,
                  onAnimationComplete: ()-> Unit) {
        val cardView = playerCardViewHelper.playerCardViewList.find { it.card.getTag() == card.getTag() }!!
        val centerIndex = GameHelper.getCurrentPlayer().centerCardIndex
        val centerCardView = centerCardHelper.getCenterCardView(centerIndex)
//        CommonHelper.print("Player click = $centerIndex > ${card.getCard()}")

        cardView.animate()
            .translationX(centerCardView.xPos)
            .translationY(centerCardView.yPos)
            .setDuration(GameHelper.enterCardSpeed)
            .rotation(centerCardView.rotation)
            .setStartDelay(0)
            .withStartAction {
                cardView.setSelectedCard(false)
            }
            .withEndAction {
                onAnimationComplete()
            }
            .start()
    }

    fun enterBotOrPlayerCard(playerIndex: Int, card: Card, rela: RelativeLayout,
                             playerCardViewHelper: PlayerCardViewHelper, centerCardHelper: CenterCardHelper,
                             totalPlayers: Int, onAnimationComplete: ()-> Unit) {
        rela.visibility = View.VISIBLE
        val playerDetails = GameHelper.getCurrentTurnPlayer()
        val cardView: CardView

        if (playerDetails.isBot) {
            val cardPos = PositionHelper.INSTANCE.getPlayerEntetedCardPosition(playerIndex+1,
                totalPlayers)
            cardView = CardView(rela.context, card, cardPos[0], cardPos[1])
            rela.addView(cardView)
        }
        else {
            playerCardViewHelper.deselectAllCards()
            cardView = playerCardViewHelper.playerCardViewList.find { it.card.getTag() == card.getTag() }!!
        }

        val centerCardView = centerCardHelper.getCenterCardView(playerDetails.centerCardIndex)
        cardView.alpha = 0f
        cardView.animate()
            .alpha(1f)
            .translationX(centerCardView.xPos)
            .translationY(centerCardView.yPos)
            .rotation(centerCardView.rotation)
            .setDuration(GameHelper.enterCardSpeed)
            .setStartDelay(0)
            .withStartAction {
                cardView.setSelectedCard(false)
            }
            .withEndAction {
                if (playerDetails.isBot)
                    rela.removeView(cardView)
                onAnimationComplete()
            }
            .start()
    }

    fun roundComplete(playerDetails: PlayerDetails, totalPlayers: Int, rela: RelativeLayout, centerCardHelper: CenterCardHelper,
                      onAnimationStart: () -> Unit,
                      onAnimationEnd: () -> Unit) {
        rela.visibility = View.VISIBLE
        val playerPos = PositionHelper.INSTANCE.getPlayerEntetedCardPosition(playerDetails.centerCardIndex+1, totalPlayers)
        for (i in 0 until totalPlayers) {
            val centerCardView = centerCardHelper.getCenterCardView(i)
            val card = centerCardView.card
            if (i == totalPlayers-1)
                onAnimationStart()
            val cardView = CardView(rela.context, card, centerCardView.xPos, centerCardView.yPos)
            cardView.rotation = centerCardView.rotation
            rela.addView(cardView)

            cardView.animate().translationX(playerPos[0]).translationY(playerPos[1])
                .alpha(0f)
                .setDuration(500)
                .setStartDelay(500)
                .withEndAction {
                    if (i == totalPlayers-1) {
                        rela.removeAllViews()
                        rela.visibility = View.GONE
                        onAnimationEnd()
                    }
                }
                .start()
        }
    }

}