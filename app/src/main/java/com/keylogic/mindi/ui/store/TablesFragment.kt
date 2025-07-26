package com.keylogic.mindi.ui.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.keylogic.mindi.R
import com.keylogic.mindi.adapters.StoreAdapter
import com.keylogic.mindi.dialogs.BuyStoreItemDialogFragment
import com.keylogic.mindi.enums.VIPStore
import com.keylogic.mindi.helper.VIPStoreHelper
import com.keylogic.mindi.ui.viewModel.TableViewModel
import com.keylogic.mindi.databinding.FragmentTablesBinding

class TablesFragment : Fragment() {
    private var _binding: FragmentTablesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TableViewModel by viewModels()
    private lateinit var tableAdapter: StoreAdapter
    private val currTab = VIPStore.TABLES

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTablesBinding.inflate(inflater, container, false)
        setupFragmentResultListener()

        tableAdapter = StoreAdapter(
            requireContext(),
            currTab.tabIndex,
            emptyList(),
            onItemClick = { position ->
                val tableList = viewModel.tables.value ?: return@StoreAdapter
                if (position !in tableList.indices) return@StoreAdapter

                if (tableList[position].purchaseEndDate == 0L) {
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
                    tableAdapter.notifyItemChanged(position)
                }
            }
        )

        binding.tableRecycler.adapter = tableAdapter
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.tableRecycler.layoutManager = layoutManager

        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(binding.tableRecycler)

        viewModel.tables.observe(viewLifecycleOwner) { tableList ->
            tableAdapter.updateList(tableList)
        }

        return binding.root
    }

    private fun setupFragmentResultListener() {
        childFragmentManager.setFragmentResultListener(
            currTab.tabName,
            viewLifecycleOwner
        ) { _, bundle ->
            val isPurchased = bundle.getBoolean(BuyStoreItemDialogFragment.KEY_IS_ITEM_PURCHASED, false)
            val index = bundle.getInt(BuyStoreItemDialogFragment.KEY_ITEM_INDEX, -1)
            if (isPurchased && index >= 0) {
                viewModel.updateTables()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
