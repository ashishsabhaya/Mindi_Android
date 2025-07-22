package com.keylogic.mindi.Custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.keylogic.mindi.R
import androidx.core.content.withStyledAttributes

class StrokeTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var strokeColor: Int = Color.BLACK
    private var strokeWidth: Float = 6f
    private var isStrokeEnabled: Boolean = true

    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.StrokeTextView) {
            strokeColor = getColor(R.styleable.StrokeTextView_txtStrokeColor, strokeColor)
            strokeWidth = getDimension(R.styleable.StrokeTextView_txtStrokeWidth, strokeWidth)
            isStrokeEnabled = getBoolean(R.styleable.StrokeTextView_txtStrokeEnabled, true)
        }

        // Custom font
        typeface = ResourcesCompat.getFont(context, R.font.poetsen_one_regular)
    }

    override fun onDraw(canvas: Canvas) {
        if (isStrokeEnabled) {
            val text = text ?: return
            val layout = layout ?: return

            strokePaint.color = strokeColor
            strokePaint.strokeWidth = strokeWidth
            strokePaint.textSize = paint.textSize
            strokePaint.typeface = paint.typeface
            strokePaint.textAlign = paint.textAlign

            val lineCount = layout.lineCount
            for (i in 0 until lineCount) {
                val lineStart = layout.getLineStart(i)
                val lineEnd = layout.getLineEnd(i)

                val ellipsisStart = layout.getEllipsisStart(i)
                val ellipsisCount = layout.getEllipsisCount(i)

                // Get text for this line
                val drawText = if (ellipsisCount > 0 && ellipsisStart >= 0) {
                    val visibleEnd = lineEnd - ellipsisCount
                    text.subSequence(lineStart, visibleEnd).toString() + "…"
                } else {
                    text.subSequence(lineStart, lineEnd).toString()
                }
                val x = layout.getLineLeft(i) + totalPaddingLeft
                val y = layout.getLineBaseline(i) + totalPaddingTop

                canvas.drawText(drawText, x, y.toFloat(), strokePaint)
            }
        }

        // Draw the filled text
        super.onDraw(canvas)
    }

    fun setViewText(resId: Int) {
        text = resources.getString(resId)
    }


    fun setIsStrokeEnabled(isEnabled: Boolean) {
        isStrokeEnabled = isEnabled
        invalidate()
    }

    fun setViewTextSize(spSize: Int) {
        setTextSize(spSize.toFloat())
    }
}
