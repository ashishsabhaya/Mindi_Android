package com.keylogic.mindi.Custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.keylogic.mindi.R

class TitleConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var isAllSideStoke = false
    private var isStrokeEnabled = true
    private var customCornerRadius = 0f.dpToPx(context)
    private var customHeight = 0f.dpToPx(context)
    private var defaultStrokeWidth = 0f.dpToPx(context)
    private var elevationValue = 8f.dpToPx(context) // Fixed elevation = 8dp
    private var strokeWidth = 0f

    private val strokePaint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.TitleConstraintLayout)

        defaultStrokeWidth = a.getDimension(R.styleable.TitleConstraintLayout_titleStrokeWidth, defaultStrokeWidth)
        customCornerRadius = a.getDimension(R.styleable.TitleConstraintLayout_titleRadius, 0f)
        isStrokeEnabled = a.getBoolean(R.styleable.TitleConstraintLayout_isTitleStrokeEnabled, true)
        isAllSideStoke = a.getBoolean(R.styleable.TitleConstraintLayout_isAllSideStoke, false)

        if (a.hasValue(R.styleable.TitleConstraintLayout_titleHeight)) {
            customHeight = a.getDimension(R.styleable.TitleConstraintLayout_titleHeight, 0f)
        }
        a.recycle()

        // Calculate dynamic radius and stroke
        var fgCornerRadius = customHeight * (5f / 53f)
        if (customCornerRadius != 0f)
            fgCornerRadius = customCornerRadius
        val bgCornerRadius = fgCornerRadius + 2f

        strokeWidth = customHeight * (1f / 53f)
        if (defaultStrokeWidth > 0f)
            strokeWidth = defaultStrokeWidth
        if (!isStrokeEnabled)
            strokeWidth = 0f

        background = buildBackgroundDrawable(bgCornerRadius)

        // Prepare paint
        strokePaint.strokeWidth = strokeWidth
        setWillNotDraw(false)

        // Set fixed elevation
        elevation = elevationValue
    }

    private fun buildBackgroundDrawable(cornerRadius: Float): GradientDrawable {
        val colors = intArrayOf(
            ContextCompat.getColor(context, R.color.title_start),
            ContextCompat.getColor(context, R.color.title_center2),
            ContextCompat.getColor(context, R.color.title_center1),
            ContextCompat.getColor(context, R.color.title_center),
            ContextCompat.getColor(context, R.color.title_center11),
            ContextCompat.getColor(context, R.color.title_center12),
            ContextCompat.getColor(context, R.color.title_end)
        )

        return GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors).apply {
            this.cornerRadius = cornerRadius
            // No stroke here — we'll draw it manually in onDraw()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (!isStrokeEnabled || strokeWidth <= 0f) return

        val halfStroke = strokeWidth / 2f

        // Create gradient shader for the stroke
        val shader = LinearGradient(
            0f, 0f, width.toFloat(), 0f,
            intArrayOf(
                ContextCompat.getColor(context, R.color.title_start),
                ContextCompat.getColor(context, R.color.title_stroke),
                ContextCompat.getColor(context, R.color.title_end)
            ),
            null,
            Shader.TileMode.CLAMP
        )
        strokePaint.shader = shader

        if (isAllSideStoke) {
            // Full border (rounded)
            val rectF = RectF(
                halfStroke,
                halfStroke,
                width.toFloat() - halfStroke,
                height.toFloat() - halfStroke
            )
            canvas.drawRoundRect(rectF, customCornerRadius, customCornerRadius, strokePaint)
        } else {
            // Only top and bottom lines
            canvas.drawLine(0f, halfStroke, width.toFloat(), halfStroke, strokePaint) // Top
            canvas.drawLine(0f, height - halfStroke, width.toFloat(), height - halfStroke, strokePaint) // Bottom
        }
    }

    private fun Float.dpToPx(context: Context): Float =
        this * context.resources.displayMetrics.density
}
