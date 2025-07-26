package com.keylogic.mindi.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.keylogic.mindi.R
import androidx.core.content.withStyledAttributes

class BetPriceIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var customCornerRadius = 0f.dpToPx(context)
    private var customHeight = 0f.dpToPx(context)

    // Triangle size ratio
    private var triangleHeight = 0f
    private var trianglePath = Path()
    private val trianglePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.bet_price_start) // Match foreground or use desired color
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.BetPriceIndicator) {
            customCornerRadius = getDimension(R.styleable.BetPriceIndicator_betViewContainerRadius, 0f)
            if (hasValue(R.styleable.BetPriceIndicator_betViewHeight)) {
                customHeight = getDimension(R.styleable.BetPriceIndicator_betViewHeight, 0f)
            }
        }

        triangleHeight = customHeight * 0.2f

        // Dynamic radius: 10px for 53px height
        var fgCornerRadius = customHeight * (10f / 53f)
        if (customCornerRadius != 0f)
            fgCornerRadius = customCornerRadius
        val bgCornerRadius = fgCornerRadius + 4f

        // Background bottom inset (approx. 8%)
        val bottomMargin = customHeight * 0.05f

        elevation = 0f

        // 👇 Background starts below triangle
        background = buildLayeredDrawable(bgCornerRadius, fgCornerRadius, bottomMargin)
        setWillNotDraw(false)

        setPadding(
            paddingLeft,
            triangleHeight.toInt(),
            paddingRight,
            bottomMargin.toInt()
        )
    }

    private fun buildLayeredDrawable(
        bgRadius: Float,
        fgRadius: Float,
        bottomMargin: Float
    ): LayerDrawable {
        val bgDrawable = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(
                ContextCompat.getColor(context, R.color.position_bg_start),
                ContextCompat.getColor(context, R.color.position_bg_center),
                ContextCompat.getColor(context, R.color.position_bg_end)
            )
        ).apply {
            cornerRadius = bgRadius
        }

        val fgDrawable = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(
                ContextCompat.getColor(context, R.color.bet_price_start),
                ContextCompat.getColor(context, R.color.bet_price_end)
            )
        ).apply {
            cornerRadius = fgRadius
        }

        return LayerDrawable(arrayOf(bgDrawable, fgDrawable)).apply {
            setLayerInset(
                0, 0, triangleHeight.toInt(), 0, 0)
            setLayerInset(
                1,
                0, triangleHeight.toInt(), 0,
                bottomMargin.toInt()
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val triangleBase = triangleHeight

        // Create triangle pointing down at the center top
        trianglePath.reset()
        trianglePath.moveTo(width / 2f, 0f) // Top middle
        trianglePath.lineTo((width / 2f) - triangleBase / 2, triangleHeight)
        trianglePath.lineTo((width / 2f) + triangleBase / 2, triangleHeight)
        trianglePath.close()

        canvas.drawPath(trianglePath, trianglePaint)
    }

    private fun Float.dpToPx(context: Context): Float =
        this * context.resources.displayMetrics.density
}
