package com.keylogic.mindi.gamePlay.animation

import android.view.View
import android.widget.RelativeLayout
import com.keylogic.mindi.gamePlay.helper.DisplayHelper
import com.keylogic.mindi.gamePlay.helper.PositionHelper
import com.keylogic.mindi.gamePlay.models.ChipView

class ChipAnimation() {
    companion object {
        val INSTANCE = ChipAnimation()
    }

    fun collectAllPlayersChips(animationLayout: RelativeLayout, totalPlayers: Int, chipViewCenterX: Float, chipViewCenterY: Float,
                               onAnimationComplete: () -> Unit) {
        animationLayout.visibility = View.VISIBLE
        val chipViewList = ArrayList<ChipView>()
        fun removeAllViews() {
            chipViewList.forEach { animationLayout.removeView(it) }
            chipViewList.clear()
        }
        removeAllViews()
        for (i in 1 .. totalPlayers) {
            val pos = PositionHelper.Companion.INSTANCE.getPlayerPosition(i, totalPlayers)
            val xPos = pos[0] + (DisplayHelper.Companion.profileWidth - DisplayHelper.Companion.chipWH) / 2f
            val yPos = pos[1] + (DisplayHelper.Companion.profileHeight - DisplayHelper.Companion.chipWH) / 2f
            val chipView = ChipView(animationLayout.context, xPos, yPos)
            animationLayout.addView(chipView)
            chipView.visibility = View.GONE
            chipViewList.add(chipView)
        }

        fun moveToChipPosition() {
            val duration = 500L
            val delay = 100L
            for ((index, chipView) in chipViewList.withIndex()) {
                chipView.animate().translationX(chipViewCenterX).translationY(chipViewCenterY)
                    .setDuration(duration)
                    .setStartDelay(delay * index)
                    .withEndAction {
                        if (index == chipViewList.size-1) {
                            removeAllViews()
                            animationLayout.visibility = View.GONE
                            onAnimationComplete()
                        }
                    }
                    .start()
            }
        }

        val duration = 750L
        for (i in 0 until totalPlayers) {
            val xPos = (DisplayHelper.Companion.screenWidth - DisplayHelper.Companion.chipWH) / 2f
            val yPos = (DisplayHelper.Companion.screenHeight - DisplayHelper.Companion.chipWH) / 2f
            val chipView = chipViewList[i]

            chipView.animate().translationX(xPos).translationY(yPos)
                .setDuration(duration)
                .withStartAction {
                    chipView.visibility = View.VISIBLE
                }
                .withEndAction {
                    if (i == totalPlayers-1)
                        moveToChipPosition()
                }
                .start()
        }
    }

    fun collectDailyRewardChips(animationLayout: RelativeLayout, startX: Float, startY: Float,
                                endX: Float, endY: Float,
                               onAnimationComplete: () -> Unit) {
        animationLayout.visibility = View.VISIBLE
        animationLayout.setOnClickListener(null)
        val chipViewList = ArrayList<ChipView>()
        fun removeAllViews() {
            chipViewList.forEach { animationLayout.removeView(it) }
            chipViewList.clear()
        }
        removeAllViews()
        for (i in 1 .. 8) {
            val chipView = ChipView(animationLayout.context, startX, startY)
            animationLayout.addView(chipView)
            chipView.visibility = View.GONE
            chipViewList.add(chipView)
        }

        val duration = 500L
        val delay = 100L
        for ((index, chipView) in chipViewList.withIndex()) {
            chipView.animate().translationX(endX).translationY(endY)
                .setDuration(duration)
                .setStartDelay(delay * index)
                .withStartAction {
                    chipView.visibility = View.VISIBLE
                }
                .withEndAction {
                    chipView.visibility = View.GONE
                    if (index == chipViewList.size-1) {
                        removeAllViews()
                        animationLayout.visibility = View.GONE
                        onAnimationComplete()
                    }
                }
                .start()
        }
    }

}