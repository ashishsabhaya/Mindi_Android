package com.keylogic.mindi.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatSeekBar
import com.keylogic.mindi.helper.CommonHelper
import com.keylogic.mindi.R
import androidx.core.graphics.scale

class CustomSeekBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.seekBarStyle
) : AppCompatSeekBar(context, attrs, defStyleAttr) {
    private var minProgress = 10
    private var maxProgress = 100

    private var isSeekStrokeEnabled: Boolean = true
    private var seekStrokeWidth: Float = 4f
    private var seekHeight: Float = 20f
    private var seekCornerRadius: Float = 10f
    private var indicatorView: BetPriceIndicator? = null
    private var betPriceCountTxt: StrokeTextView? = null

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val thumbPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var thumbX = 0f
    private var currBetPrice = 0L

    private val thumbBitmap: Bitmap? by lazy {
        BitmapFactory.decodeResource(resources, R.drawable.ic_thumb)
    }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.CustomSeekBar, 0, 0).apply {
            try {
                isSeekStrokeEnabled = getBoolean(R.styleable.CustomSeekBar_isSeekStrokeEnabled, isSeekStrokeEnabled)
                seekStrokeWidth = getDimension(R.styleable.CustomSeekBar_seekStrokeWidth, seekStrokeWidth)
                seekHeight = getDimension(R.styleable.CustomSeekBar_seekHeight, seekHeight)
                seekCornerRadius = getDimension(R.styleable.CustomSeekBar_seekCornerRadius, seekCornerRadius)
            } finally {
                recycle()
            }
        }

        // Remove default thumb if you're using a custom bitmap
        thumb = null
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        val verticalMargin = seekHeight * 0.1f
        if (seekStrokeWidth == 4f)
            seekStrokeWidth = verticalMargin / 3f
        val width = width.toFloat()
        val height = height.toFloat()
        val centerY = height / 2f
        val top = centerY - seekHeight / 2f + verticalMargin
        val bottom = centerY + seekHeight / 2f - verticalMargin
        val progressRatio = (progress.coerceIn(minProgress, maxProgress)).toFloat() / max
        val thumbMargin = seekHeight / 2f
        val progressX = (thumbMargin + (width - 2 * thumbMargin) * progressRatio)

        // Draw background
        val bgRect = RectF(0f, top, width, bottom)
        paint.shader = null
        paint.color = context.getColor(R.color.position_fg_single)
        canvas.drawRoundRect(bgRect, seekCornerRadius, seekCornerRadius, paint)

        // Draw progress gradient
        val progressRect = RectF(0f, top, progressX, bottom)
        val gradient = LinearGradient(
            0f, top, 0f, bottom,
            context.getColor(R.color.blue_selected_start),
            context.getColor(R.color.blue_selected_end),
            Shader.TileMode.CLAMP
        )
        paint.shader = gradient
        canvas.drawRoundRect(progressRect, seekCornerRadius, seekCornerRadius, paint)
        paint.shader = null

        // Optional stroke
        if (isSeekStrokeEnabled) {
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = seekStrokeWidth
            paint.color = Color.WHITE

            val strokeInset = seekStrokeWidth / 2f
            val strokeRect = RectF(
                strokeInset,
                top + strokeInset,
                width - strokeInset,
                bottom - strokeInset
            )
            canvas.drawRoundRect(strokeRect, seekCornerRadius, seekCornerRadius, paint)
            paint.style = Paint.Style.FILL
        }

        // Draw custom thumb
        val thumbSize = seekHeight
        thumbX = (progressX - thumbSize / 2f)
        indicatorView?.x = thumbX - seekHeight * 0.1f
        val thumbY = centerY - thumbSize / 2f

        //chip count as per progress
        val chipCount = nearestMultipleOfFive(progress) * 100L
        currBetPrice = chipCount
        betPriceCountTxt?.text = CommonHelper.INSTANCE.getChip(chipCount)

        thumbBitmap?.let {
            val scaledThumb = it.scale(thumbSize.toInt(), thumbSize.toInt())
            canvas.drawBitmap(scaledThumb, thumbX, thumbY, null)
        } ?: run {
            thumbPaint.color = Color.YELLOW
            canvas.drawCircle(progressX, centerY, thumbSize / 2f, thumbPaint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredHeight = (seekHeight + paddingTop + paddingBottom).toInt()
        val resolvedHeight = resolveSize(desiredHeight, heightMeasureSpec)
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(resolvedHeight, MeasureSpec.EXACTLY))
    }

    fun setCustomProgress(deckCount: Int) {
        val curr = deckCount * 5
        minProgress = curr
        progress = curr.coerceIn(minProgress, maxProgress)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val handled = super.onTouchEvent(event)
        if (event.action == MotionEvent.ACTION_DOWN) {
            progress = nearestMultipleOfFive(progress).coerceIn(minProgress, maxProgress)
        }
        if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_MOVE) {
            progress = nearestMultipleOfFive(progress).coerceIn(minProgress, maxProgress)
        }
        return handled
    }

    fun nearestMultipleOfFive(value: Int): Int {
        val lower = (value / 5) * 5
        val upper = lower + 5
        return if (value - lower < upper - value) lower else upper
    }

    fun getBetPrice(): Long { return currBetPrice }

    override fun setProgress(progress: Int) {
        val snapped = nearestMultipleOfFive(progress).coerceIn(minProgress, maxProgress)
        super.setProgress(snapped)
    }

    fun updateIndicatorPosition(betPriceIndicator: BetPriceIndicator, betPriceTxt: StrokeTextView) {
        indicatorView = betPriceIndicator
        betPriceCountTxt = betPriceTxt
    }

}
