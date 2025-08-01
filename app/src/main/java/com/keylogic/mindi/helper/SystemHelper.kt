package com.keylogic.mindi.helper

import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager

class SystemUiHelper {
    companion object {
        val INSTANCE = SystemUiHelper()
    }

//    fun hideNavigation(window: Window?) {
//        val decorView = window!!.decorView
//
//        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
//        val insetsController: WindowInsetsController?
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            insetsController = decorView.windowInsetsController
//            insetsController?.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//            insetsController?.hide(WindowInsets.Type.navigationBars())
//        }
//        else @Suppress("DEPRECATION") {
//            window.decorView.systemUiVisibility = (
//                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
////                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                            or View.SYSTEM_UI_FLAG_FULLSCREEN
//                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    )
//        }
//
//    }

    //hide status bar and bottom navigation
    fun enableImmersiveMode(window: Window?) {
        val decorView = window?.decorView
        val insetsController: WindowInsetsController?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            insetsController = decorView?.windowInsetsController
            insetsController?.let {
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            }
        } else @Suppress("DEPRECATION") {
            window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            window?.decorView?.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    )
        }
    }

    @Suppress("DEPRECATION")
    fun fullScreenForDialog(window: Window?) {
        window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                )
    }


//    @Suppress("DEPRECATION")
//    fun disableImmersiveMode(activity: Activity) {
//        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
//    }

}
