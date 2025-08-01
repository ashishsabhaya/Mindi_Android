package com.keylogic.mindi.gamePlay.helper

import com.keylogic.mindi.R
import com.keylogic.mindi.custom.PlayerProfileView
import com.keylogic.mindi.databinding.GameLayoutBinding
import com.keylogic.mindi.gamePlay.models.PlayerDetails
import com.keylogic.mindi.helper.ProfileHelper

class PlayerDesignHelper(private val layout: GameLayoutBinding) {
    companion object {
        val playerWaitingProfileList = ArrayList<PlayerProfileView>()
    }

    fun designWaitingPlayer(playerDetailsList: ArrayList<PlayerDetails>) {
        val totalPlayers = playerDetailsList.size
        playerWaitingProfileList.forEach { layout.playAreaRelative.removeView(it) }
        playerWaitingProfileList.clear()

        val context = layout.playAreaRelative.context
        for (i in 1 .. totalPlayers) {
            val currPlayer = playerDetailsList[i-1]
            val currPos = PositionHelper.INSTANCE.getPlayerPosition(i, totalPlayers)

            val playerView = PlayerProfileView(context)
            playerView.setContainerSize(DisplayHelper.profileWidth, DisplayHelper.profileHeight,
                isRed = currPlayer.isRedTeamMember)

            val resource = if (i == 1)
                ProfileHelper.INSTANCE.getProfileResource(context, currPlayer.profileId)
            else
                R.drawable.ic_player_waiting

            val name = if (i == 1) currPlayer.name else
                context.resources.getString(R.string.waiting)
            playerView.updateDetails(name, resource)
            playerView.x = currPos[0]
            playerView.y = currPos[1]
            layout.playAreaRelative.addView(playerView)
            playerWaitingProfileList.add(playerView)
        }
    }

    fun updateWaitingProfile(playerIndex: Int, currPlayer: PlayerDetails) {
        val playerView = playerWaitingProfileList[playerIndex]
        val resource = ProfileHelper.INSTANCE.getProfileResource(playerView.context, currPlayer.profileId)
        playerView.updateDetails(currPlayer.name, resource)
    }

}