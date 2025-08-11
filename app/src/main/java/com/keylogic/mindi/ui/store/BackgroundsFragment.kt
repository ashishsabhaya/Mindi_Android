package com.keylogic.mindi.ui.store

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.keylogic.mindi.R
import com.keylogic.mindi.adapters.StoreAdapter
import com.keylogic.mindi.enums.VIPStore
import com.keylogic.mindi.helper.VIPStoreHelper
import com.keylogic.mindi.ui.viewModel.BackgroundViewModel
import com.keylogic.mindi.databinding.FragmentBackgroundsBinding
import com.keylogic.mindi.dialogs.BuyStoreItemDialogFragment
import com.keylogic.mindi.ui.viewModel.VipStoreViewModel
import kotlin.getValue


class BackgroundsFragment : Fragment() {
    private var _binding: FragmentBackgroundsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BackgroundViewModel by viewModels()
    private lateinit var backgroundAdapter: StoreAdapter
    private val currTab = VIPStore.BACKGROUNDS
    private val vipStoreViewModel: VipStoreViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBackgroundsBinding.inflate(inflater, container, false)

        setupFragmentResultListener()


        backgroundAdapter = StoreAdapter(
            requireContext(),
            currTab.tabIndex,
            emptyList(),
            onItemClick = { position ->
                val backgroundList = viewModel.backgrounds.value ?: return@StoreAdapter
                if (position !in backgroundList.indices) return@StoreAdapter

                if (backgroundList[position].purchaseEndDate == 0L) {
                    if (findNavController().currentDestination?.id == R.id.vipStoreFragment) {
                        val bundle = Bundle().apply {
                            putInt(BuyStoreItemDialogFragment.KEY_TAB_INDEX, currTab.tabIndex)
                            putInt(BuyStoreItemDialogFragment.KEY_ITEM_INDEX, position)
                        }
                        findNavController().navigate(R.id.buyStoreItemDialogFragment, bundle)
                    }
                } else {
                    VIPStoreHelper.INSTANCE.buyOrSelectStoreItem(
                        context = requireContext(),
                        isBuyItem = false,
                        tabIndex = currTab.tabIndex,
                        itemIndex = position,
                        forWeek = false
                    )
                    backgroundAdapter.notifyItemChanged(position)
                }
            }
        )

        binding.backgroundRecycler.adapter = backgroundAdapter
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.backgroundRecycler.layoutManager = layoutManager

        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(binding.backgroundRecycler)

        viewModel.backgrounds.observe(viewLifecycleOwner) { backgroundList ->
            backgroundAdapter.updateList(backgroundList)
        }

        return binding.root
    }

    private fun setupFragmentResultListener() {
        requireActivity().supportFragmentManager.setFragmentResultListener(
            currTab.tabName,
            viewLifecycleOwner
        ) { _, bundle ->
            val isPurchased = bundle.getBoolean(BuyStoreItemDialogFragment.KEY_IS_ITEM_PURCHASED, false)
            val index = bundle.getInt(BuyStoreItemDialogFragment.KEY_ITEM_INDEX, -1)
            if (isPurchased && index >= 0) {
                viewModel.updateBackgrounds()
                vipStoreViewModel.refreshChipCount()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
