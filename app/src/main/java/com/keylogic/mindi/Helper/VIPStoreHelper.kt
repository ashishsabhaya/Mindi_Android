package com.keylogic.mindi.Helper

import android.content.Context
import com.keylogic.mindi.Models.Avatar
import com.keylogic.mindi.Models.CardBack

class VIPStoreHelper {
    companion object {
        val INSTANCE = VIPStoreHelper()
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

}