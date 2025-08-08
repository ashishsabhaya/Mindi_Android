package com.keylogic.mindi.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.keylogic.mindi.helper.ProfileHelper
import com.keylogic.mindi.R
import com.keylogic.mindi.custom.StrokeTextView
import com.keylogic.mindi.helper.CommonHelper
import com.keylogic.mindi.models.ResultProfile

class ResultProfileAdapter(
    private val context: Context,
    private val list: List<ResultProfile>
) : RecyclerView.Adapter<ResultProfileAdapter.ProfileViewHolder>() {

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImg: ImageView = itemView.findViewById(R.id.rp_profile_img)
        val profileCard: MaterialCardView = itemView.findViewById(R.id.rp_profile_card)
        val nameTxt: StrokeTextView = itemView.findViewById(R.id.rp_profile_name_txt)
        val chipCountTxt: StrokeTextView = itemView.findViewById(R.id.rp_chip_count_txt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.result_profile_item_layout, parent, false)
        return ProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val currProfile = list[position]

        val resource = ProfileHelper.INSTANCE.getProfileResource(context, currProfile.profileImgId)
        holder.profileImg.setImageResource(resource)
        holder.nameTxt.text = currProfile.name
        val preFix = if (currProfile.isWinner) "+" else "-"
        val chipAmount = CommonHelper.INSTANCE.getChip(currProfile.betAmound)
        holder.chipCountTxt.text = preFix + chipAmount

        val color = if (currProfile.isWinner)
            context.getColor(R.color.green_team)
        else
            context.getColor(R.color.red_team)
        holder.profileCard.setStrokeColor(color)

    }

    override fun getItemCount(): Int = list.size
}
