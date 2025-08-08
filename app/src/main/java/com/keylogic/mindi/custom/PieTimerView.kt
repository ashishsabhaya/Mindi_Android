package com.keylogic.mindi.custom

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.toColorInt
import kotlin.math.min

class PieTimerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = "#991ABC09".toColorInt()
        style = Paint.Style.FILL
    }

    private val rect = RectF()
    private var sweepAngle = 0f
    private var cornerRadius = 0f
    private var totalTimeMs = 10000L // default 10 seconds
    private var animator: ValueAnimator? = null

    /** New flag for team color */
    var isUserTeam: Boolean = true
        set(value) {
            field = value
            // green for user team, red for opponent team
            paint.color = if (value) "#991DF107".toColorInt() else "#99FF0204".toColorInt()
            invalidate()
        }

    /** Set the pie color manually (overrides team color) */
    fun setPieColor(color: Int) {
        paint.color = color
        invalidate()
    }

    /** Set corner radius (for rounded rectangle) */
    fun setCornerRadius(radius: Float) {
        cornerRadius = radius
        invalidate()
    }

    /** Set timer duration in seconds */
    fun setTotalTime(seconds: Int) {
        totalTimeMs = (seconds * 1000).toLong()
    }

    /** Start the pie timer animation */
    fun startTimer() {
        animator?.cancel()
        animator = ValueAnimator.ofFloat(1f, 0f).apply {
            duration = totalTimeMs
            addUpdateListener {
                val progress = it.animatedValue as Float
                sweepAngle = progress * 360f
                invalidate()
            }
            start()
        }
    }

    /** Stop the timer and reset progress */
    fun stopTimer() {
        animator?.cancel()
        sweepAngle = 0f
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val widthF = width.toFloat()
        val heightF = height.toFloat()
        val radius = min(widthF, heightF) / 2f

        rect.set(
            widthF / 2f - radius,
            heightF / 2f - radius,
            widthF / 2f + radius,
            heightF / 2f + radius
        )

        if (cornerRadius > 0) {
            val path = Path().apply {
                addRoundRect(rect, cornerRadius, cornerRadius, Path.Direction.CW)
            }
            canvas.save()
            canvas.clipPath(path)
            canvas.drawArc(rect, -90f, sweepAngle, true, paint)
            canvas.restore()
        } else {
            canvas.drawArc(rect, -90f, sweepAngle, true, paint)
        }
    }
}
