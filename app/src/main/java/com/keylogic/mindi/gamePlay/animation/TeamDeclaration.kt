package com.keylogic.mindi.gamePlay.animation

import com.keylogic.mindi.databinding.GameLayoutBinding
import com.keylogic.mindi.gamePlay.helper.PositionHelper
import com.keylogic.mindi.gamePlay.models.Card
import com.keylogic.mindi.gamePlay.models.CardView

class TeamDeclaration(val layout: GameLayoutBinding) {

    fun declareTeam(totalPlayers: Int, cardList: ArrayList<Card>) {
        val context = layout.playAreaRelative.context
        for (i in 0 until totalPlayers) {
            val pos = PositionHelper.INSTANCE.getPlayerCardPosition(i+1, totalPlayers)
            val cardView = CardView(context, cardList[i], pos[0], pos[1])
            layout.playAreaRelative.addView(cardView)
        }
    }

}