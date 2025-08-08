package com.keylogic.mindi.gamePlay.helper

import com.keylogic.mindi.R
import com.keylogic.mindi.gamePlay.models.PlayerProfileView
import com.keylogic.mindi.databinding.GameLayoutBinding
import com.keylogic.mindi.gamePlay.models.PlayerDetails
import com.keylogic.mindi.helper.ProfileHelper

class PlayerDesignHelper(private val layout: GameLayoutBinding) {
    val playerProfileList = ArrayList<PlayerProfileView>()

    fun designWaitingPlayer(playerDetailsList: ArrayList<PlayerDetails>) {
        val totalPlayers = playerDetailsList.size
        playerProfileList.forEach { layout.playAreaRelative.removeView(it) }
        playerProfileList.clear()

        val context = layout.playAreaRelative.context
        for (i in 1 .. totalPlayers) {
            val currPlayer = playerDetailsList[i-1]
            val currPos = PositionHelper.INSTANCE.getPlayerPosition(i, totalPlayers)

            val playerView = PlayerProfileView(context)
            playerView.setContainerSize(DisplayHelper.profileWidth, DisplayHelper.profileHeight,
                isRed = i % 2 == 0)

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
            playerProfileList.add(playerView)

            playerView.setOnClickListener {
                playerView.startTimer()
            }
        }
    }

    fun updateWaitingProfile(playerIndex: Int, currPlayer: PlayerDetails) {
        val playerView = playerProfileList[playerIndex]
        val resource = ProfileHelper.INSTANCE.getProfileResource(playerView.context, currPlayer.profileId)
        playerView.updateDetails(currPlayer.name, resource)
    }

    fun designPlayer(playerDetailsList: ArrayList<PlayerDetails>) {
        val totalPlayers = playerDetailsList.size
        playerProfileList.forEach { layout.playAreaRelative.removeView(it) }
        playerProfileList.clear()

        val context = layout.playAreaRelative.context
        for (i in 1 .. totalPlayers) {
            val currPlayer = playerDetailsList[i-1]
            val currPos = PositionHelper.INSTANCE.getPlayerPosition(i, totalPlayers)

            val playerView = PlayerProfileView(context)
            playerView.setContainerSize(DisplayHelper.profileWidth, DisplayHelper.profileHeight,
                isRed = i % 2 == 0)

            val resource = ProfileHelper.INSTANCE.getProfileResource(context, currPlayer.profileId)
            val name = currPlayer.name
            playerView.updateDetails(name, resource)
            playerView.x = currPos[0]
            playerView.y = currPos[1]
            layout.playAreaRelative.addView(playerView)
            playerProfileList.add(playerView)
        }
    }

}