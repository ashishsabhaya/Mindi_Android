package com.keylogic.mindi.Helper

import android.content.Context
import kotlin.random.Random

class ProfileHelper {
    companion object {
        val INSTANCE = ProfileHelper()

        var totalChips = 1_500L
        var defaultProfileId = 0
        var vipProfileId = -1
        var profileName = ""
        var profileUID = ""
        var gameWin = 0
        var gameLost = 0
        var gamePlayed = 0
    }

    fun getDefaultProfileResource(context: Context, position: Int): Int {
        val defaultProfileName = getDefaultProfilePrefix() + position
        val resource = getProfileImageByName(context, defaultProfileName)
        return resource
    }

    fun getVIPProfileResource(context: Context, position: Int): Int {
        val defaultProfileName = getVIPProfilePrefix() + position
        val resource = getProfileImageByName(context, defaultProfileName)
        return resource
    }

    fun getProfileResource(context: Context): Int {
        return if (vipProfileId == -1)
            getDefaultProfileResource(context, defaultProfileId)
        else
            getVIPProfileResource(context, vipProfileId)
    }

    fun getRandomProfileName(): String {
        val names = listOf(
            "Tokyo", "Berlin", "Rio", "Nairobi", "Denver", "Moscow", "Helsinki",
            "Oslo", "Lisbon", "Stockholm", "Palermo", "Bogota", "Marseille",
            "Chicago", "Seoul", "Sydney", "Jakarta", "Vienna", "Miami", "Cape Town"
        )
        return names[Random.nextInt(0, names.size)]
    }

    fun getProfileImageByName(context: Context, profileName: String): Int {
        return context.resources.getIdentifier(profileName, "drawable", context.packageName)
    }

    fun getDefaultProfilePrefix(): String {
        return "dp_"
    }

    fun getVIPProfilePrefix(): String {
        return "vip_avatar_"
    }

    fun generateUniqueKey(): String {
        val letters = (1..4).map { ('A'..'Z').random() }.joinToString("")
        val numbers = (1..4).map { Random.nextInt(0, 10) }.joinToString("")
        return profileName + "_" + letters + numbers
    }

    fun generateBotUniqueKey(name: String): String {
        val letters = (1..6).map { ('A'..'Z').random() }.joinToString("")
        val numbers = (1..6).map { Random.nextInt(0, 10) }.joinToString("")
        return name + "_" + letters + numbers
    }

}