package com.keylogic.mindi.Custom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.keylogic.mindi.R
import androidx.core.content.withStyledAttributes

class IconTextConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var isStrokeEnabled = true
    private var isElevationEnabled = true
    private var isIconVisible = true
    private var customCornerRadius = 0f.dpToPx(context)
    private var iconResource = 0
    private var customHeight = 0f.dpToPx(context)
    private var defaultStrokeWidth = 0f.dpToPx(context)
    private var elevationValue = 8f.dpToPx(context)

    private lateinit var circleView: ImageView
    private lateinit var labelView: StrokeTextView
    private lateinit var titleLabelView: StrokeTextView

    private var labelText: String = ""
    private var titleLabelText: String = ""
    private var labelTextSize: Float = 18f.spToPx(context)
    private var iconSize: Float = 18f.spToPx(context)
    private var labelStartMargin: Int = 5.dpToPxInt()
    private var contentPadding: Int = 2.dpToPxInt()

    init {
        context.withStyledAttributes(attrs, R.styleable.IconTextConstraintLayout) {

            defaultStrokeWidth =
                getDimension(R.styleable.IconTextConstraintLayout_cStrokeWidth, defaultStrokeWidth)
            customCornerRadius =
                getDimension(R.styleable.IconTextConstraintLayout_cCornerRadius, customCornerRadius)
            isStrokeEnabled =
                getBoolean(R.styleable.IconTextConstraintLayout_cIsViewStrokeEnabled, isStrokeEnabled)
            isIconVisible =
                getBoolean(R.styleable.IconTextConstraintLayout_cIsIconVisible, isIconVisible)
            isElevationEnabled =
                getBoolean(R.styleable.IconTextConstraintLayout_cIsElevationEnabled, isElevationEnabled)
            labelText = getString(R.styleable.IconTextConstraintLayout_cLabelText) ?: labelText
            titleLabelText = getString(R.styleable.IconTextConstraintLayout_cTitleLabelText) ?: titleLabelText
            context.withStyledAttributes(attrs, R.styleable.IconTextConstraintLayout) {
                iconResource = getResourceId(
                    R.styleable.IconTextConstraintLayout_cIconResource,
                    iconResource
                )
            }
            labelTextSize =
                getDimension(R.styleable.IconTextConstraintLayout_cLabelTextSize, labelTextSize)
            iconSize =
                getDimension(R.styleable.IconTextConstraintLayout_cIconSize, iconSize)
            labelStartMargin = getDimensionPixelSize(
                R.styleable.IconTextConstraintLayout_cLabelStartMargin,
                labelStartMargin
            )
            contentPadding = getDimensionPixelSize(
                R.styleable.IconTextConstraintLayout_cContentPadding,
                contentPadding
            )

            if (hasValue(R.styleable.IconTextConstraintLayout_cViewHeight)) {
                customHeight = getDimension(R.styleable.IconTextConstraintLayout_cViewHeight, 0f)
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

        if (isElevationEnabled)
            elevation = elevationValue
    }

    fun setViewTextAndResource(text: String, resource: Int) {
        labelText = text
        setupChildren()
        if (isIconVisible)
            circleView.setImageResource(resource)
    }

    private fun setupChildren() {
        removeAllViews()


        // LinearLayout to hold icon and label
        val linearLayout = LinearLayout(context).apply {
            id = generateViewId()
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            ).apply {
                topToTop = LayoutParams.PARENT_ID
                bottomToBottom = LayoutParams.PARENT_ID
                startToStart = LayoutParams.PARENT_ID
                endToEnd = LayoutParams.PARENT_ID
            }
            setPadding(contentPadding, contentPadding, contentPadding, contentPadding)
        }

        if (titleLabelText.isNotEmpty()) {
            titleLabelView = StrokeTextView(context).apply {
                id = generateViewId()
                text = titleLabelText
                setTextColor(Color.WHITE)
                gravity = if (isIconVisible) Gravity.START else Gravity.CENTER
                setTextSize(TypedValue.COMPLEX_UNIT_PX, labelTextSize)
                layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                    topToTop = LayoutParams.PARENT_ID
                    startToStart = LayoutParams.PARENT_ID
                }
            }
        }

        val circleSize = iconSize.toInt()
        circleView = ImageView(context).apply {
            id = generateViewId()
            layoutParams = LayoutParams(circleSize, circleSize).apply {
                marginStart = 0
                topToTop = LayoutParams.PARENT_ID
                bottomToBottom = LayoutParams.PARENT_ID
                if (titleLabelText.isNotEmpty()) {
                    startToEnd = titleLabelView.id
                    marginStart = labelStartMargin
                }
                else
                    startToStart = LayoutParams.PARENT_ID
            }
            setImageResource(iconResource)
        }

        labelView = StrokeTextView(context).apply {
            id = generateViewId()
            text = labelText
            setTextColor(Color.WHITE)
            gravity = if (isIconVisible) Gravity.START else Gravity.CENTER
            setTextSize(TypedValue.COMPLEX_UNIT_PX, labelTextSize)
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                topToTop = LayoutParams.PARENT_ID
                bottomToBottom = LayoutParams.PARENT_ID
                if (isIconVisible) {
                    startToEnd = circleView.id
                    marginStart = labelStartMargin
                }
            }
        }

        if (titleLabelText.isNotEmpty())
            linearLayout.addView(titleLabelView)
        if (isIconVisible)
            linearLayout.addView(circleView)
        linearLayout.addView(labelView)
        addView(linearLayout)
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
        val bgColor = if (isIconVisible) R.color.radio_container_fill else R.color.icon_txt_container_fill1
        val fillColor = ContextCompat.getColor(context, bgColor)

        return GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(fillColor, fillColor)).apply {
            this.cornerRadius = cornerRadius
            if (strokeWidth > 0f) {
                setStroke(strokeWidth.toInt(), ContextCompat.getColor(context, R.color.white))
            }
        }
    }

    // Extensions
    private fun Float.dpToPx(context: Context): Float =
        this * context.resources.displayMetrics.density

    private fun Int.dpToPxInt(): Int =
        (this * context.resources.displayMetrics.density).toInt()

    private fun Float.spToPx(context: Context): Float =
        this * context.resources.displayMetrics.scaledDensity

    fun Float.sspToPx(context: Context): Float =
        this * context.resources.displayMetrics.scaledDensity

}
