package com.keylogic.mindi.database

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.keylogic.mindi.helper.CommonHelper
import com.keylogic.mindi.helper.DailyRewardHelper
import com.keylogic.mindi.helper.ProfileHelper
import com.keylogic.mindi.helper.VIPStoreHelper
import com.keylogic.mindi.models.StoreItem
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Locale


class MyPreferences {
    companion object {
        val INSTANCE = MyPreferences()

        private const val PREFS_NAME = "game_prefs"
        private const val KEY_IS_NEW_USER = "is_new_user"
        private const val KEY_IS_ADS_FREE = "is_ads_free"
        private const val KEY_LAST_SAVED_DATE = "lastSavedDate"
        private const val KEY_IS_NEW_DAY = "isNewDay"
        private const val KEY_MUSIC = "isMusicEnabled"
        private const val KEY_SOUND = "isSoundEnabled"
        private const val KEY_VIBRATION = "isVibrationEnabled"
        private const val KEY_TOTAL_CHIPS = "totalChips"
        private const val KEY_DEFAULT_PROFILE_ID = "defaultCardsId"
        private const val KEY_DEFAULT_CARD_ID = "defaultProfileId"
        private const val KEY_DEFAULT_TABLE_ID = "defaultTableId"
        private const val KEY_DEFAULT_BACKGROUND_ID = "defaultBackgroundId"
        private const val KEY_PROFILE_NAME = "profileName"
        private const val KEY_PROFILE_UID = "profileUID"
        private const val KEY_GAME_WIN = "gameWin"
        private const val KEY_GAME_LOST = "gameLost"
        private const val KEY_GAME_PLAYED = "gamePlayed"
        private const val KEY_PROFILE_ID = "profileId"
        private const val KEY_TABLE_ID = "tableId"
        private const val KEY_CARD_BACK_ID = "cardBackId"
        private const val KEY_BACKGROUND_ID = "backgroundId"

        private const val KEY_AVATAR_LIST = "avatarList"
        private const val KEY_TABLE_LIST = "tableList"
        private const val KEY_CARD_BACK_LIST = "cardBackList"
        private const val KEY_BACKGROUND_LIST = "backgroundList"

        private const val KEY_CURR_REWARD = "curr_reward"
        private const val KEY_CURR_REWARD_DAY = "curr_reward_day"
        private const val KEY_IS_REWARD_COLLECTED = "isRewardCollected"
    }

    fun saveGameDetails(context: Context) {
        saveGameSettings(context)
        saveGameThemeDetails(context)
        saveGameProfileDetails(context)
        saveDailyReward(context)
    }

    fun saveGameSettings(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit {
            putBoolean(KEY_IS_NEW_USER, CommonHelper.isNewUser)
            putBoolean(KEY_IS_ADS_FREE, CommonHelper.isAdsFree)
            putBoolean(KEY_MUSIC, CommonHelper.isNewDay)
            putLong(KEY_LAST_SAVED_DATE, CommonHelper.lastSavedDate)
            putBoolean(KEY_IS_NEW_DAY, CommonHelper.isMusicEnabled)
            putBoolean(KEY_SOUND, CommonHelper.isSoundEnabled)
            putBoolean(KEY_VIBRATION, CommonHelper.isVibrationEnabled)
        }
    }

    fun saveDailyReward(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit {
            putInt(KEY_CURR_REWARD, DailyRewardHelper.currDay)
            putString(KEY_CURR_REWARD_DAY, DailyRewardHelper.lastSavedRewardDay)
            putBoolean(KEY_IS_REWARD_COLLECTED, DailyRewardHelper.isDailyRewardCollected)
        }
        saveGameProfileDetails(context)
    }

    fun saveGameThemeDetails(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        prefs.edit {
            putLong(KEY_TOTAL_CHIPS, ProfileHelper.totalChips)

            putInt(KEY_DEFAULT_PROFILE_ID, ProfileHelper.defaultProfileId)
            putInt(KEY_DEFAULT_CARD_ID, ProfileHelper.defaultCardsId)
            putInt(KEY_DEFAULT_TABLE_ID, ProfileHelper.defaultTableId)
            putInt(KEY_DEFAULT_BACKGROUND_ID, ProfileHelper.defaultBackgroundId)
            putInt(KEY_PROFILE_ID, ProfileHelper.profileId)
            putInt(KEY_TABLE_ID, ProfileHelper.tableId)
            putInt(KEY_CARD_BACK_ID, ProfileHelper.cardBackId)
            putInt(KEY_BACKGROUND_ID, ProfileHelper.backgroundId)

            putString(KEY_AVATAR_LIST, gson.toJson(VIPStoreHelper.avatarList))
            putString(KEY_CARD_BACK_LIST, gson.toJson(VIPStoreHelper.cardBackList))
            putString(KEY_TABLE_LIST, gson.toJson(VIPStoreHelper.tablesList))
            putString(KEY_BACKGROUND_LIST, gson.toJson(VIPStoreHelper.backgroundList))
        }
    }

    fun saveGameProfileDetails(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit {
            putLong(KEY_TOTAL_CHIPS, ProfileHelper.totalChips)
            putString(KEY_PROFILE_NAME, ProfileHelper.profileName)
            putString(KEY_PROFILE_UID, ProfileHelper.profileUID)
            putInt(KEY_GAME_WIN, ProfileHelper.gameWin)
            putInt(KEY_GAME_LOST, ProfileHelper.gameLost)
            putInt(KEY_GAME_PLAYED, ProfileHelper.gamePlayed)
        }
    }

