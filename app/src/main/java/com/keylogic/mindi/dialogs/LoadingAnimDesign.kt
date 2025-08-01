package com.keylogic.mindi.dialogs

import android.view.View
import android.widget.RelativeLayout
import com.keylogic.mindi.enums.SuitType
import com.keylogic.mindi.gamePlay.helper.DisplayHelper
import com.keylogic.mindi.gamePlay.models.Card
import com.keylogic.mindi.gamePlay.models.CardView

class LoadingAnimDesign {
    companion object {
        val INSTANCE = LoadingAnimDesign()
    }

    fun gameLoadingAnimations(mainRela: RelativeLayout) {
        mainRela.removeAllViews()

        val cWidth = DisplayHelper.cardWidth
        val cHeight = DisplayHelper.cardHeight

        val layoutW = cWidth * 3
        val layoutH = cHeight * 2
        val rela = RelativeLayout(mainRela.context)
        rela.layoutParams = RelativeLayout.LayoutParams(layoutW, layoutH)
        mainRela.addView(rela)

        val listOfCardViews = ArrayList<CardView>()
        var index = 0

        // Create 2x2 grid of card views
        for (i in 0..1) {
            for (j in 0..1) {
                val xPos = cWidth / 3f + (cWidth + cWidth / 3) * j.toFloat()
                val yPos = cHeight * i.toFloat()
                val card = getCard(index)
                val cardView = CardView(mainRela.context, card, xPos, yPos)
                cardView.isDefaultCard = true
                cardView.x = xPos
                cardView.y = yPos
                rela.addView(cardView)
                listOfCardViews.add(cardView)
                index++
            }
        }

        fun generateCardView(index: Int): CardView {
            val original = listOfCardViews[index]
            val newCard = CardView(original.context, original.card, original.xPos, original.yPos)
            newCard.x = original.xPos
            newCard.y = original.yPos
            newCard.layoutParams = RelativeLayout.LayoutParams(cWidth, cHeight)
            return newCard
        }

        val transitions = listOf(
            0 to 1,
            1 to 3,
            3 to 2,
            2 to 0,
            0 to 2,
            2 to 3,
            3 to 1,
            1 to 0
        )

        var transitionIndex = 0

        fun startNextAnimation() {
            val (start, end) = transitions[transitionIndex]

            // Hide the static card at start position
            if (transitionIndex >= transitions.size/2)
                listOfCardViews[start].visibility = View.VISIBLE
            else
                listOfCardViews[start].visibility = View.INVISIBLE

            val newCardView = generateCardView(start)
            rela.addView(newCardView)

            newCardView.animate()
                .translationX(listOfCardViews[end].xPos)
                .translationY(listOfCardViews[end].yPos)
                .setDuration(750)
                .withEndAction {
                    rela.removeView(newCardView)
                    // Prepare for next transition
                    transitionIndex = (transitionIndex + 1) % transitions.size
                    startNextAnimation()
                }
                .start()
        }

        // Kick off the animation loop
        startNextAnimation()
    }


    private fun getCard(index: Int, rank: Int = 10): Card {
        return Card(SuitType.entries[index], rank,0)
    }

}