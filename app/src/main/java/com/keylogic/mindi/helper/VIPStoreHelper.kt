package com.keylogic.mindi.helper

import android.annotation.SuppressLint
import android.content.Context
import com.keylogic.mindi.database.MyPreferences
import com.keylogic.mindi.enums.VIPStore
import com.keylogic.mindi.models.StoreItem
import java.util.Calendar

class VIPStoreHelper {
    companion object {
        val INSTANCE = VIPStoreHelper()
        val avatarList = ArrayList<StoreItem>()
        val cardBackList = ArrayList<StoreItem>()
        val tablesList = ArrayList<StoreItem>()
        val backgroundList = ArrayList<StoreItem>()
    }

    fun getStoreItemPrice(tabIndex: Int, itemIndex: Int): String {
        return when(tabIndex) {
            VIPStore.AVATAR.tabIndex -> avatarList[itemIndex].getFormatedChip()
            VIPStore.CARDS.tabIndex -> cardBackList[itemIndex].getFormatedChip()
            VIPStore.TABLES.tabIndex -> tablesList[itemIndex].getFormatedChip()
            VIPStore.BACKGROUNDS.tabIndex -> backgroundList[itemIndex].getFormatedChip()
            else -> ""
        }
    }

    fun getStoreItem(tabIndex: Int, itemIndex: Int): StoreItem? {
        return when(tabIndex) {
            VIPStore.AVATAR.tabIndex -> avatarList[itemIndex]
            VIPStore.CARDS.tabIndex -> cardBackList[itemIndex]
            VIPStore.TABLES.tabIndex -> tablesList[itemIndex]
            VIPStore.BACKGROUNDS.tabIndex -> backgroundList[itemIndex]
            else -> null
        }
    }

    fun updateVIPStoreDetails() {
        for (avatar in VIPStoreHelper.avatarList) {
            if (CommonHelper.INSTANCE.isTimePassed(System.currentTimeMillis(), avatar.purchaseEndDate)) {
                avatar.purchaseDate = 0
                avatar.purchaseEndDate = 0
                if (avatar.isSelected) {
                    ProfileHelper.profileId = ProfileHelper.defaultProfileId
                    avatar.isSelected = false
                }
            }
        }

        for (cards in VIPStoreHelper.cardBackList) {
            if (CommonHelper.INSTANCE.isTimePassed(System.currentTimeMillis(), cards.purchaseEndDate)) {
                cards.purchaseDate = 0
                cards.purchaseEndDate = 0
                if (cards.isSelected) {
                    ProfileHelper.cardBackId = ProfileHelper.defaultCardsId
                    cards.isSelected = false
                }
            }
        }

        for (table in VIPStoreHelper.tablesList) {
            if (CommonHelper.INSTANCE.isTimePassed(System.currentTimeMillis(), table.purchaseEndDate)) {
                table.purchaseDate = 0
                table.purchaseEndDate = 0
                if (table.isSelected) {
                    ProfileHelper.tableId = ProfileHelper.defaultTableId
                    table.isSelected = false
                }
            }
        }

        for (background in VIPStoreHelper.backgroundList) {
            if (CommonHelper.INSTANCE.isTimePassed(System.currentTimeMillis(), background.purchaseEndDate)) {
                background.purchaseDate = 0
                background.purchaseEndDate = 0
                if (background.isSelected) {
                    ProfileHelper.backgroundId = ProfileHelper.defaultBackgroundId
                    background.isSelected = false
                }
            }
        }
    }

    @SuppressLint("DiscouragedApi")
    fun getResourceByName(context: Context, itemName: String): Int {
        return context.resources.getIdentifier(itemName, "drawable", context.packageName)
    }

    fun getAvatarPreFix(): String {
        return "avatar_"
    }

    fun getCardBackPreFix(): String {
        return "cards_"
    }

    fun getTablePreFix(): String {
        return "table_"
    }

    fun getBackgroundPreFix(): String {
        return "background_"
    }

    fun buyOrSelectStoreItem(context: Context, isBuyItem: Boolean, tabIndex: Int, itemIndex: Int, forWeek: Boolean) {
        val now = System.currentTimeMillis()
        val expiryTime = calculateExpiry(forWeek)

        when (tabIndex) {
            VIPStore.AVATAR.tabIndex -> {
                avatarList.forEach { it.isSelected = false }
                if (isBuyItem) {
                    avatarList[itemIndex].purchaseDate = now
                    avatarList[itemIndex].purchaseEndDate = expiryTime
                }
                avatarList[itemIndex].isSelected = true
                ProfileHelper.profileId = itemIndex
            }
            VIPStore.CARDS.tabIndex -> {
                cardBackList.forEach { it.isSelected = false }
                if (isBuyItem) {
                    cardBackList[itemIndex].purchaseDate = now
                    cardBackList[itemIndex].purchaseEndDate = expiryTime
                }
                cardBackList[itemIndex].isSelected = true
                ProfileHelper.cardBackId = itemIndex
            }
            VIPStore.TABLES.tabIndex -> {
                tablesList.forEach { it.isSelected = false }
                if (isBuyItem) {
                    tablesList[itemIndex].purchaseDate = now
                    tablesList[itemIndex].purchaseEndDate = expiryTime
                }
                tablesList[itemIndex].isSelected = true
                ProfileHelper.tableId = itemIndex
            }
            VIPStore.BACKGROUNDS.tabIndex -> {
                backgroundList.forEach { it.isSelected = false }
                if (isBuyItem) {
                    backgroundList[itemIndex].purchaseDate = now
                    backgroundList[itemIndex].purchaseEndDate = expiryTime
                }
                backgroundList[itemIndex].isSelected = true
                ProfileHelper.backgroundId = itemIndex
            }
        }
        MyPreferences.INSTANCE.saveGameThemeDetails(context)
    }

    private fun calculateExpiry(forWeek: Boolean): Long {
        val calendar = Calendar.getInstance()

        // Add 6 more days (including today = 7 days)
        if (forWeek) {
            calendar.add(Calendar.DAY_OF_YEAR, 6)
        }

        // Set time to 11:59:59 PM
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)

        return calendar.timeInMillis
    }

    fun generateAvatarList(): ArrayList<StoreItem> {
        val list = ArrayList<StoreItem>()

        for (i in 0..7) {
            list.add(StoreItem(i,false,1_500L * (i+1)))
        }
        return list
    }

    fun generateCardBackList(): ArrayList<StoreItem> {
        val list = ArrayList<StoreItem>()

        for (i in 0..7) {
            list.add(StoreItem(i,false,1_500L * (i+1)))
        }
        return list
    }

    fun generateTableList(): ArrayList<StoreItem> {
        val list = ArrayList<StoreItem>()

        for (i in 0..2) {
            list.add(StoreItem(i,false,2_500L * (i+1)))
        }
        return list
    }

    fun generateBackgroundList(): ArrayList<StoreItem> {
        val list = ArrayList<StoreItem>()

        for (i in 0..5) {
            list.add(StoreItem(i,false,3_500L * (i+1)))
        }
        return list
    }

}