package com.keylogic.mindi.Adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.keylogic.mindi.Custom.IconTextConstraintLayout
import com.keylogic.mindi.Enum.VIPStore
import com.keylogic.mindi.Helper.CommonHelper
import com.keylogic.mindi.Helper.VIPStoreHelper
import com.keylogic.mindi.Models.StoreItem
import com.keylogic.mindi.R

class StoreAdapter(
    private val context: Context,
    private val tabIndex: Int,
    private var itemList: List<StoreItem>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<StoreAdapter.StoreItemViewHolder>() {
    var selectedIndex = 0

    class StoreItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImg: ImageView = itemView.findViewById(R.id.vip_store_item_img)
        val itemInfoCons: IconTextConstraintLayout = itemView.findViewById(R.id.vip_store_item_info_cons)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(
                when (tabIndex) {
                    VIPStore.CARDS.tabIndex -> R.layout.card_back_item_layout
                    VIPStore.TABLES.tabIndex -> R.layout.table_item_layout
                    VIPStore.BACKGROUNDS.tabIndex -> R.layout.background_item_layout
                    else -> R.layout.avatar_item_layout
                }
                , parent, false)
        return StoreItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoreItemViewHolder, position: Int) {
        val currItem = itemList[position]

        val index = 0//position
        val resourceName = when(tabIndex) {
            VIPStore.CARDS.tabIndex -> {
                VIPStoreHelper.INSTANCE.getCardBackPreFix() + index
            }
            VIPStore.TABLES.tabIndex -> {
                VIPStoreHelper.INSTANCE.getTablePreFix() + index
            }
            VIPStore.BACKGROUNDS.tabIndex -> {
                VIPStoreHelper.INSTANCE.getBackgroundPreFix() + index
            }
            else -> {
                VIPStoreHelper.INSTANCE.getAvatarPreFix() + index
            }
        }

        val resource = VIPStoreHelper.INSTANCE.getResourceByName(context, resourceName)
        holder.itemImg.setImageResource(resource)

        val infoIcon = if (currItem.isSelected)
            R.drawable.ic_selected
        else if (currItem.purchaseDate != 0L)
            R.drawable.ic_clock
        else
            R.drawable.ic_chip

        val infoTxt = if (currItem.isSelected)
            CommonHelper.INSTANCE.getItemSelected()
        else if (currItem.purchaseEndDate != 0L)
            CommonHelper.INSTANCE.getLeftTime(currItem.purchaseEndDate)
        else
            CommonHelper.INSTANCE.getChip(currItem.price)

        holder.itemInfoCons.setViewTextAndResource(infoTxt, infoIcon)

        CommonHelper.INSTANCE.setScaleOnTouch(holder.itemInfoCons, onclick = {
            notifyItemChanged(selectedIndex)
            selectedIndex = position
            onItemClick(position)
        })
    }

    fun updateList(newList: List<StoreItem>) {
        this.itemList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = itemList.size
}
