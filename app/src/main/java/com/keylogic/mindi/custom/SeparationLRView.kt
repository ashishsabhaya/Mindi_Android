package com.keylogic.mindi.custom

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.keylogic.mindi.R

class SeparationLRView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        background = buildBackgroundDrawable()
    }

    private fun buildBackgroundDrawable(): GradientDrawable {
        val colors = intArrayOf(
            ContextCompat.getColor(context, R.color.separator_lr_start),
            ContextCompat.getColor(context, R.color.separator_lr_end)
        )

        return GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors).apply {
        }
    }

}
