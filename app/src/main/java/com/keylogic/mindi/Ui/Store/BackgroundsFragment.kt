package com.keylogic.mindi.Ui.Store

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.keylogic.mindi.Adapters.StoreAdapter
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
    private lateinit var backgroundAdapter: StoreAdapter
    private val currTab = VIPStore.BACKGROUNDS

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
                    BuyStoreItemDialogFragment.show(
                        requireActivity(),
                        childFragmentManager,
                        currTab.tabIndex,
                        position
                    )
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
        childFragmentManager.setFragmentResultListener(
            resources.getString(currTab.tabName),
            viewLifecycleOwner
        ) { _, bundle ->
            val isPurchased = bundle.getBoolean(BuyStoreItemDialogFragment.KEY_IS_ITEM_PURCHASED, false)
            val index = bundle.getInt(BuyStoreItemDialogFragment.KEY_ITEM_INDEX, -1)
            if (isPurchased && index >= 0) {
                viewModel.updateBackgrounds()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
