package com.keylogic.mindi.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.keylogic.mindi.R
import com.keylogic.mindi.helper.SystemUiHelper

abstract class BaseFullDialogFragment : DialogFragment() {

    private var isRestoredFromConfigChange = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isRestoredFromConfigChange = savedInstanceState != null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = false

        val dialog = Dialog(requireContext(), getDialogTheme())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(isDialogCancelable())
        dialog.setContentView(getContentView(dialog.layoutInflater))
        dialog.window?.let { configureWindow(it) }

        return dialog
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.let {
            SystemUiHelper.INSTANCE.fullScreenForDialog(it)
        }

        if (!isRestoredFromConfigChange) {
            dialog?.window?.setWindowAnimations(getWindowAnimationStyle())
        } else {
            dialog?.window?.setWindowAnimations(getWindowAnimationDismissStyle())
        }
    }

    open fun getDialogTheme(): Int = R.style.TransparentDialogTheme

    open fun isDialogCancelable(): Boolean = false

    open fun getWindowAnimationStyle(): Int = R.style.DialogBottomInAnimationEntryExit

    open fun getWindowAnimationDismissStyle(): Int = R.style.DialogBottomInAnimationExit

    @Suppress("DEPRECATION")
    open fun configureWindow(window: Window) {
        val animationStyle = if (isRestoredFromConfigChange) {
            getWindowAnimationDismissStyle()
        } else {
            getWindowAnimationStyle()
        }

        window.setWindowAnimations(animationStyle)
        window.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        window.setGravity(Gravity.CENTER)

        // Enable full screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // Optional: remove background dim if needed
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        // Enable immersive fullscreen UI
        SystemUiHelper.INSTANCE.fullScreenForDialog(window)
    }

    abstract fun getContentView(inflater: LayoutInflater): View
}
