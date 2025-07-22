package com.keylogic.mindi.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.keylogic.mindi.Custom.IconTextConstraintLayout
import com.keylogic.mindi.Helper.CommonHelper
import com.keylogic.mindi.Helper.VIPStoreHelper
import com.keylogic.mindi.Models.Background
import com.keylogic.mindi.R

class BackgroundAdapter(
    private val context: Context,
    private val backgroundList: List<Background>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<BackgroundAdapter.BackgroundViewHolder>() {
    var selectedProfileIndex = 0

    class BackgroundViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImg: ImageView? = itemView.findViewById(R.id.vip_store_item_img)
        val itemInfoCons: IconTextConstraintLayout? = itemView.findViewById(R.id.vip_store_item_info_cons)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackgroundViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.background_item_layout, parent, false)
        return BackgroundViewHolder(view)
    }

    override fun onBindViewHolder(holder: BackgroundViewHolder, position: Int) {
        val currItem = backgroundList[position]

        val resourceName = VIPStoreHelper.INSTANCE.getBackgroundPreFix() + position
        //--> temp
        val resourceName1 = VIPStoreHelper.INSTANCE.getBackgroundPreFix() + 1
        val resource = VIPStoreHelper.INSTANCE.getResourceByName(context, resourceName1)
        holder.itemImg?.setImageResource(resource)

        val infoIcon = if (currItem.purchaseDate != 0L)
            R.drawable.ic_clock
        else if (currItem.isSelected)
            R.drawable.ic_selected
        else
            R.drawable.ic_chip

        val infoTxt = if (currItem.purchaseDate != 0L)
            CommonHelper.INSTANCE.getLeftTime(currItem.purchaseDate)
        else if (currItem.isSelected)
            CommonHelper.INSTANCE.getItemSelected()
        else
            CommonHelper.INSTANCE.getChip(currItem.price)

        holder.itemInfoCons?.setViewTextAndResource(infoTxt, infoIcon)

        holder.itemInfoCons?.setOnClickListener {
            notifyItemChanged(selectedProfileIndex)
            selectedProfileIndex = position
            onItemClick(position)
        }
    }

    override fun getItemCount(): Int = backgroundList.size
}