    fun loadGameSettings(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        CommonHelper.isNewUser = prefs.getBoolean(KEY_IS_NEW_USER, CommonHelper.isNewUser)
        CommonHelper.isAdsFree = prefs.getBoolean(KEY_IS_NEW_USER, CommonHelper.isAdsFree)
        CommonHelper.isNewDay = prefs.getBoolean(KEY_IS_NEW_DAY, CommonHelper.isNewDay)
        CommonHelper.lastSavedDate = prefs.getLong(KEY_LAST_SAVED_DATE, CommonHelper.lastSavedDate)
        CommonHelper.isMusicEnabled = prefs.getBoolean(KEY_MUSIC, CommonHelper.isMusicEnabled)
        CommonHelper.isSoundEnabled = prefs.getBoolean(KEY_SOUND, CommonHelper.isSoundEnabled)
        CommonHelper.isVibrationEnabled = prefs.getBoolean(KEY_VIBRATION, CommonHelper.isVibrationEnabled)

        DailyRewardHelper.currDay = prefs.getInt(KEY_CURR_REWARD, DailyRewardHelper.currDay)
        DailyRewardHelper.lastSavedRewardDay = prefs.getString(KEY_CURR_REWARD_DAY, DailyRewardHelper.lastSavedRewardDay) ?: DailyRewardHelper.lastSavedRewardDay
        DailyRewardHelper.isDailyRewardCollected = prefs.getBoolean(KEY_IS_REWARD_COLLECTED, DailyRewardHelper.isDailyRewardCollected)

        ProfileHelper.totalChips = prefs.getLong(KEY_TOTAL_CHIPS, ProfileHelper.totalChips)
        ProfileHelper.profileName = prefs.getString(KEY_PROFILE_NAME, ProfileHelper.profileName) ?: ProfileHelper.profileName
        ProfileHelper.profileUID = prefs.getString(KEY_PROFILE_UID, ProfileHelper.profileUID) ?: ProfileHelper.profileUID
        ProfileHelper.gameWin = prefs.getInt(KEY_GAME_WIN, ProfileHelper.gameWin)
        ProfileHelper.gameLost = prefs.getInt(KEY_GAME_LOST, ProfileHelper.gameLost)
        ProfileHelper.gamePlayed = prefs.getInt(KEY_GAME_PLAYED, ProfileHelper.gamePlayed)

        ProfileHelper.defaultProfileId = prefs.getInt(KEY_DEFAULT_PROFILE_ID, ProfileHelper.defaultProfileId)
        ProfileHelper.defaultCardsId = prefs.getInt(KEY_DEFAULT_CARD_ID, ProfileHelper.defaultCardsId)
        ProfileHelper.defaultTableId = prefs.getInt(KEY_DEFAULT_TABLE_ID, ProfileHelper.defaultTableId)
        ProfileHelper.defaultBackgroundId = prefs.getInt(KEY_DEFAULT_BACKGROUND_ID, ProfileHelper.defaultBackgroundId)
        ProfileHelper.profileId = prefs.getInt(KEY_PROFILE_ID, ProfileHelper.profileId)
        ProfileHelper.tableId = prefs.getInt(KEY_TABLE_ID, ProfileHelper.tableId)
        ProfileHelper.cardBackId = prefs.getInt(KEY_CARD_BACK_ID, ProfileHelper.cardBackId)
        ProfileHelper.backgroundId = prefs.getInt(KEY_BACKGROUND_ID, ProfileHelper.backgroundId)

        VIPStoreHelper.avatarList.addAll(Gson().fromJson(prefs.getString(KEY_AVATAR_LIST, "[]"), object : TypeToken<ArrayList<StoreItem>>() {}.type))
        VIPStoreHelper.cardBackList.addAll(Gson().fromJson(prefs.getString(KEY_CARD_BACK_LIST, "[]"), object : TypeToken<ArrayList<StoreItem>>() {}.type))
        VIPStoreHelper.tablesList.addAll(Gson().fromJson(prefs.getString(KEY_TABLE_LIST, "[]"), object : TypeToken<ArrayList<StoreItem>>() {}.type))
        VIPStoreHelper.backgroundList.addAll(Gson().fromJson(prefs.getString(KEY_BACKGROUND_LIST, "[]"), object : TypeToken<ArrayList<StoreItem>>() {}.type))

        if (VIPStoreHelper.avatarList.isEmpty())
            VIPStoreHelper.avatarList.addAll(VIPStoreHelper.INSTANCE.generateAvatarList())
        if (VIPStoreHelper.cardBackList.isEmpty())
            VIPStoreHelper.cardBackList.addAll(VIPStoreHelper.INSTANCE.generateCardBackList())
        if (VIPStoreHelper.tablesList.isEmpty())
            VIPStoreHelper.tablesList.addAll(VIPStoreHelper.INSTANCE.generateTableList())
        if (VIPStoreHelper.backgroundList.isEmpty())
            VIPStoreHelper.backgroundList.addAll(VIPStoreHelper.INSTANCE.generateBackgroundList())

    }
}
