package com.keylogic.mindi.Dialogs

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

class ShowDialog {
    companion object {
        val INSTANCE = ShowDialog()
    }

    fun openDialogOnce(activity: Activity, fm: FragmentManager, tag: String): Boolean {
        val existing = fm.findFragmentByTag(tag)
        return (existing == null || !existing.toString().contains(tag)) && (!activity.isFinishing || !activity.isDestroyed)
    }

    fun dismiss(activity: Activity, tag: String) {
        if (activity is FragmentActivity) {
            val fm = activity.supportFragmentManager
            val existing = fm.findFragmentByTag(tag)
            if (existing is BaseDialogFragment && existing.dialog?.isShowing == true) {
                existing.dialog?.cancel()
            }
            if (existing is BaseLoadingDialogFragment && existing.dialog?.isShowing == true) {
                existing.dialog?.cancel()
            }
        }
    }

}