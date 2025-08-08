package com.keylogic.mindi.gamePlay.animation

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.keylogic.mindi.gamePlay.helper.DisplayHelper
import com.keylogic.mindi.gamePlay.helper.PositionHelper
import com.keylogic.mindi.helper.CommonHelper

class MindiCollect {
    companion object {
        val mindiAnimSpeed = 400L
        val INSTANCE = MindiCollect()
    }

    //1st = total mindi
    //2nd = player profile view index
    fun showMindiAnimation(context: Context, totalPlayers: Int, view: ImageView, pair: Pair<Int, Int>,
                           onAnimationEnd: () -> Unit) {
        view.z = 1f
        view.visibility = View.VISIBLE
        val resourceName = "mindi_${pair.first}"
        val resource = CommonHelper.INSTANCE.getResourceByName(context, resourceName)
        view.setImageResource(resource)
        val viewWH = context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._100sdp)
        view.scaleX = 0f
        view.scaleY = 0f
        val pos = PositionHelper.INSTANCE.getPlayerPosition(pair.second+1, totalPlayers)
        view.x = pos[0] + (DisplayHelper.profileWidth - viewWH) / 2f
        view.y = pos[1] + (DisplayHelper.profileHeight - viewWH) / 2f

        val xPos = (DisplayHelper.screenWidth - viewWH) / 2f
        val yPos = (DisplayHelper.screenHeight - viewWH) / 2f

        view.animate()
            .translationX(xPos).translationY(yPos)
            .scaleX(1.5f).scaleY(1.5f)
            .setStartDelay(0).setDuration(mindiAnimSpeed).withEndAction {
                view.animate()
                    .scaleX(0f).scaleY(0f)
                    .setStartDelay(mindiAnimSpeed/2).setDuration(mindiAnimSpeed).withEndAction {
                        view.visibility = View.GONE
                        onAnimationEnd()
                    }
                    .start()
            }
            .start()
    }

}