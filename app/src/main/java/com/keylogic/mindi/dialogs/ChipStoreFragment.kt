package com.keylogic.mindi.dialogs

import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.keylogic.mindi.R
import com.keylogic.mindi.adapters.ChipStorePlanAdapter
import com.keylogic.mindi.databinding.DialogFragmentChipStoreBinding
import com.keylogic.mindi.helper.ChipStoreHelper
import com.keylogic.mindi.helper.CommonHelper

class ChipStoreFragment : BaseFullDialogFragment() {
    private var _binding: DialogFragmentChipStoreBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ChipStorePlanAdapter

    override fun getContentView(inflater: LayoutInflater): View {
        _binding = DialogFragmentChipStoreBinding.inflate(inflater)
        setupUI()
        return binding.root
    }

    private fun setupUI() {
        binding.topTitleInclude.chipCons.visibility = View.GONE
        binding.topTitleInclude.titleTxt.text = getString(R.string.chip_store)

        CommonHelper.Companion.INSTANCE.setScaleOnTouch(binding.topTitleInclude.cancelCons) {
            findNavController().popBackStack()
        }

        adapter = ChipStorePlanAdapter(requireContext(), ChipStoreHelper.chipPlanList) { plan ->
            // Handle plan click
        }

        binding.chipPlanRecycler.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.chipPlanRecycler.adapter = adapter
        binding.chipPlanRecycler.suppressLayout(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
