package com.keylogic.mindi.helper

import com.keylogic.mindi.gamePlay.models.PlayerDetails
import kotlin.random.Random

class BotHelper {
    companion object {
        val INSTANCE = BotHelper()
    }

    fun generateBotPlayerProfile(joinIndex: Int): PlayerDetails {
        return PlayerDetails(
            isCurrPlayer = false,
            name = getRandomBotProfileName(),
            uId = generateBotUniqueKey(),
            profileId = generateRandomProfileId(),
            joinIndex = joinIndex,
            isBot = true,
            isOnline = false,
            isCreator = false,
            lastTimeStamp = 0,
            isMyTeammate = -1,
            centerCardIndex = -1
        )
    }

    private fun getRandomBotProfileName(): String {
        val names = listOf(
            "Tokyo", "Berlin", "Rio", "Nairobi", "Denver", "Moscow", "Helsinki",
            "Oslo", "Lisbon", "Stockholm", "Palermo", "Bogota", "Marseille",
            "Chicago", "Seoul", "Sydney", "Jakarta", "Vienna", "Miami", "Cape Town"
        )
        return names[Random.nextInt(0, names.size)]
    }

    private fun generateBotUniqueKey(): String {
        val letters = (1..6).map { ('A'..'Z').random() }.joinToString("")
        val numbers = (1..6).map { Random.nextInt(0, 10) }.joinToString("")
        return letters + numbers + System.currentTimeMillis()
    }

    private fun generateRandomProfileId(): Int {
        return Random.nextInt(0, 8)
    }

}