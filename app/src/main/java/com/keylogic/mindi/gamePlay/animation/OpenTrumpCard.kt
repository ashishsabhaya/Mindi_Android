package com.keylogic.mindi.gamePlay.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.LinearInterpolator
import com.keylogic.mindi.databinding.GameLayoutBinding
import com.keylogic.mindi.gamePlay.helper.DisplayHelper
import com.keylogic.mindi.gamePlay.helper.GameHelper
import com.keylogic.mindi.gamePlay.models.CardView

class OpenTrumpCard {
    companion object {
        val INSTANCE = OpenTrumpCard()
    }

    fun showTrumpCard(
        gameLayouts: GameLayoutBinding,
        trumpCardView: CardView,
        onAnimationComplete: () -> Unit
    ) {
        val speed = 500L
        val rela = gameLayouts.animationRelative
        rela.visibility = View.VISIBLE
        val trumpCard = GameHelper.trumpCardSuit.card

        trumpCardView.visibility = View.INVISIBLE

        val cardView = CardView(rela.context, trumpCard, trumpCardView.xPos, trumpCardView.yPos)
        cardView.isDefaultCard = true
        cardView.setCardBack()
        rela.addView(cardView)

        fun returnToPosition() {
            cardView.animate().translationX(trumpCardView.xPos).translationY(trumpCardView.yPos)
                .setStartDelay(speed/2)
                .setDuration(speed)
                .withEndAction {
                    trumpCardView.visibility = View.VISIBLE
                    rela.removeAllViews()
                    rela.visibility = View.GONE
                    onAnimationComplete()
                }
                .start()
        }


        fun openCardView() {
            val animator = ObjectAnimator.ofFloat(cardView, "rotationY", 180f, 0f)
            animator.duration = speed
            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
                    super.onAnimationEnd(animation, isReverse)
                    returnToPosition()
                }
            })
            animator.start()
            Handler(Looper.getMainLooper()).postDelayed({
                cardView.updateResource(trumpCard)
            }, speed / 2)
        }


        val centerX = (DisplayHelper.screenWidth - DisplayHelper.cardWidth) / 2f
        val centerY = (DisplayHelper.screenHeight - DisplayHelper.cardHeight) / 2f
        cardView.animate().translationX(centerX).translationY(centerY)
            .setDuration(speed)
            .withEndAction {
                openCardView()
            }
            .start()

    }

    fun startScaleAnimation(view: View) {
        val speed = 1_000L
        val start = 1f
        val end = 1.05f
        val scaleUpX = ObjectAnimator.ofFloat(view, View.SCALE_X, start, end)
        val scaleUpY = ObjectAnimator.ofFloat(view, View.SCALE_Y, start, end)

        scaleUpX.repeatMode = ValueAnimator.REVERSE
        scaleUpY.repeatMode = ValueAnimator.REVERSE

        scaleUpX.repeatCount = ValueAnimator.INFINITE
        scaleUpY.repeatCount = ValueAnimator.INFINITE

        scaleUpX.duration = speed
        scaleUpY.duration = speed

        scaleUpX.interpolator = LinearInterpolator()
        scaleUpY.interpolator = LinearInterpolator()

        scaleUpX.start()
        scaleUpY.start()
    }

}