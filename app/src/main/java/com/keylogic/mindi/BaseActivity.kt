package com.keylogic.mindi

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.keylogic.mindi.Helper.SystemUiHelper

open class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        enableEdgeToEdge()
        SystemUiHelper.INSTANCE.enableImmersiveMode(window)
    }
}