package com.keylogic.mindi.gamePlay.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Handler
import android.os.Looper
import android.view.View
import com.keylogic.mindi.databinding.GameLayoutBinding
import com.keylogic.mindi.gamePlay.helper.DisplayHelper
import com.keylogic.mindi.gamePlay.helper.GameHelper
import com.keylogic.mindi.gamePlay.helper.PositionHelper
import com.keylogic.mindi.gamePlay.models.Card
import com.keylogic.mindi.gamePlay.models.CardView
import com.keylogic.mindi.gamePlay.models.PlayerDetails
import com.keylogic.mindi.helper.ProfileHelper
import kotlin.random.Random

class DistributeCards(private val layout: GameLayoutBinding) {

    fun distributeCards(onComplete: (Card) -> Unit) {
        fun hideAnimationRela() {
            layout.animationRelative.removeAllViews()
            layout.animationRelative.visibility = View.GONE
        }

        val playerDetailsList = GameHelper.playerDetailsList
        val totalPlayers = GameHelper.tableConfig.totalPlayers
        val currentPlayer = GameHelper.getCurrentPlayer()
        val cardViewList = ArrayList<CardView>()
        val rela = layout.animationRelative
        rela.visibility = View.VISIBLE
        val delayTime = 200L
        val animSpeed = 400L

        val centerX = (DisplayHelper.screenWidth - DisplayHelper.cardWidth) / 2f
        val centerY = (DisplayHelper.screenHeight - DisplayHelper.cardHeight) / 2f

        val angleStep = 360f / totalPlayers
        val startAngle = -270f

        var lastSelected: CardView? = null
        for (playerIndex in 1..totalPlayers) {
            val player = playerDetailsList[playerIndex-1]
            val isFirst = playerIndex == 1
            for (j in 0 until player.playerGameDetails.cardList.size) {
                val totalWidth = (player.playerGameDetails.cardList.size - 1) * (PositionHelper.INSTANCE.getUserCardXDistance()) + DisplayHelper.cardWidth
                val startX = (DisplayHelper.screenWidth - totalWidth) / 2
                val newX = startX + (PositionHelper.INSTANCE.getUserCardXDistance() * j)
                val newY = PositionHelper.INSTANCE.getUserCardYPosition()

                val eachAngle = startAngle + (playerIndex - 1) * angleStep
                val rotationAngle = (eachAngle + 90) % 360 - if (isFirst) 180 else 0

                val pos = PositionHelper.INSTANCE.getPlayerEntetedCardPosition(playerIndex, totalPlayers)
                val xPos = if (playerIndex == 1) newX else pos[0]
                val yPos = if (playerIndex == 1) newY else pos[1]

                val cardView = CardView(rela.context, Card(), xPos, yPos)
                rela.addView(cardView)
                cardView.x = centerX
                cardView.y = centerY
                cardView.setCardBack()
                if (playerIndex == 1)
                    cardViewList.add(cardView)

                cardView.setOnClickListener {
                    if (GameHelper.tableConfig.isHideMode && GameHelper.currTurnPlayerUID == ProfileHelper.profileUID) {
                        if (cardView == lastSelected) {
                            cardView.tag = "000"
                            hideCardAnimation(cardView, cardViewList, currentPlayer) {
                                val card = player.playerGameDetails.cardList[j]
                                hideAnimationRela()
                                onComplete(card)
                            }
                        }
                        else {
                            lastSelected?.setSelectedCard(false)
                            cardView.setSelectedCard(true, withColor = false)
                            lastSelected = cardView
                        }
                    }
                }

                cardView.visibility = View.GONE
                cardView.xPos = xPos
                cardView.yPos = yPos
                val alpha = if (isFirst) 1f else 0f
                cardView.animate().translationX(xPos).translationY(yPos).alpha(alpha).rotation(rotationAngle)
                    .setStartDelay(delayTime * j).setDuration(animSpeed)
                    .withStartAction { cardView.visibility = View.VISIBLE }
                    .withEndAction {
                        if (playerIndex != 1)
                            rela.removeView(cardView)
                        if (playerIndex == totalPlayers && j == player.playerGameDetails.cardList.size-1) {
                            if (GameHelper.tableConfig.isHideMode) {
                                if (GameHelper.currTurnPlayerUID.isNotEmpty() &&
                                    GameHelper.currTurnPlayerUID != ProfileHelper.profileUID) {
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        val index = GameHelper.getCurrentTurnPlayer().centerCardIndex
                                        val cardPos = PositionHelper.INSTANCE.getPlayerEntetedCardPosition(
                                            index+1,
                                            totalPlayers
                                        )
                                        val cardView =
                                            CardView(rela.context, Card(), cardPos[0], cardPos[1])
                                        cardView.setCardBack()
                                        rela.addView(cardView)
                                        hideCardAnimation(cardView, cardViewList, currentPlayer) {
                                            val botCardList =
                                                playerDetailsList[index].playerGameDetails.cardList
                                            val randomIndex = Random.nextInt(0, botCardList.size)
                                            hideAnimationRela()
                                            onComplete(botCardList[randomIndex])
                                        }
                                    }, 1_000L)
                                }
                            }
                            else
                                openCards(cardViewList, currentPlayer) {
                                    hideAnimationRela()
                                    onComplete(Card())
                                }
                        }
                    }.start()
            }
        }
    }

    fun hideCardAnimation(
        cardView: CardView,
        cardViewList: ArrayList<CardView>,
        user: PlayerDetails,
        onAnimationComplete: () -> Unit
    ) {
        val pos = PositionHelper.INSTANCE.getTrumpCardPosition(GameHelper.tableConfig.totalPlayers)
        cardView.animate()
            .translationX(pos[0])
            .translationY(pos[1])
            .setDuration(500)
            .setStartDelay(0)
            .withEndAction {
                openCards(cardViewList, user, onAnimationComplete)
            }
            .start()
    }

    private fun openCards(cardViewList: ArrayList<CardView>, user: PlayerDetails, onAnimationComplete: () -> Unit) {
        val speed = 300L
        cardViewList.shuffle()
        for (i in cardViewList.size-1 downTo 0) {
            val cardView = cardViewList[i]
            if (cardView.tag == "000")
                continue
            cardView.setCardBack()
            cardView.rotation = 0f
            val card = user.playerGameDetails.cardList[i]
            val animator = ObjectAnimator.ofFloat(cardView, "rotationY", 180f, 0f)
            animator.duration = speed
            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
                    super.onAnimationEnd(animation, isReverse)
                    cardView.updateResource(card)
                    if (i == 0) {
                        cardViewList.forEach { layout.animationRelative.removeView(it) }
                        cardViewList.clear()
                        onAnimationComplete()
                    }
                }
            })
            animator.start()

            Handler(Looper.getMainLooper()).postDelayed({
                cardView.updateResource(card)
            }, speed / 2)

        }
    }

}