package com.keylogic.mindi.Database

import android.content.Context
import com.keylogic.mindi.Helper.CommonHelper
import com.keylogic.mindi.Helper.ProfileHelper
import androidx.core.content.edit

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
    }

    fun saveGameDetails(context: Context) {
        saveGameSettings(context)
        saveGameThemeDetails(context)
        saveGameProfileDetails(context)
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

    fun saveGameThemeDetails(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit {
            putInt(KEY_DEFAULT_PROFILE_ID, ProfileHelper.defaultProfileId)
            putInt(KEY_DEFAULT_CARD_ID, ProfileHelper.defaultCardsId)
            putInt(KEY_DEFAULT_TABLE_ID, ProfileHelper.defaultTableId)
            putInt(KEY_DEFAULT_BACKGROUND_ID, ProfileHelper.defaultBackgroundId)
            putInt(KEY_PROFILE_ID, ProfileHelper.profileId)
            putInt(KEY_TABLE_ID, ProfileHelper.tableId)
            putInt(KEY_CARD_BACK_ID, ProfileHelper.cardBackId)
            putInt(KEY_BACKGROUND_ID, ProfileHelper.backgroundId)
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
    }
}
