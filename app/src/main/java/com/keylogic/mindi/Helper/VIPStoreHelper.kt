package com.keylogic.mindi.Helper

import android.content.Context
import com.keylogic.mindi.Enum.VIPStore
import com.keylogic.mindi.Models.Avatar
import com.keylogic.mindi.Models.Background
import com.keylogic.mindi.Models.CardBack
import com.keylogic.mindi.Models.Tables

class VIPStoreHelper {
    companion object {
        val INSTANCE = VIPStoreHelper()
        val avatarList = ArrayList<Avatar>()
        val cardBackList = ArrayList<CardBack>()
        val tablesList = ArrayList<Tables>()
        val backgroundList = ArrayList<Background>()
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

    fun generateAvatarList(): ArrayList<Avatar> {
        val list = ArrayList<Avatar>()

        for (i in 0..7) {
            list.add(Avatar(i,false,1_500L * (i+1),0))
        }
        return list
    }

    fun generateCardBackList(): ArrayList<CardBack> {
        val list = ArrayList<CardBack>()

        for (i in 0..7) {
            list.add(CardBack(i,false,1_500L * (i+1),0))
        }
        return list
    }

    fun generateTableList(): ArrayList<Tables> {
        val list = ArrayList<Tables>()

        for (i in 0..5) {
            list.add(Tables(i,false,2_500L * (i+1),0))
        }
        return list
    }

    fun generateBackgroundList(): ArrayList<Background> {
        val list = ArrayList<Background>()

        for (i in 0..5) {
            list.add(Background(i,false,3_500L * (i+1),0))
        }
        return list
    }

}