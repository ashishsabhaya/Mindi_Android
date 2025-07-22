package com.keylogic.mindi.Ui.Store

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.keylogic.mindi.Adapters.BackgroundAdapter
import com.keylogic.mindi.Dialogs.BuyStoreItemDialogFragment
import com.keylogic.mindi.Enum.VIPStore
import com.keylogic.mindi.Helper.VIPStoreHelper
import com.keylogic.mindi.R
import com.keylogic.mindi.Ui.ViewModel.BackgroundViewModel
import com.keylogic.mindi.databinding.FragmentBackgroundsBinding


class BackgroundsFragment : Fragment() {
    private var _binding: FragmentBackgroundsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BackgroundViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBackgroundsBinding.inflate(inflater, container, false)

        viewModel.backgrounds.observe(viewLifecycleOwner) { backgroundList ->
            val adapter = BackgroundAdapter(requireContext(), backgroundList, onItemClick = { position ->
                BuyStoreItemDialogFragment.show(requireActivity(), VIPStore.BACKGROUNDS.tabIndex, position)
            })
            binding.backgroundRecycler.adapter = adapter
            val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.backgroundRecycler.layoutManager = layoutManager

            val pagerSnapHelper = PagerSnapHelper()
            pagerSnapHelper.attachToRecyclerView(binding.backgroundRecycler)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
