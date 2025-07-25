package com.keylogic.mindi.Ui.Store

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.keylogic.mindi.Adapters.StoreAdapter
import com.keylogic.mindi.Dialogs.BuyStoreItemDialogFragment
import com.keylogic.mindi.Enum.VIPStore
import com.keylogic.mindi.Helper.VIPStoreHelper
import com.keylogic.mindi.Ui.ViewModel.AvatarViewModel
import com.keylogic.mindi.databinding.FragmentAvatarBinding

class AvatarFragment : Fragment() {
    private var _binding: FragmentAvatarBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AvatarViewModel by viewModels()
    private lateinit var avatarAdapter: StoreAdapter
    private val currTab = VIPStore.AVATAR

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAvatarBinding.inflate(inflater, container, false)

        setupFragmentResultListener()

        avatarAdapter = StoreAdapter(
            requireContext(),
            currTab.tabIndex,
            emptyList(),
            onItemClick = { position ->
                val avatarList = viewModel.avatars.value ?: return@StoreAdapter
                if (position !in avatarList.indices) return@StoreAdapter

                if (avatarList[position].purchaseEndDate == 0L) {
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
                    avatarAdapter.notifyItemChanged(position)
                }
            }
        )

        binding.avatarRecycler.adapter = avatarAdapter
        binding.avatarRecycler.layoutManager = GridLayoutManager(requireContext(), 5)

        viewModel.avatars.observe(viewLifecycleOwner) { avatarList ->
            avatarAdapter.updateList(avatarList)
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
                viewModel.updateAvatars()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
