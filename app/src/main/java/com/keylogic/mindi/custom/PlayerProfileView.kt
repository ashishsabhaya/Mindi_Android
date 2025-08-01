package com.keylogic.mindi.custom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.card.MaterialCardView
import com.keylogic.mindi.R

class PlayerProfileView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private var containerWidth = 0
    private var isRedTeam = true
    private var containerHeight = 0
    private var backgroundHeight = 0
    private var cardMarginArr = intArrayOf(0, 0, 0, 0)
    private var textMarginArr = intArrayOf(0, 0, 0, 0)
    private var textSize = 16f
    private var cardStrokeWidth = 0
    private var cardStrokeColor = Color.WHITE

    private lateinit var imageView: ImageView
    private lateinit var nameTextView: StrokeTextView
    private lateinit var backgroundLayout: SelectionConstraintLayout
    private lateinit var imageCardView: MaterialCardView

    init {
        initializeAttributes(context, attrs)
    }

    private fun initializeAttributes(context: Context, attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.PlayerProfileView).apply {
            containerWidth = getDimensionPixelSize(R.styleable.PlayerProfileView_pContainerWidth, 0)
            containerHeight = getDimensionPixelSize(R.styleable.PlayerProfileView_pContainerHeight, 0)
            recycle()
        }
    }

    fun setupLayout(context: Context) {
        layoutParams = LayoutParams(containerWidth, containerHeight)

        val linearLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
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
            setCardBackgroundColor(context.getColor(R.color.player_waiting_bg))
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
            setTextColor(context.getColor(R.color.white))
            isSingleLine = true
        }

        backgroundLayout.addView(nameTextView)
        linearLayout.addView(imageCardView)
        linearLayout.addView(backgroundLayout)
        this.addView(linearLayout)
    }

    // ========= Setter Functions =========
    fun setContainerSize(widthPx: Int, heightPx: Int, isRed: Boolean = true) {
        containerWidth = widthPx
        containerHeight = heightPx
        cardStrokeWidth = containerWidth / 60
        isRedTeam = isRed
        backgroundHeight = containerHeight / 4
        textSize = backgroundHeight / 4f
        val margin = backgroundHeight / 8
        textMarginArr = intArrayOf(margin, 0, margin, 0)
        setupLayout(context)

        if (isRedTeam)
            imageCardView.strokeColor = context.getColor(R.color.red_team)
        else
            imageCardView.strokeColor = context.getColor(R.color.green_team)
    }

    fun updateDetails(
        name: String,
        imageResId: Int
    ) {
        nameTextView.text = name
        imageView.setImageResource(imageResId)
    }

}
