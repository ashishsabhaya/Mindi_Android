package com.keylogic.mindi.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.keylogic.mindi.R

class DialogBackgroundCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var imageView: ImageView

    init {
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )

        // Read custom attribute (strokeColor)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DialogBackgroundCard)
        val strokeColorValue = typedArray.getColor(
            R.styleable.DialogBackgroundCard_cardStrokeColor,
            ContextCompat.getColor(context, R.color.white) // default white
        )
        typedArray.recycle()

        val cardView = MaterialCardView(context).apply {
            id = generateViewId()
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                0
            ).apply {
                topToTop = LayoutParams.PARENT_ID
                bottomToBottom = LayoutParams.PARENT_ID
                startToStart = LayoutParams.PARENT_ID
                endToEnd = LayoutParams.PARENT_ID
            }
            strokeColor = strokeColorValue
            strokeWidth = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._1sdp)
        }

        imageView = ImageView(context).apply {
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
            scaleType = ImageView.ScaleType.CENTER_CROP
            setImageResource(R.drawable.dialog_background)
        }

        cardView.addView(imageView)
        addView(cardView)
    }

    fun setSpotlightBackgroundResource() {
        imageView.setImageResource(R.drawable.dialog_background1)
    }
}
