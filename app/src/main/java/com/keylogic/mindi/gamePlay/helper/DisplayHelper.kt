package com.keylogic.mindi.gamePlay.helper

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import com.keylogic.mindi.enums.DeviceType
import com.keylogic.mindi.helper.CommonHelper

class DisplayHelper {
    companion object {
        val INSTANCE = DisplayHelper()
//        var cardImgW = 221
//        var cardImgH = 306
        var screenWidth = 0
        var screenHeight = 0

        var cardWidth = 0
        var chipWH = 0
        var cardHeight = 0

        var profileWidth = 0
        var profileHeight = 0

        var totalProfileDivision = 0f
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
        calculateCardWH(context)
    }

    private fun calculateCardWH(context: Context) {
        cardWidth = screenWidth / 16
        cardHeight = cardWidth * 306 / 221
        chipWH = cardWidth / 3
    }

    private fun calculateProfileWH() {
        totalProfileDivision = if (CommonHelper.Companion.deviceType == DeviceType.NORMAL) 11f else 9.5f
        profileWidth = (screenWidth / totalProfileDivision).toInt()
        profileHeight = profileWidth
    }

}