package com.keylogic.mindi.gamePlay.models

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.keylogic.mindi.enums.SuitType
import com.keylogic.mindi.helper.CommonHelper
import com.keylogic.mindi.gamePlay.helper.DisplayHelper
import com.keylogic.mindi.R
import com.keylogic.mindi.gamePlay.helper.GameHelper
import kotlin.apply

class CardView @JvmOverloads constructor(
    context: Context,
    var card: Card,
    var xPos: Float = 0f,
    var yPos: Float = 0f
) : AppCompatImageView(context) {
    var isDefaultCard = false
    var isCardSelected: Boolean = false
    var isDisabled: Boolean = false
        private set

    init {
        layoutParams = FrameLayout.LayoutParams(DisplayHelper.cardWidth, DisplayHelper.cardHeight)
        scaleType = ScaleType.FIT_XY
        updateView()
    }

    constructor(context: Context) : this(
        context,
        Card(SuitType.NONE, 0, 0),
        0f,
        0f
    )
    private fun updateView() {
        setImageResource(getImageResourceByName(card.getCard(), CommonHelper.currCFTheme))
        x = xPos
        y = yPos
    }

    fun setCardBack() {
        setImageResource(CommonHelper.currCBTheme)
    }

    fun setDisabled(isCardDisabled: Boolean) {
        isDisabled = isCardDisabled
        if (isTrumpCard())
            setTrumpCard(true)
        else {
            val color = ContextCompat.getColor(context, R.color.disable_cards)
            val colorWithOpacity = Color.argb(if (isCardDisabled) 70 else 0, Color.red(color), Color.green(color), Color.blue(color))
            setColorFilter(colorWithOpacity, PorterDuff.Mode.SRC_ATOP)
        }
    }

    fun setTrumpCard(isSelected: Boolean) {
        val color = ContextCompat.getColor(context, R.color.trump_cards)
        val colorWithOpacity = Color.argb(if (isSelected) 150 else 0, Color.red(color), Color.green(color), Color.blue(color))
        setColorFilter(colorWithOpacity, PorterDuff.Mode.SRC_ATOP)
    }

    fun setSelectedCard(isSelected: Boolean, withColor: Boolean = true) {
        isCardSelected = isSelected
        if (withColor) {
//            if (isTrumpCard())
//                setTrumpCard(true)
//            else {
                val color = ContextCompat.getColor(context, R.color.selected_cards)
                val colorWithOpacity = Color.argb(if (isSelected) 150 else 0, Color.red(color), Color.green(color), Color.blue(color))
                setColorFilter(colorWithOpacity, PorterDuff.Mode.SRC_ATOP)
//            }
        }
        val targetY = if (isSelected) yPos - (DisplayHelper.cardHeight / 4f) else yPos
        ObjectAnimator.ofFloat(this, "translationY", targetY).apply {
            setDuration(100)
            start()
        }
    }

    private fun isTrumpCard(): Boolean {
        return !isDefaultCard && card.suit != SuitType.NONE && card.suit == GameHelper.trumpCardSuit.suit
    }

    fun updateResource(newCard: Card) {
        this.card = newCard
        setImageResource(getImageResourceByName(newCard.getCard(), CommonHelper.currCFTheme))
//        if (isTrumpCard())
//            setTrumpCard(true)
    }

    fun setCustomSize(width: Int, height: Int) {
        layoutParams = RelativeLayout.LayoutParams(width, height)
    }

    @SuppressLint("DiscouragedApi")
    private fun getImageResourceByName(imageName: String, theme: String): Int {
        return resources.getIdentifier("$imageName$theme", "drawable", context.packageName)
    }

}
