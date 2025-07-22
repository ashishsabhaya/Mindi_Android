package com.keylogic.mindi.Ui.Store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import com.keylogic.mindi.Adapters.TableAdapter
import com.keylogic.mindi.Dialogs.BuyStoreItemDialogFragment
import com.keylogic.mindi.Enum.VIPStore
import com.keylogic.mindi.Helper.VIPStoreHelper
import com.keylogic.mindi.Ui.ViewModel.TableViewModel
import com.keylogic.mindi.databinding.FragmentTablesBinding

class TablesFragment : Fragment() {
    private var _binding: FragmentTablesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TableViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTablesBinding.inflate(inflater, container, false)

        viewModel.tables.observe(viewLifecycleOwner) { tableList ->
            val adapter = TableAdapter(requireContext(), tableList, onItemClick = { position ->
                BuyStoreItemDialogFragment.show(requireActivity(), VIPStore.TABLES.tabIndex, position)
            })
            binding.tableRecycler.adapter = adapter
            val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.tableRecycler.layoutManager = layoutManager

            val pagerSnapHelper = PagerSnapHelper()
            pagerSnapHelper.attachToRecyclerView(binding.tableRecycler)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
