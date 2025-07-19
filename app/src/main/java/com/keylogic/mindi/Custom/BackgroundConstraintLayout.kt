package com.keylogic.mindi.Custom

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.keylogic.mindi.R

class BackgroundConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var customCornerRadius = 0f.dpToPx(context)
    private var defaultStrokeWidth = 0f.dpToPx(context)
    private var elevationValue = 8f.dpToPx(context) // Example: fixed elevation = 8dp
    private var customWidth: Float? = null
    private var customHeight: Float? = null
    private var isPositive: Boolean = true
    private var isStrokeEnabled: Boolean = true
    private var isElevationEnabled: Boolean = true
    private var isFgColorIsSingleDarkBlue: Boolean = false
    private var isInstagram: Boolean = false
    private var isMessenger: Boolean = false
    private var isWhatsapp: Boolean = false
    private var isShare: Boolean = false

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.BackgroundConstraintLayout)

        defaultStrokeWidth = a.getDimension(R.styleable.BackgroundConstraintLayout_strokeWidth, defaultStrokeWidth)
        customCornerRadius = a.getDimension(R.styleable.BackgroundConstraintLayout_cornerRadius,0f)
        isPositive = a.getBoolean(R.styleable.BackgroundConstraintLayout_isPositive,true)
        isStrokeEnabled = a.getBoolean(R.styleable.BackgroundConstraintLayout_isStrokeEnabled,true)
        isElevationEnabled = a.getBoolean(R.styleable.BackgroundConstraintLayout_isBGElevationEnabled,true)
        isFgColorIsSingleDarkBlue = a.getBoolean(R.styleable.BackgroundConstraintLayout_isFgColorIsSingleDarkBlue,false)
        isInstagram = a.getBoolean(R.styleable.BackgroundConstraintLayout_isInstagram,false)
        isMessenger = a.getBoolean(R.styleable.BackgroundConstraintLayout_isMessenger,false)
        isWhatsapp = a.getBoolean(R.styleable.BackgroundConstraintLayout_isWhatsapp,false)
        isShare = a.getBoolean(R.styleable.BackgroundConstraintLayout_isShare,false)

        if (a.hasValue(R.styleable.BackgroundConstraintLayout_customWidth)) {
            customWidth = a.getDimension(R.styleable.BackgroundConstraintLayout_customWidth, 0f)
        }

        if (a.hasValue(R.styleable.BackgroundConstraintLayout_customHeight)) {
            customHeight = a.getDimension(R.styleable.BackgroundConstraintLayout_customHeight, 0f)
        }
        a.recycle()

        if (isElevationEnabled)
            elevation = elevationValue

        background = buildLayeredDrawable()
    }

    private fun buildLayeredDrawable(): LayerDrawable {
        val viewHeight = customHeight ?: height.toFloat()

        // Dynamic radius: 10px for 53px height
        var fgCornerRadius = viewHeight * (10f / 53f)
        if (customCornerRadius != 0f)
            fgCornerRadius = customCornerRadius
        val bgCornerRadius = fgCornerRadius + 4f

        // Dynamic stroke: 1px for 53px height
        var strokeWidth = viewHeight * (1f / 53f)
        if (defaultStrokeWidth > 0f)
            strokeWidth = defaultStrokeWidth
        if (!isStrokeEnabled)
            strokeWidth = 0f

        // Background bottom inset (approx. 8%)
        val bottomMargin = viewHeight * 0.09f

        setPadding(
            paddingLeft,
            paddingTop,
            paddingRight,
            (bottomMargin + strokeWidth).toInt() + paddingBottom
        )

        val bgDrawable = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            if (isInstagram) {
                intArrayOf(
                    ContextCompat.getColor(context, R.color.instagram_bg_start),
                    ContextCompat.getColor(context, R.color.instagram_bg_center),
                    ContextCompat.getColor(context, R.color.instagram_bg_end)
                )
            }
            else if (isMessenger) {
                intArrayOf(
                    ContextCompat.getColor(context, R.color.messenger_bg_start),
                    ContextCompat.getColor(context, R.color.messenger_bg_center),
                    ContextCompat.getColor(context, R.color.messenger_bg_end)
                )
            }
            else if (isWhatsapp) {
                intArrayOf(
                    ContextCompat.getColor(context, R.color.whatsapp_bg_start),
                    ContextCompat.getColor(context, R.color.whatsapp_bg_center),
                    ContextCompat.getColor(context, R.color.whatsapp_bg_end)
                )
            }
            else if (isShare) {
                intArrayOf(
                    ContextCompat.getColor(context, R.color.share_bg_start),
                    ContextCompat.getColor(context, R.color.share_bg_center),
                    ContextCompat.getColor(context, R.color.share_bg_end)
                )
            }
            else if (isPositive) {
                intArrayOf(
                    ContextCompat.getColor(context, R.color.position_bg_start),
                    ContextCompat.getColor(context, R.color.position_bg_center),
                    ContextCompat.getColor(context, R.color.position_bg_end)
                )
            }
            else {
                intArrayOf(
                    ContextCompat.getColor(context, R.color.negative_bg_start),
                    ContextCompat.getColor(context, R.color.negative_bg_center),
                    ContextCompat.getColor(context, R.color.negative_bg_end)
                )
            }
        ).apply {
            cornerRadius = bgCornerRadius
        }

        val fgDrawable = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            if (isInstagram) {
                intArrayOf(
                    ContextCompat.getColor(context, R.color.instagram_fg_start),
                    ContextCompat.getColor(context, R.color.instagram_fg_center),
                    ContextCompat.getColor(context, R.color.instagram_fg_end)
                )
            }
            else if (isMessenger) {
                intArrayOf(
                    ContextCompat.getColor(context, R.color.messenger_fg_start),
                    ContextCompat.getColor(context, R.color.messenger_fg_center),
                    ContextCompat.getColor(context, R.color.messenger_fg_end)
                )
            }
            else if (isWhatsapp) {
                intArrayOf(
                    ContextCompat.getColor(context, R.color.whatsapp_fg_start),
                    ContextCompat.getColor(context, R.color.whatsapp_fg_center),
                    ContextCompat.getColor(context, R.color.whatsapp_fg_end)
                )
            }
            else if (isShare) {
                intArrayOf(
                    ContextCompat.getColor(context, R.color.share_fg_start),
                    ContextCompat.getColor(context, R.color.share_fg_center),
                    ContextCompat.getColor(context, R.color.share_fg_end)
                )
            }
            else if (isPositive) {
                if (isFgColorIsSingleDarkBlue) {
                    intArrayOf(
                        ContextCompat.getColor(context, R.color.position_fg_single),
                        ContextCompat.getColor(context, R.color.position_fg_single)
                    )
                }
                else {
                    intArrayOf(
                        ContextCompat.getColor(context, R.color.position_fg_start),
                        ContextCompat.getColor(context, R.color.position_fg_end)
                    )
                }
            }
            else {
                intArrayOf(
                    ContextCompat.getColor(context, R.color.negative_fg_start),
                    ContextCompat.getColor(context, R.color.negative_fg_end)
                )
            }
        ).apply {
            cornerRadius = fgCornerRadius
            setStroke(strokeWidth.toInt(), ContextCompat.getColor(context, R.color.white))
        }

        return LayerDrawable(arrayOf(bgDrawable, fgDrawable)).apply {
            setLayerInset(
                1,
                0, 0, 0,
                bottomMargin.toInt()
            )
        }
    }

    fun setFgColorIsSingleDarkBlue(isSingleColor: Boolean) {
        isFgColorIsSingleDarkBlue = isSingleColor
    }

    fun isViewStrokeEnabled(isEnabled: Boolean) {
        isStrokeEnabled = isEnabled
    }

    fun setViewHeight(height: Int) {
        customHeight = height.toFloat()
        background = buildLayeredDrawable()
    }

    private fun Float.dpToPx(context: Context): Float =
        this * context.resources.displayMetrics.density
}
