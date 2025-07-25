package com.keylogic.mindi.Ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.keylogic.mindi.Adapters.ViewPagerAdapter
import com.keylogic.mindi.Custom.SelectionConstraintLayout
import com.keylogic.mindi.Custom.StrokeTextView
import com.keylogic.mindi.Enum.DeviceType
import com.keylogic.mindi.Enum.VIPStore
import com.keylogic.mindi.Helper.CommonHelper
import com.keylogic.mindi.R
import com.keylogic.mindi.Ui.Store.AvatarFragment
import com.keylogic.mindi.Ui.Store.BackgroundsFragment
import com.keylogic.mindi.Ui.Store.CardsFragment
import com.keylogic.mindi.Ui.Store.TablesFragment
import com.keylogic.mindi.databinding.FragmentVipStoreBinding

class VipStoreFragment : Fragment() {
    private var _binding: FragmentVipStoreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVipStoreBinding.inflate(layoutInflater, container,false)

        CommonHelper.INSTANCE.setScaleOnTouch(binding.topTitleInclude.cancelCons, onclick = {
            findNavController().popBackStack()
        })

        CommonHelper.INSTANCE.setScaleOnTouch(binding.topTitleInclude.chipCons, onclick = {
            findNavController().navigate(R.id.action_vipStoreFragment_to_chipStoreFragment)
        })

        binding.topTitleInclude.titleTxt.setText(resources.getString(R.string.vip_store))
        binding.topTitleInclude.chipCountTxt.setText(CommonHelper.INSTANCE.getTotalChip())

        val adapter = ViewPagerAdapter(requireActivity())
        adapter.addFragment(resources.getString(VIPStore.AVATAR.tabName),AvatarFragment())
        adapter.addFragment(resources.getString(VIPStore.CARDS.tabName),CardsFragment())
        adapter.addFragment(resources.getString(VIPStore.TABLES.tabName),TablesFragment())
        adapter.addFragment(resources.getString(VIPStore.BACKGROUNDS.tabName),BackgroundsFragment())

        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val tabView =
                LayoutInflater.from(requireContext()).inflate(R.layout.tab_item_layout, null)
            val tabNameTxt = tabView.findViewById<StrokeTextView>(R.id.tab_item_name_txt)
            tabNameTxt.text = adapter.getItemName(position)
            val dimen =
                if (CommonHelper.deviceType == DeviceType.NORMAL) com.intuit.sdp.R.dimen._30sdp
                else com.intuit.sdp.R.dimen._24sdp
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                resources.getDimensionPixelSize(dimen)
            )
            tabView.layoutParams = layoutParams
            tab.customView = tabView
        }.attach()

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                for (i in 0 until binding.tabLayout.tabCount) {
                    val tabBgCons = binding.tabLayout.getTabAt(i)?.customView?.findViewById<SelectionConstraintLayout>(R.id.tab_item_bg_con)
                    tabBgCons?.updateSelection(i == position)
                }
            }
        })

        binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0), true)
        binding.tabLayout.tabRippleColor = null
        binding.tabLayout.setSelectedTabIndicatorColor(Color.TRANSPARENT)
        binding.tabLayout.isTabIndicatorFullWidth = true

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null)
                    binding.viewPager.setCurrentItem(tab.position, false)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}