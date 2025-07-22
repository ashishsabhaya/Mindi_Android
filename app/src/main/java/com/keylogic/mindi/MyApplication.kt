package com.keylogic.mindi

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.keylogic.mindi.Helper.SystemUiHelper

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleObserver(this))
        registerActivityLifecycleCallbacks(ActivityLifecycleCallbacksImpl())

    }

}

/**
 * Separate class to handle app lifecycle events (foreground/background)
 */
class AppLifecycleObserver(private val app: MyApplication) : DefaultLifecycleObserver {
    override fun onStart(owner: LifecycleOwner) {
    }
}

/**
 * Handles activity lifecycle callbacks
 */
class ActivityLifecycleCallbacksImpl : Application.ActivityLifecycleCallbacks {

    override fun onActivityPaused(activity: Activity) {
        executeCommonFunctionOnPause()
    }

    override fun onActivityResumed(activity: Activity) {
        executeCommonFunctionOnResume()
        SystemUiHelper.INSTANCE.enableImmersiveMode(activity.window)
    }

    // Other lifecycle methods (not needed for this case)
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {
    }

    private fun executeCommonFunctionOnPause() {}

    private fun executeCommonFunctionOnResume() {}
}
