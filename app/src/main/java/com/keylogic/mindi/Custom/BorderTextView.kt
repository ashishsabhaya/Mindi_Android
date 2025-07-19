package com.keylogic.mindi.Custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.keylogic.mindi.R
import androidx.core.content.withStyledAttributes

class BorderTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var text: String = ""
    var textSizePx: Float = 0f
    var textColor: Int = Color.WHITE
    var strokeColor: Int = Color.BLACK
    var strokeWidth: Float = 6f
    var isStrokeEnabled: Boolean = true
    var gravity: Int = Gravity.CENTER

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }

    private val bounds = Rect()

    init {
        context.withStyledAttributes(attrs, R.styleable.BorderTextView) {
            text = getString(R.styleable.BorderTextView_text) ?: text
            textSizePx = getDimension(R.styleable.BorderTextView_textSize, textSizePx)
            textColor = getColor(R.styleable.BorderTextView_textColor, textColor)
            strokeColor = getColor(R.styleable.BorderTextView_strokeColor, strokeColor)
            strokeWidth = getDimension(R.styleable.BorderTextView_textStrokeWidth, strokeWidth)
            isStrokeEnabled = getBoolean(R.styleable.BorderTextView_isTextStrokeEnabled, true)
            gravity = getInt(R.styleable.BorderTextView_gravity, gravity)
        }
        val tf = ResourcesCompat.getFont(context, R.font.poetsen_one_regular)
        textPaint.typeface = tf
        strokePaint.typeface = tf
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textPaint.textSize = textSizePx
        textPaint.getTextBounds(text, 0, text.length, bounds)

        val textWidth = bounds.width()
        val textHeight = bounds.height()

        val desiredWidth = textWidth + paddingLeft + paddingRight
        val desiredHeight = textHeight + paddingTop + paddingBottom

        val resolvedWidth = resolveSize(desiredWidth, widthMeasureSpec)
        val resolvedHeight = resolveSize(desiredHeight, heightMeasureSpec)

        setMeasuredDimension(resolvedWidth, resolvedHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        textPaint.textSize = textSizePx
        textPaint.color = textColor

        strokePaint.textSize = textSizePx
        strokePaint.color = strokeColor
        strokePaint.strokeWidth = strokeWidth

        // Get text bounds
        textPaint.getTextBounds(text, 0, text.length, bounds)

        val textWidth = bounds.width().toFloat()
        val textHeight = bounds.height().toFloat()

        // Compute horizontal alignment
        val x = when (gravity and Gravity.HORIZONTAL_GRAVITY_MASK) {
            Gravity.CENTER_HORIZONTAL -> (width - textWidth) / 2f
            Gravity.RIGHT -> width - textWidth - paddingRight.toFloat()
            else -> paddingLeft.toFloat()
        }

        // Compute vertical alignment
        val yBaseline = when (gravity and Gravity.VERTICAL_GRAVITY_MASK) {
            Gravity.CENTER_VERTICAL -> (height + textHeight) / 2f - bounds.bottom
            Gravity.BOTTOM -> height - paddingBottom.toFloat()
            else -> paddingTop - bounds.top.toFloat()
        }

        if (isStrokeEnabled) {
            canvas.drawText(text, x, yBaseline, strokePaint)
        }
        canvas.drawText(text, x, yBaseline, textPaint)
    }

    fun setViewText(string: Int) {
        text = resources.getString(string)
    }

    fun setViewTextSize(size: Int) {
        textSizePx = size.toFloat()
    }
}
