package com.keylogic.mindi.custom

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.keylogic.mindi.R

class GradientBgConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        background = buildLayeredDrawable()
    }

    private fun buildLayeredDrawable(): LayerDrawable {
        val bgDrawable = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(
                ContextCompat.getColor(context, R.color.game_menu_start),
                ContextCompat.getColor(context, R.color.game_menu_center),
                ContextCompat.getColor(context, R.color.game_menu_end)
            )
        )

        return LayerDrawable(arrayOf(bgDrawable))
    }

}