package com.keylogic.mindi.custom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.keylogic.mindi.R
import androidx.core.content.withStyledAttributes

class RadioConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var isStrokeEnabled = true
    private var customCornerRadius = 0f.dpToPx(context)
    private var customHeight = 0f.dpToPx(context)
    private var defaultStrokeWidth = 0f.dpToPx(context)
    private var elevationValue = 8f.dpToPx(context)
    private var isSelectedState = false

    private lateinit var circleView: ImageView
    private lateinit var labelView: StrokeTextView

    private var labelText: String = ""
    private var labelTextSize: Float = 18f.spToPx(context)
    private var iconSize: Float = 18f.spToPx(context)
    private var labelStartMargin: Int = 5.dpToPxInt()
    private var contentPadding: Int = 2.dpToPxInt()

    init {
        context.withStyledAttributes(attrs, R.styleable.RadioConstraintLayout) {

            defaultStrokeWidth =
                getDimension(R.styleable.RadioConstraintLayout_viewStrokeWidth, defaultStrokeWidth)
            customCornerRadius = getDimension(R.styleable.RadioConstraintLayout_viewRadius, 0f)
            isStrokeEnabled =
                getBoolean(R.styleable.RadioConstraintLayout_isViewStrokeEnabled, true)
            labelText = getString(R.styleable.RadioConstraintLayout_labelText) ?: labelText
            labelTextSize =
                getDimension(R.styleable.RadioConstraintLayout_labelTextSize, labelTextSize)
            iconSize = getDimension(R.styleable.RadioConstraintLayout_iconSize, iconSize)
            isSelectedState = getBoolean(R.styleable.RadioConstraintLayout_isRadioChecked, false)
            labelStartMargin = getDimensionPixelSize(
                R.styleable.RadioConstraintLayout_labelStartMargin,
                labelStartMargin
            )
            contentPadding = getDimensionPixelSize(
                R.styleable.RadioConstraintLayout_contentPadding,
                contentPadding
            )

            if (hasValue(R.styleable.RadioConstraintLayout_viewHeight)) {
                customHeight = getDimension(R.styleable.RadioConstraintLayout_viewHeight, 0f)
            }

        }

        contentPadding += defaultStrokeWidth.toInt()

        val lPadding = contentPadding + paddingLeft
        val tPadding = contentPadding + paddingTop
        val rPadding = contentPadding + paddingRight
        val bPadding = contentPadding + paddingBottom

        setPadding(lPadding, tPadding, rPadding, bPadding)
        setupChildren()

        updateVisualState()
        updateCheck(isSelectedState)
        elevation = elevationValue
    }

    private fun setupChildren() {
        removeAllViews()

        val circleSize = iconSize.toInt()
        circleView = ImageView(context).apply {
            id = View.generateViewId()
            layoutParams = LayoutParams(circleSize, circleSize).apply {
                marginStart = 0
                topToTop = LayoutParams.PARENT_ID
                bottomToBottom = LayoutParams.PARENT_ID
                startToStart = LayoutParams.PARENT_ID
            }
        }

        labelView = StrokeTextView(context).apply {
            id = generateViewId()
            text = labelText
            setTextColor(Color.WHITE)
            gravity = Gravity.START
            setTextSize(TypedValue.COMPLEX_UNIT_PX, labelTextSize)
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                marginStart = labelStartMargin
                topToTop = LayoutParams.PARENT_ID
                bottomToBottom = LayoutParams.PARENT_ID
                startToEnd = circleView.id
            }
        }

        addView(circleView)
        addView(labelView)
    }

    private fun updateVisualState() {
        val dynamicRadius = if (customCornerRadius > 0f) {
            customCornerRadius
        } else {
            if (customHeight > 0f) customHeight * (6f / 53f) else 0f
        }

        val strokeWidth = when {
            !isStrokeEnabled || defaultStrokeWidth == 0f -> 0f
            customHeight > 0f -> defaultStrokeWidth.coerceAtLeast(customHeight * (1f / 53f))
            else -> defaultStrokeWidth
        }

        background = buildBackgroundDrawable(dynamicRadius + 2f, strokeWidth)
        invalidate()
    }

    private fun buildBackgroundDrawable(cornerRadius: Float, strokeWidth: Float): GradientDrawable {
        val fillColor = ContextCompat.getColor(context, R.color.radio_container_fill)

        return GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(fillColor, fillColor)).apply {
            this.cornerRadius = cornerRadius
            if (strokeWidth > 0f) {
                setStroke(strokeWidth.toInt(), ContextCompat.getColor(context, R.color.white))
            }
        }
    }

    fun updateCheck(isRadioChecked: Boolean) {
        isSelectedState = isRadioChecked
        if (isSelectedState)
            circleView.setImageResource(R.drawable.ic_radio_selected)
        else
            circleView.setImageResource(R.drawable.ic_radio_un_selected)
        updateVisualState()
    }

    // Extensions
    private fun Float.dpToPx(context: Context): Float =
        this * context.resources.displayMetrics.density

    private fun Int.dpToPxInt(): Int =
        (this * context.resources.displayMetrics.density).toInt()

    @Suppress("DEPRECATION")
    private fun Float.spToPx(context: Context): Float =
        this * context.resources.displayMetrics.scaledDensity
}
