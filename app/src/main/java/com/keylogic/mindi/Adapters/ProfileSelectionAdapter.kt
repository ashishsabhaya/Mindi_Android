package com.keylogic.mindi.Adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.keylogic.mindi.Helper.ProfileHelper
import com.keylogic.mindi.R

class ProfileSelectionAdapter(
    private val context: Context,
    private val totalProfile: Int,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<ProfileSelectionAdapter.ProfileViewHolder>() {
    var selectedProfileIndex = 0

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImg: ImageView = itemView.findViewById(R.id.profile_item_img)
        val itemCard: MaterialCardView = itemView.findViewById(R.id.profile_item_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.profile_item_layout, parent, false)
        return ProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val defaultProfileName = ProfileHelper.INSTANCE.getDefaultProfilePrefix() + position
        val resource = ProfileHelper.INSTANCE.getProfileImageByName(context, defaultProfileName)
        holder.itemImg.setImageResource(resource)

        updateSelection(holder.itemCard, position)

        holder.itemView.setOnClickListener {
            notifyItemChanged(selectedProfileIndex)
            selectedProfileIndex = position
            updateSelection(holder.itemCard, position)
            onItemClick(position)
        }
    }

    private fun updateSelection(itemCard: MaterialCardView, position: Int) {
        if (position == selectedProfileIndex)
            itemCard.strokeColor = context.resources.getColor(R.color.selected_profile_stroke,null)
        else
            itemCard.strokeColor = context.resources.getColor(R.color.white,null)
    }

    override fun getItemCount(): Int = totalProfile
}
