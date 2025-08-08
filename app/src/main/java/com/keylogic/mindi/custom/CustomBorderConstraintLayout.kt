package com.keylogic.mindi.custom

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.keylogic.mindi.R

class CustomBorderConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var strokeColor: Int = ContextCompat.getColor(context, android.R.color.black)
    private var strokeWidth: Float = 1f.dpToPx(context)
    private var fillColor: Int = ContextCompat.getColor(context, android.R.color.transparent)

    // Independent corner radii
    private var cornerRadiusTopLeft: Float = 0f.dpToPx(context)
    private var cornerRadiusTopRight: Float = 0f.dpToPx(context)
    private var cornerRadiusBottomRight: Float = 0f.dpToPx(context)
    private var cornerRadiusBottomLeft: Float = 0f.dpToPx(context)

    private val drawable = GradientDrawable()

    init {
        context.withStyledAttributes(attrs, R.styleable.CustomBorderConstraintLayout) {
            strokeColor = getColor(
                R.styleable.CustomBorderConstraintLayout_bgStrokeColor,
                ContextCompat.getColor(context, android.R.color.black)
            )
            strokeWidth = getDimension(
                R.styleable.CustomBorderConstraintLayout_bgStrokeWidth,
                1f.dpToPx(context)
            )
            fillColor = getColor(
                R.styleable.CustomBorderConstraintLayout_bgFillColor,
                ContextCompat.getColor(context, android.R.color.transparent)
            )

            // Per-corner radius from XML
            cornerRadiusTopLeft = getDimension(
                R.styleable.CustomBorderConstraintLayout_cbCornerRadiusTopLeft,
                0f.dpToPx(context)
            )
            cornerRadiusTopRight = getDimension(
                R.styleable.CustomBorderConstraintLayout_cbCornerRadiusTopRight,
                0f.dpToPx(context)
            )
            cornerRadiusBottomRight = getDimension(
                R.styleable.CustomBorderConstraintLayout_cbCornerRadiusBottomRight,
                0f.dpToPx(context)
            )
            cornerRadiusBottomLeft = getDimension(
                R.styleable.CustomBorderConstraintLayout_cbCornerRadiusBottomLeft,
                0f.dpToPx(context)
            )
        }

        updateBorder()
        background = drawable
    }

    private fun updateDrawable() {
        drawable.shape = GradientDrawable.RECTANGLE
        drawable.setColor(fillColor)
        drawable.setStroke(strokeWidth.toInt(), strokeColor)

        // Apply per-corner radii
        drawable.cornerRadii = floatArrayOf(
            cornerRadiusTopLeft, cornerRadiusTopLeft,
            cornerRadiusTopRight, cornerRadiusTopRight,
            cornerRadiusBottomRight, cornerRadiusBottomRight,
            cornerRadiusBottomLeft, cornerRadiusBottomLeft
        )
    }

    fun updateBorder(
        strokeColor: Int = this.strokeColor,
        strokeWidth: Float = this.strokeWidth,
        fillColor: Int = this.fillColor,
        topLeftRadius: Float = this.cornerRadiusTopLeft,
        topRightRadius: Float = this.cornerRadiusTopRight,
        bottomRightRadius: Float = this.cornerRadiusBottomRight,
        bottomLeftRadius: Float = this.cornerRadiusBottomLeft
    ) {
        this.strokeColor = strokeColor
        this.strokeWidth = strokeWidth
        this.fillColor = fillColor
        this.cornerRadiusTopLeft = topLeftRadius
        this.cornerRadiusTopRight = topRightRadius
        this.cornerRadiusBottomRight = bottomRightRadius
        this.cornerRadiusBottomLeft = bottomLeftRadius
        updateDrawable()
    }

    private fun Float.dpToPx(context: Context): Float =
        this * context.resources.displayMetrics.density
}
