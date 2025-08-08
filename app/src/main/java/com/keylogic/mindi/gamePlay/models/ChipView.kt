package com.keylogic.mindi.gamePlay.models

import android.content.Context
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import com.keylogic.mindi.R
import com.keylogic.mindi.gamePlay.helper.DisplayHelper

class ChipView @JvmOverloads constructor(
    context: Context,
    var xPos: Float = 0f,
    var yPos: Float = 0f
) : AppCompatImageView(context) {
    init {
        layoutParams = FrameLayout.LayoutParams(DisplayHelper.chipWH, DisplayHelper.chipWH)
        scaleType = ScaleType.FIT_XY
        updateView()
    }

    private fun updateView() {
        setImageResource(R.drawable.ic_chip)
        x = xPos
        y = yPos
    }
}
