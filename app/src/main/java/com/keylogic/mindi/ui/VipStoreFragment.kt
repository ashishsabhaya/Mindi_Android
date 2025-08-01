package com.keylogic.mindi.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.keylogic.mindi.adapters.ViewPagerAdapter
import com.keylogic.mindi.custom.SelectionConstraintLayout
import com.keylogic.mindi.custom.StrokeTextView
import com.keylogic.mindi.enums.DeviceType
import com.keylogic.mindi.enums.VIPStore
import com.keylogic.mindi.helper.CommonHelper
import com.keylogic.mindi.R
import com.keylogic.mindi.ui.store.AvatarFragment
import com.keylogic.mindi.ui.store.BackgroundsFragment
import com.keylogic.mindi.ui.store.CardsFragment
import com.keylogic.mindi.ui.store.TablesFragment
import com.keylogic.mindi.databinding.FragmentVipStoreBinding
import com.keylogic.mindi.ui.viewModel.VipStoreViewModel

class VipStoreFragment : Fragment() {
    private var _binding: FragmentVipStoreBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VipStoreViewModel by activityViewModels()
    private lateinit var adapter: ViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVipStoreBinding.inflate(inflater, container, false)

        setupUI()
        observeViewModel()

        return binding.root
    }

    private fun setupUI() {
        CommonHelper.INSTANCE.setScaleOnTouch(binding.topTitleInclude.cancelCons) {
            findNavController().popBackStack()
        }

        CommonHelper.INSTANCE.setScaleOnTouch(binding.topTitleInclude.chipCons) {
            if (findNavController().currentDestination?.id == R.id.vipStoreFragment) {
                findNavController().navigate(R.id.chipStoreFragment)
            }
        }

        binding.topTitleInclude.titleTxt.text = getString(R.string.vip_store)

        adapter = ViewPagerAdapter(requireActivity()).apply {
            addFragment(VIPStore.AVATAR.tabName, AvatarFragment())
            addFragment(VIPStore.CARDS.tabName, CardsFragment())
            addFragment(VIPStore.TABLES.tabName, TablesFragment())
            addFragment(VIPStore.BACKGROUNDS.tabName, BackgroundsFragment())
        }

        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val tabView = LayoutInflater.from(requireContext()).inflate(R.layout.tab_item_layout, null)
            val tabNameTxt = tabView.findViewById<StrokeTextView>(R.id.tab_item_name_txt)
            tabNameTxt.text = adapter.getItemName(position)

            val heightRes = if (CommonHelper.deviceType == DeviceType.NORMAL)
                com.intuit.sdp.R.dimen._30sdp else com.intuit.sdp.R.dimen._24sdp
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                resources.getDimensionPixelSize(heightRes)
            )
            tabView.layoutParams = layoutParams
            tab.customView = tabView
        }.attach()

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.onTabSelected(position)
            }
        })

        binding.tabLayout.tabRippleColor = null
        binding.tabLayout.setSelectedTabIndicatorColor(Color.TRANSPARENT)
        binding.tabLayout.isTabIndicatorFullWidth = true

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let { binding.viewPager.setCurrentItem(it.position, false) }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun observeViewModel() {
        viewModel.chipCount.observe(viewLifecycleOwner) { count ->
            binding.topTitleInclude.chipCountTxt.text = CommonHelper.INSTANCE.getChip(count)
        }

        viewModel.selectedTab.observe(viewLifecycleOwner) { selectedIndex ->
            for (i in 0 until binding.tabLayout.tabCount) {
                val tabBgCons = binding.tabLayout.getTabAt(i)
                    ?.customView?.findViewById<SelectionConstraintLayout>(R.id.tab_item_bg_con)
                tabBgCons?.updateSelection(i == selectedIndex)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}