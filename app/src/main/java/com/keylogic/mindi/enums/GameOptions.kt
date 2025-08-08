package com.keylogic.mindi.enums

import com.keylogic.mindi.R

enum class GameOptions(val oName: String, val res: Int) {
    SOUND("Sound",R.drawable.ic_go_sound),
    BACKGROUND_MUSIC("Background Music",R.drawable.ic_go_music),
    VIBRATE("Vibrate",R.drawable.ic_go_vibration),
    TABLE_INFO("Table Information",R.drawable.ic_go_info),
    HELP("Help",R.drawable.ic_go_help),
    EXIT("Exit to lobby",R.drawable.ic_go_exit)
}