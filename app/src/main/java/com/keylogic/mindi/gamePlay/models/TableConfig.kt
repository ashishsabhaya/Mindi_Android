package com.keylogic.mindi.gamePlay.models

import android.os.Parcelable
import com.keylogic.mindi.enums.DeckType
import kotlinx.parcelize.Parcelize

@Parcelize
data class TableConfig(
    val deckType: DeckType,
    val betPrice: Long,
    val isHideMode: Boolean,
    val totalPlayers: Int,
    val isRoomFull: Boolean,
    val isPrivateTable: Boolean
) : Parcelable