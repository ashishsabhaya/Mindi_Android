package com.keylogic.mindi.Dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.keylogic.mindi.Helper.SystemUiHelper
import com.keylogic.mindi.R

abstract class BaseLoadingDialogFragment : DialogFragment() {

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

        // Only play enter music if not restoring from config change
//        if (!isRestoredFromConfigChange) {
//            Music.dialogEnter()
//        }

        return dialog
    }

    override fun onResume() {
        super.onResume()
        SystemUiHelper.INSTANCE.enableImmersiveMode(requireDialog().window)

        // Re-apply animation only if not restored (to avoid animating on config change)
//        if (!isRestoredFromConfigChange) {
//            dialog?.window?.setWindowAnimations(getWindowAnimationStyle())
//        } else {
//            dialog?.window?.setWindowAnimations(getWindowAnimationDismissStyle()) // no animation
//        }
    }

    open fun getDialogTheme(): Int = R.style.TransparentDialogTheme

    open fun isDialogCancelable(): Boolean = false

    open fun getWindowAnimationStyle(): Int = R.style.DialogAnimationEntryExit

    open fun getWindowAnimationDismissStyle(): Int = R.style.DialogAnimationExitOnly

    open fun configureWindow(window: Window) {
//        val animationStyle = if (isRestoredFromConfigChange) getWindowAnimationDismissStyle() else getWindowAnimationStyle()
//        window.setWindowAnimations(animationStyle)

        window.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        window.setGravity(Gravity.CENTER)
    }

    abstract fun getContentView(inflater: LayoutInflater): View

    override fun onDestroy() {
        super.onDestroy()

        // Only play exit music if activity is not being destroyed due to orientation change
//        if (!requireActivity().isChangingConfigurations) {
//            Music.dialogExit()
//        }
    }
}
