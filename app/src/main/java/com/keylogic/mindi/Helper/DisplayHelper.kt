package com.keylogic.mindi.Helper

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager

class DisplayHelper {
    companion object {
        val INSTANCE = DisplayHelper()
        var screenWidth = 0
        var screenHeight = 0

        var cardWidth = 0
        var cardHeight = 0

        var profileWidth = 0
        var profileHeight = 0

        var totalProfileDivision = 5f
    }

    fun calculateScreenWH(context: Context) {
        val windowManager = context.getSystemService(WindowManager::class.java)
        val width: Int
        val height: Int
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val bounds = windowManager.currentWindowMetrics.bounds
            width = bounds.width()
            height = bounds.height()
        } else {
            val displayMetrics: DisplayMetrics = context.resources.displayMetrics
            width = displayMetrics.widthPixels
            height = displayMetrics.heightPixels
        }

        screenWidth = width
        screenHeight = height

        calculateProfileWH()
        calculateCardWH()
    }

    private fun calculateCardWH() {
        cardWidth = screenWidth / 18
        cardHeight = cardWidth * 306 / 221
    }

    private fun calculateProfileWH() {
        profileHeight = (screenHeight / totalProfileDivision).toInt()
        profileWidth = profileHeight
    }

}