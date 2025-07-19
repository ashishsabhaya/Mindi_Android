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
import com.keylogic.mindi.Helper.CommonHelper
import com.keylogic.mindi.Helper.ProfileHelper
import com.keylogic.mindi.Helper.VIPStoreHelper
import com.keylogic.mindi.Models.Avatar
import com.keylogic.mindi.R

class AvatarAdapter(
    private val context: Context,
    private val avatarList: ArrayList<Avatar>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder>() {
    var selectedProfileIndex = 0

    class AvatarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImg: ImageView? = itemView.findViewById(R.id.vip_store_item_img)
        val itemInfoCons: IconTextConstraintLayout? = itemView.findViewById(R.id.vip_store_item_info_cons)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.avatar_item_layout, parent, false)
        return AvatarViewHolder(view)
    }

    override fun onBindViewHolder(holder: AvatarViewHolder, position: Int) {
        val currItem = avatarList[position]

        val resourceName = VIPStoreHelper.INSTANCE.getAvatarPreFix() + position
        //--> temp
        val resourceName1 = ProfileHelper.INSTANCE.getDefaultProfilePrefix() + position
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

    override fun getItemCount(): Int = avatarList.size
}
