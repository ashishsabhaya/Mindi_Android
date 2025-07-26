package com.keylogic.mindi.dialogs

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import com.keylogic.mindi.R
import com.keylogic.mindi.databinding.LoadingLayoutBinding

class LoadingDialogFragment : BaseLoadingDialogFragment() {
    private var _binding: LoadingLayoutBinding? = null
    private val binding get() = _binding!!

    override fun getContentView(inflater: LayoutInflater): View {
        _binding = LoadingLayoutBinding.inflate(inflater)
        LoadingAnimDesign.INSTANCE.gameLoadingAnimations(binding.dLoadingGamesRela)

        if (loadingMesg.isEmpty())
            binding.loadingTxt.text = resources.getString(R.string.please_wait)
        else
            binding.loadingTxt.text = loadingMesg

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun isCancelable(): Boolean = false

    companion object {
        private var loadingMesg = ""
        private const val TAG = "LoadingDialogFragment"

//        fun show(activity: Activity, fm: FragmentManager, message: String = "") {
//            if (ShowDialog.INSTANCE.openDialogOnce(activity, fm,TAG)) {
//                loadingMesg = message
//                LoadingDialogFragment().show(fm, TAG)
//            }
//        }

        fun show(currActivity: Activity, message: String = "") {
            val activity = currActivity as? FragmentActivity ?: return
            val fm = activity.supportFragmentManager
            if (ShowDialog.INSTANCE.openDialogOnce(activity, fm,TAG)) {
                loadingMesg = message
                LoadingDialogFragment().show(fm, TAG)
            }
        }

        fun dismiss(activity: Activity) {
            ShowDialog.INSTANCE.dismiss(activity,TAG)
        }
    }
}