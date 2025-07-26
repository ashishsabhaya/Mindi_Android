package com.keylogic.mindi.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.keylogic.mindi.R
import androidx.core.content.withStyledAttributes

class StoreConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var borderWidth = 5f.dpToPx(context)
    private var cornerRadius = 2f

    init {
        context.withStyledAttributes(attrs, R.styleable.StoreConstraintLayout) {
            borderWidth = getDimension(R.styleable.StoreConstraintLayout_borderWidth, borderWidth)
            cornerRadius = getDimension(R.styleable.StoreConstraintLayout_borderRadius, 0f)
        }

        val lPadding = borderWidth.toInt() + paddingLeft
        val tPadding = borderWidth.toInt() + paddingTop
        val rPadding = borderWidth.toInt() + paddingRight
        val bPadding = borderWidth.toInt() + paddingBottom
        setPadding(lPadding, tPadding, rPadding, bPadding)

        // Prevent background overlap
        setWillNotDraw(false)
        background = null
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderWidth

        val shader = LinearGradient(
            0f, 0f, 0f, height.toFloat(), // Top to bottom gradient
            context.getColor(R.color.border_start),
            context.getColor(R.color.border_end),
            Shader.TileMode.CLAMP
        )
        paint.shader = shader

        val halfStroke = borderWidth / 2f
        val rect = RectF(halfStroke, halfStroke, width - halfStroke, height - halfStroke)
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)
    }

    private fun Float.dpToPx(context: Context): Float =
        this * context.resources.displayMetrics.density
}
