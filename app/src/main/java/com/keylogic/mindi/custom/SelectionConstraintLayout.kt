package com.keylogic.mindi.custom

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.keylogic.mindi.R
import androidx.core.content.withStyledAttributes

class SelectionConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var isRewardCollected = false
    private var isRewardNotCollected = false
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

    private var cornerRadiusTopLeft = 0f.dpToPx(context)
    private var cornerRadiusTopRight = 0f.dpToPx(context)
    private var cornerRadiusBottomRight = 0f.dpToPx(context)
    private var cornerRadiusBottomLeft = 0f.dpToPx(context)

    init {
        context.withStyledAttributes(attrs, R.styleable.SelectionConstraintLayout) {

            defaultStrokeWidth = getDimension(
                R.styleable.SelectionConstraintLayout_containerBorderWidth,
                defaultStrokeWidth
            )
            customCornerRadius = getDimension(
                R.styleable.SelectionConstraintLayout_containerRadius,
                customCornerRadius
            )
            isViewSelected = getBoolean(R.styleable.SelectionConstraintLayout_isSelected, false)
            isRewardCollected = getBoolean(
                R.styleable.SelectionConstraintLayout_isRewardCollected,
                isRewardCollected
            )
            isRewardNotCollected = getBoolean(
                R.styleable.SelectionConstraintLayout_isRewardNotCollected,
                isRewardNotCollected
            )
            isStrokeEnabled =
                getBoolean(R.styleable.SelectionConstraintLayout_isBorderEnabled, isStrokeEnabled)
            isElevationEnabled = getBoolean(
                R.styleable.SelectionConstraintLayout_isElevationEnabled,
                isElevationEnabled
            )
            isCommonGradient =
                getBoolean(R.styleable.SelectionConstraintLayout_isCommonGradient, isCommonGradient)
            isSingleDarkColor = getBoolean(
                R.styleable.SelectionConstraintLayout_isSingleDarkColor,
                isSingleDarkColor
            )
            isSingleLightColor = getBoolean(
                R.styleable.SelectionConstraintLayout_isSingleLightColor,
                isSingleLightColor
            )
            isSelectedColorIsBlueGradient = getBoolean(
                R.styleable.SelectionConstraintLayout_isSelectedColorIsBlueGradient,
                false
            )

            // NEW: Read specific corner radii
            cornerRadiusTopLeft = getDimension(
                R.styleable.SelectionConstraintLayout_cornerRadiusTopLeft,
                cornerRadiusTopLeft
            )
            cornerRadiusTopRight = getDimension(
                R.styleable.SelectionConstraintLayout_cornerRadiusTopRight,
                cornerRadiusTopRight
            )
            cornerRadiusBottomRight = getDimension(
                R.styleable.SelectionConstraintLayout_cornerRadiusBottomRight,
                cornerRadiusBottomRight
            )
            cornerRadiusBottomLeft = getDimension(
                R.styleable.SelectionConstraintLayout_cornerRadiusBottomLeft,
                cornerRadiusBottomLeft
            )


            if (hasValue(R.styleable.SelectionConstraintLayout_containerHeight)) {
                customHeight =
                    getDimension(R.styleable.SelectionConstraintLayout_containerHeight, 0f)
            }
        }

        background = buildBackgroundDrawable()
    }

    private fun buildBackgroundDrawable(): GradientDrawable {
        elevation = if (isElevationEnabled) elevationValue else 0f
        // Dynamic radius: 10px for 53px height
        var fgCornerRadius = customHeight * (10f / 53f)
        if (customCornerRadius != 0f)
            fgCornerRadius = customCornerRadius
        val givenCornerRadius = fgCornerRadius + 4f

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

        val colors = if (isRewardCollected) {
            intArrayOf(
                ContextCompat.getColor(context, R.color.rc_bottom_start),
                ContextCompat.getColor(context, R.color.rc_bottom_end),
            )
        }
        else if (isRewardNotCollected) {
            intArrayOf(
                ContextCompat.getColor(context, R.color.rnc_bottom_start),
                ContextCompat.getColor(context, R.color.rnc_bottom_end),
            )
        }
        else if (isCommonGradient) {
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
            if (cornerRadiusTopLeft > 0f || cornerRadiusTopRight > 0f ||
                cornerRadiusBottomRight > 0f || cornerRadiusBottomLeft > 0f) {
                cornerRadii = floatArrayOf(
                    cornerRadiusTopLeft, cornerRadiusTopLeft,
                    cornerRadiusTopRight, cornerRadiusTopRight,
                    cornerRadiusBottomRight, cornerRadiusBottomRight,
                    cornerRadiusBottomLeft, cornerRadiusBottomLeft
                )
            } else {
                this.cornerRadius = givenCornerRadius
            }

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

//    fun setViewHeight(height: Int) {
//        customHeight = height.toFloat()
//        background = buildBackgroundDrawable()
//    }

    fun updateSelection(newSelection: Boolean) {
        isViewSelected = newSelection
        background = buildBackgroundDrawable()
    }

    fun updateRewardCollection(isCurrDay: Boolean = false, isNotCurrDay: Boolean = false) {
        isRewardCollected = isCurrDay
        isRewardNotCollected = isNotCurrDay
        background = buildBackgroundDrawable()
    }

    private fun Float.dpToPx(context: Context): Float =
        this * context.resources.displayMetrics.density
}
