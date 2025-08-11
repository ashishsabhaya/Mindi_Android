package com.keylogic.mindi.helper

import android.annotation.SuppressLint
import android.content.Context
import com.keylogic.mindi.gamePlay.models.PlayerDetails
import kotlin.random.Random

class ProfileHelper {
    companion object {
        val INSTANCE = ProfileHelper()

        var freeChipCount = 500L
        var totalChips = 1_500L
        var profileName = ""
        var profileUID = ""
        var gameWin = 0
        var gameLost = 0
        var gamePlayed = 0
        var defaultProfileId = 0
        var defaultCardsId = 0
        var defaultTableId = 100
        var defaultBackgroundId = 0
        var profileId = 0
        var tableId = 0
        var cardBackId = 0
        var backgroundId = 0
    }

    fun getUserProfileDetails(joinIndex: Int = 0): PlayerDetails {
        return PlayerDetails(
            isCurrPlayer = true,
            name = profileName,
            uId = profileUID,
            profileId = profileId,
            joinIndex = joinIndex,
            isBot = false,
            isOnline = false,
            isCreator = false,
            lastTimeStamp = System.currentTimeMillis(),
            isMyTeammate = -1,
            centerCardIndex = -1
        )
    }

    fun getProfileResource(context: Context, position: Int): Int {
        val name = VIPStoreHelper.INSTANCE.getAvatarPreFix() + position
        val resource = getProfileImageByName(context, name)
        return resource
    }

    fun getProfileResource(context: Context): Int {
        val name = VIPStoreHelper.INSTANCE.getAvatarPreFix() + profileId
        val resource = getProfileImageByName(context, name)
        return resource
    }

    fun getCardResource(context: Context): Int {
        val name = VIPStoreHelper.INSTANCE.getCardBackPreFix() + cardBackId
        val resource = getProfileImageByName(context, name)
        return resource
    }

    fun getTableResource(context: Context): Int {
        val name = VIPStoreHelper.INSTANCE.getTablePreFix() + tableId
        val resource = getProfileImageByName(context, name)
        return resource
    }

    fun getBackgroundResource(context: Context): Int {
        val name = VIPStoreHelper.INSTANCE.getBackgroundPreFix() + backgroundId
        val resource = getProfileImageByName(context, name)
        return resource
    }

    fun getRandomProfileName(): String {
        val names = listOf(
            "Tokyo", "Berlin", "Rio", "Nairobi", "Denver", "Moscow", "Helsinki",
            "Oslo", "Lisbon", "Stockholm", "Palermo", "Bogota", "Marseille",
            "Chicago", "Seoul", "Sydney", "Jakarta", "Vienna", "Miami", "Cape Town"
        )
        return names[Random.nextInt(0, names.size)]
    }

    @SuppressLint("DiscouragedApi")
    fun getProfileImageByName(context: Context, profileName: String): Int {
        return context.resources.getIdentifier(profileName, "drawable", context.packageName)
    }

    fun generateUniqueKey(): String {
        val letters = (1..4).map { ('A'..'Z').random() }.joinToString("")
        val numbers = (1..4).map { Random.nextInt(0, 10) }.joinToString("")
        return profileName + "_" + letters + numbers
    }
}