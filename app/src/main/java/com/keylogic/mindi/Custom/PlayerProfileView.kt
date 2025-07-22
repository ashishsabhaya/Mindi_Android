package com.keylogic.mindi.Custom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import com.google.android.material.card.MaterialCardView
import com.keylogic.mindi.R

class PlayerProfileView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var containerWidth = 0
    private var containerHeight = 0
    private var backgroundHeight = 0
    private var cardMarginArr = intArrayOf(0, 0, 0, 0)
    private var textMarginArr = intArrayOf(0, 0, 0, 0)
    private var textSize = 16f
    private var cardStrokeWidth = 0
    private var cardStrokeColor = Color.TRANSPARENT
    private var initDefine = true

    private lateinit var imageView: ImageView
    private lateinit var nameTextView: StrokeTextView
    private lateinit var backgroundLayout: SelectionConstraintLayout
    private lateinit var imageCardView: MaterialCardView

    init {
        initializeAttributes(context, attrs)
        if (initDefine) setupLayout(context)
    }

    private fun initializeAttributes(context: Context, attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.PlayerProfileView).apply {
            containerWidth = getDimensionPixelSize(R.styleable.PlayerProfileView_pContainerWidth, 0)
            containerHeight = getDimensionPixelSize(R.styleable.PlayerProfileView_pContainerHeight, 0)
            recycle()
        }
    }

    private fun setupLayout(context: Context) {
        layoutParams = LayoutParams(containerWidth, containerHeight)

        val linearLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }

        val imageSectionHeight = containerHeight - backgroundHeight

        // Image Card
        imageCardView = MaterialCardView(context).apply {
            layoutParams = LinearLayout.LayoutParams(imageSectionHeight, imageSectionHeight).apply {
                gravity = Gravity.CENTER_HORIZONTAL
                setMargins(cardMarginArr[0], cardMarginArr[1], cardMarginArr[2], cardMarginArr[3])
            }
            radius = imageSectionHeight / 2f
            strokeWidth = cardStrokeWidth
            strokeColor = cardStrokeColor
        }

        imageView = ImageView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            scaleType = ImageView.ScaleType.CENTER_CROP
        }

        imageCardView.addView(imageView)

        // Background Layout
        backgroundLayout = SelectionConstraintLayout(context).apply {
            updateProgrammatically(backgroundHeight, newSelection = true, isCommonGradientColor = true, false)
            layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, backgroundHeight).apply {
                //setMargins(marginLeft, marginTop + backgroundHeight/12, marginRight, marginBottom)
            }
        }

        // TextView
        nameTextView = StrokeTextView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT).apply {
                setMargins(textMarginArr[0], textMarginArr[1], textMarginArr[2], textMarginArr[3])
                startToStart = LayoutParams.PARENT_ID
                endToEnd = LayoutParams.PARENT_ID
                topToTop = LayoutParams.PARENT_ID
                bottomToBottom = LayoutParams.PARENT_ID
            }
            setPadding(0,0,0,0)
            setIsStrokeEnabled(false)
            textSize = this@PlayerProfileView.textSize
            gravity = Gravity.CENTER
            setTextColor(resources.getColor(R.color.white, null))
            isSingleLine = true
        }

        backgroundLayout.addView(nameTextView)
        linearLayout.addView(imageCardView)
        linearLayout.addView(backgroundLayout)
        this.addView(linearLayout)
    }

    // ========= Setter Functions =========
    fun setContainerSize(widthPx: Int, heightPx: Int) {
        containerWidth = widthPx
        containerHeight = heightPx
    }

    fun setCardViewProperties(strokeWidthPx: Int = containerWidth / 60, strokeColor: Int = Color.WHITE, marginPxArr: IntArray? = null) {
        cardStrokeWidth = strokeWidthPx
        cardStrokeColor = strokeColor
        if (marginPxArr != null)
            cardMarginArr = marginPxArr
    }

    fun setBackgroundLayoutHeight(heightPx: Int = containerHeight / 4) {
        backgroundHeight = heightPx
    }

    fun setTextViewProperties(textSizePx: Float = backgroundHeight / 4f, marginPxArr: IntArray? = null) {
        textSize = textSizePx
        val margin = backgroundHeight / 8
        textMarginArr = marginPxArr ?: intArrayOf(margin, 0, margin, 0)
    }

    fun setPlayerName(name: String) {
        if (::nameTextView.isInitialized) {
            nameTextView.text = name
        }
    }

    fun setPlayerImage(resId: Int) {
        if (::imageView.isInitialized) {
            imageView.setImageResource(resId)
        }
    }

    fun updateDetails(
        name: String,
        imageResId: Int,
        initDefine: Boolean = true
    ) {
        this.initDefine = initDefine
        if (initDefine) {
            this.removeAllViews()
            setupLayout(context)
        }

        setPlayerName(name)
        setPlayerImage(imageResId)
    }

    // Optional
    private fun Float.dpToPx(context: Context): Float =
        this * context.resources.displayMetrics.density
}
