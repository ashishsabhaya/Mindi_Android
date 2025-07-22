package com.keylogic.mindi.Custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.keylogic.mindi.R

class SelectionConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var isViewSelected = false
    private var isSelectedColorIsBlueGradient = false
    private var isStrokeEnabled = false
    private var isCommonGradient = false
    private var isSingleDarkColor = false
    private var isSingleLightColor = false
    private var isElevationEnabled = true
    private var customCornerRadius = 0f.dpToPx(context)
    private var customHeight = 0f.dpToPx(context)
    private var defaultStrokeWidth = 0f.dpToPx(context)
    private var elevationValue = 8f.dpToPx(context) // Example: fixed elevation = 8dp

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.SelectionConstraintLayout)

        defaultStrokeWidth = a.getDimension(R.styleable.SelectionConstraintLayout_containerBorderWidth, defaultStrokeWidth)
        customCornerRadius = a.getDimension(R.styleable.SelectionConstraintLayout_containerRadius,0f)
        isViewSelected = a.getBoolean(R.styleable.SelectionConstraintLayout_isSelected,false)
        isStrokeEnabled = a.getBoolean(R.styleable.SelectionConstraintLayout_isBorderEnabled,false)
        isElevationEnabled = a.getBoolean(R.styleable.SelectionConstraintLayout_isElevationEnabled,true)
        isCommonGradient = a.getBoolean(R.styleable.SelectionConstraintLayout_isCommonGradient,false)
        isSingleDarkColor = a.getBoolean(R.styleable.SelectionConstraintLayout_isSingleDarkColor,false)
        isSingleLightColor = a.getBoolean(R.styleable.SelectionConstraintLayout_isSingleLightColor,false)
        isSelectedColorIsBlueGradient = a.getBoolean(R.styleable.SelectionConstraintLayout_isSelectedColorIsBlueGradient,false)

        if (a.hasValue(R.styleable.SelectionConstraintLayout_containerHeight)) {
            customHeight = a.getDimension(R.styleable.SelectionConstraintLayout_containerHeight, 0f)
        }
        a.recycle()

        background = buildBackgroundDrawable()
    }

    private fun buildBackgroundDrawable(): GradientDrawable {
        elevation = if (isElevationEnabled) elevationValue else 0f
        // Dynamic radius: 10px for 53px height
        var fgCornerRadius = customHeight * (10f / 53f)
        if (customCornerRadius != 0f)
            fgCornerRadius = customCornerRadius
        val cornerRadius = fgCornerRadius + 4f

        // Dynamic stroke: 1px for 53px height
        val strokeWidth = if (isStrokeEnabled) {
            if (!isViewSelected)
                0f
            else if (defaultStrokeWidth > 0f)
                defaultStrokeWidth
            else
                customHeight * (1f / 53f)
        }
        else
            0f

        val colors = if (isCommonGradient) {
            intArrayOf(
                ContextCompat.getColor(context, R.color.position_fg_start),
                ContextCompat.getColor(context, R.color.position_fg_end),
            )
        }
        else if (isSingleDarkColor) {
            intArrayOf(
                ContextCompat.getColor(context, R.color.selection_dark),
                ContextCompat.getColor(context, R.color.selection_dark)
            )
        }
        else if (isSingleLightColor) {
            intArrayOf(
                ContextCompat.getColor(context, R.color.selection_light),
                ContextCompat.getColor(context, R.color.selection_light)
            )
        }
        else {
            if (isViewSelected) {
                if (isSelectedColorIsBlueGradient) {
                    intArrayOf(
                        ContextCompat.getColor(context, R.color.blue_selected_start),
                        ContextCompat.getColor(context, R.color.blue_selected_end)
                    )
                }
                else {
                    intArrayOf(
                        ContextCompat.getColor(context, R.color.red_selected_start),
                        ContextCompat.getColor(context, R.color.red_selected_end)
                    )
                }
            } else {
                intArrayOf(
                    ContextCompat.getColor(context, R.color.un_selected_start),
                    ContextCompat.getColor(context, R.color.un_selected_end)
                )
            }
        }

        return GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors).apply {
            this.cornerRadius = cornerRadius
            if (strokeWidth > 0f) {
                setStroke(strokeWidth.toInt(), ContextCompat.getColor(context, R.color.white))
            }
        }
    }

    fun updateProgrammatically(height: Int, newSelection: Boolean, isCommonGradientColor: Boolean, isElevated: Boolean) {
        customHeight = height.toFloat()
        isViewSelected = newSelection
        isStrokeEnabled = newSelection
        isCommonGradient = isCommonGradientColor
        isElevationEnabled = isElevated
        background = buildBackgroundDrawable()
    }

    fun setViewHeight(height: Int) {
        customHeight = height.toFloat()
        background = buildBackgroundDrawable()
    }

    fun updateSelection(newSelection: Boolean) {
        isViewSelected = newSelection
        background = buildBackgroundDrawable()
    }

    private fun Float.dpToPx(context: Context): Float =
        this * context.resources.displayMetrics.density
}
