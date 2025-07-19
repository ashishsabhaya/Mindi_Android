package com.keylogic.mindi.Custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.keylogic.mindi.R

class SeparationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        background = buildBackgroundDrawable()
    }

    private fun buildBackgroundDrawable(): GradientDrawable {
        val colors = intArrayOf(
            ContextCompat.getColor(context, R.color.transparent),
            ContextCompat.getColor(context, R.color.white),
            ContextCompat.getColor(context, R.color.transparent)
        )

        return GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors).apply {
        }
    }

}
