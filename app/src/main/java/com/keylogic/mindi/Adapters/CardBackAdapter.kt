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
import com.keylogic.mindi.Models.CardBack
import com.keylogic.mindi.R

class CardBackAdapter(
    private val context: Context,
    private val cardBackList: List<CardBack>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<CardBackAdapter.CardBackViewHolder>() {
    var selectedProfileIndex = 0

    class CardBackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImg: ImageView? = itemView.findViewById(R.id.vip_store_item_img)
        val itemInfoCons: IconTextConstraintLayout? = itemView.findViewById(R.id.vip_store_item_info_cons)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardBackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_back_item_layout, parent, false)
        return CardBackViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardBackViewHolder, position: Int) {
        val currItem = cardBackList[position]

        val resourceName = VIPStoreHelper.INSTANCE.getCardBackPreFix() + position
        val resource = VIPStoreHelper.INSTANCE.getResourceByName(context, resourceName)
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

    override fun getItemCount(): Int = cardBackList.size
}
