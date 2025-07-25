package com.keylogic.mindi.Custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.keylogic.mindi.R
import androidx.core.content.withStyledAttributes

class HandsIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var customCornerRadius = 0f.dpToPx(context)
    private var customHeight = 0f.dpToPx(context)

    private var triangleHeight = 0f
    private var isCustomColor = false
    private var trianglePath = Path()
    private val trianglePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.bet_price_start)
    }

    private var isTopSide = true // Default value, you can also expose it via setter

    init {
        context.withStyledAttributes(attrs, R.styleable.HandsIndicator) {
            customCornerRadius = getDimension(R.styleable.HandsIndicator_hBetViewContainerRadius, 0f)
            if (hasValue(R.styleable.HandsIndicator_hBetViewHeight)) {
                customHeight = getDimension(R.styleable.HandsIndicator_hBetViewHeight, 0f)
            }
            isCustomColor = getBoolean(R.styleable.HandsIndicator_isCustomColor, isCustomColor)
            isTopSide = getBoolean(R.styleable.HandsIndicator_isTopSide, true)
        }

        setUpView()
    }

    fun getTriangleHeight(): Float {
        return triangleHeight
    }

    private fun setUpView() {
        triangleHeight = customHeight * 0.2f

        val fgCornerRadius = if (customCornerRadius != 0f) customCornerRadius
        else customHeight * (10f / 53f)

        elevation = 0f

        background = buildLayeredDrawable(fgCornerRadius)

        setWillNotDraw(false)

        // Set padding depending on triangle side
        if (isTopSide) {
            setPadding(paddingLeft, triangleHeight.toInt(), paddingRight, paddingBottom)
        } else {
            setPadding(paddingLeft, paddingTop, paddingRight, triangleHeight.toInt())
        }
    }

    private fun buildLayeredDrawable(fgRadius: Float): LayerDrawable {
        val bgDrawable = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            if (isCustomColor) {
                intArrayOf(
                    ContextCompat.getColor(context, R.color.white),
                    ContextCompat.getColor(context, R.color.white)
                )
            }
            else {
                if (isTopSide) {
                    intArrayOf(
                        ContextCompat.getColor(context, R.color.red_team),
                        ContextCompat.getColor(context, R.color.red_team)
                    )
                } else {
                    intArrayOf(
                        ContextCompat.getColor(context, R.color.green_team),
                        ContextCompat.getColor(context, R.color.green_team)
                    )
                }
            }
        ).apply {
            cornerRadius = fgRadius
        }

        val fgDrawable = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(
                ContextCompat.getColor(context, R.color.bet_price_start),
                ContextCompat.getColor(context, R.color.bet_price_start)
            )
        ).apply {
            cornerRadius = fgRadius
        }

        // Inset drawable to allow for triangle space
        return LayerDrawable(arrayOf(bgDrawable, fgDrawable)).apply {
            val margin = 2
            if (isTopSide) {
                setLayerInset(0, 0, triangleHeight.toInt(), 0, 0)
                setLayerInset(1, margin, triangleHeight.toInt() + margin, margin, margin)
            } else {
                setLayerInset(0, 0, 0, 0, triangleHeight.toInt())
                setLayerInset(1, margin, margin, margin, triangleHeight.toInt() + margin)
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val triangleBase = triangleHeight

        // Determine triangle X position
        val triangleCenterX = if (isTopSide) width * 0.75f else width * 0.25f

        trianglePaint.color = if (isCustomColor) {
            ContextCompat.getColor(context, R.color.white)
        }
        else {
            if (isTopSide) {
                ContextCompat.getColor(context, R.color.red_team)
            } else {
                ContextCompat.getColor(context, R.color.green_team)
            }
        }

        trianglePath.reset()
        if (isTopSide) {
            trianglePath.moveTo(triangleCenterX, 0f)
            trianglePath.lineTo(triangleCenterX - triangleBase / 2, triangleHeight)
            trianglePath.lineTo(triangleCenterX + triangleBase / 2, triangleHeight)
        } else {
            trianglePath.moveTo(triangleCenterX, height.toFloat())
            trianglePath.lineTo(triangleCenterX - triangleBase / 2, height.toFloat() - triangleHeight)
            trianglePath.lineTo(triangleCenterX + triangleBase / 2, height.toFloat() - triangleHeight)
        }
        trianglePath.close()

        canvas.drawPath(trianglePath, trianglePaint)
    }

    private fun Float.dpToPx(context: Context): Float =
        this * context.resources.displayMetrics.density
}
