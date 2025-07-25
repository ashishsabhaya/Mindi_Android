package com.keylogic.mindi.Ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.keylogic.mindi.Adapters.ChipStorePlanAdapter
import com.keylogic.mindi.Helper.CommonHelper
import com.keylogic.mindi.R
import com.keylogic.mindi.ViewModel.ChipStoreViewModel
import com.keylogic.mindi.databinding.FragmentChipStoreBinding

class ChipStoreFragment : Fragment() {
    private var _binding: FragmentChipStoreBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChipStoreViewModel by viewModels()
    private lateinit var adapter: ChipStorePlanAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChipStoreBinding.inflate(inflater, container, false)

        setupUI()
        observeViewModel()

        return binding.root
    }

    private fun setupUI() {
        binding.topTitleInclude.chipCons.visibility = View.GONE
        binding.topTitleInclude.titleTxt.text = getString(R.string.chip_store)

        CommonHelper.INSTANCE.setScaleOnTouch(binding.topTitleInclude.cancelCons) {
            findNavController().popBackStack()
        }

        adapter = ChipStorePlanAdapter(requireContext(), emptyList()) { plan ->
            // Handle plan click
        }

        binding.chipPlanRecycler.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.chipPlanRecycler.adapter = adapter
        binding.chipPlanRecycler.suppressLayout(true)
    }

    private fun observeViewModel() {
        viewModel.chipPlans.observe(viewLifecycleOwner, Observer { plans ->
            adapter.updateData(plans)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
