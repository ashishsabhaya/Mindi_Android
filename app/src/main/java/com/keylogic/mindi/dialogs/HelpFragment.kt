package com.keylogic.mindi.dialogs

import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.keylogic.mindi.R
import com.keylogic.mindi.adapters.HelpAdapter
import com.keylogic.mindi.databinding.DialogFragmentHelpBinding
import com.keylogic.mindi.enums.GameOptions
import com.keylogic.mindi.helper.CommonHelper

class HelpFragment : BaseFullDialogFragment() {
    private var _binding: DialogFragmentHelpBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: HelpAdapter

    override fun getContentView(inflater: LayoutInflater): View {
        _binding = DialogFragmentHelpBinding.inflate(inflater)
        setupUI()
        return binding.root
    }

    private fun setupUI() {
        binding.topTitleInclude.chipCons.visibility = View.GONE
        binding.topTitleInclude.titleTxt.text = GameOptions.HELP.oName

        CommonHelper.Companion.INSTANCE.setScaleOnTouch(binding.topTitleInclude.cancelCons) {
            findNavController().popBackStack()
        }

        val list = ArrayList<String>()
        generateTableInformation(list)

        adapter = HelpAdapter(list)
        binding.helpRecycler.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        binding.helpRecycler.adapter = adapter
    }

    private fun generateTableInformation(list: ArrayList<String>) {
        for (i in 1 until 8) {
            val msg = "help_msg$i"
            val resId = resources.getIdentifier(msg, "string", requireContext().packageName)
            list.add(getMsgString(resId))
        }
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setWindowAnimations(R.style.DialogRightInAnimationEntryExit)
    }

    fun getMsgString(id: Int): String {
        return requireContext().getString(id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
