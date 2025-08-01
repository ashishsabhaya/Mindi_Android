package com.keylogic.mindi.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.keylogic.mindi.custom.BackgroundConstraintLayout
import com.keylogic.mindi.custom.StrokeTextView
import com.keylogic.mindi.helper.CommonHelper
import com.keylogic.mindi.models.ChipPlan
import com.keylogic.mindi.R
import com.keylogic.mindi.custom.SelectionConstraintLayout
import com.keylogic.mindi.helper.DailyRewardHelper
import com.keylogic.mindi.models.DailyReward

class DailyRewardPlanAdapter(
    private val context: Context,
    private var list: List<DailyReward>
) : RecyclerView.Adapter<DailyRewardPlanAdapter.PlanViewHolder>() {

    class PlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val topBgImg: ImageView = itemView.findViewById(R.id.reward_top_img)
        val topFgImg: ImageView = itemView.findViewById(R.id.reward_top_fg_img)
        val titleTxt: StrokeTextView = itemView.findViewById(R.id.reward_title_txt)
        val centerBgImg: ImageView = itemView.findViewById(R.id.reward_center_bg_img)
        val centerRewardImg: ImageView = itemView.findViewById(R.id.reward_chip_img)
        val mainBgCard: MaterialCardView = itemView.findViewById(R.id.reward_bg_card)
        val mainCenterCard: MaterialCardView = itemView.findViewById(R.id.reward_center_card)
        val mainFgCard: MaterialCardView = itemView.findViewById(R.id.reward_fg_card)
        val bottomCard: SelectionConstraintLayout = itemView.findViewById(R.id.reward_bottom_cons)
        val chipCountTxt: StrokeTextView = itemView.findViewById(R.id.reward_chip_count_txt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.daily_reward_item_layout, parent, false)
        return PlanViewHolder(view)
    }

    @SuppressLint("DiscouragedApi")
    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val reward = list[position]

        val chipResName = "reward_day_${position+1}"
        val chipRes = context.resources.getIdentifier(chipResName, "drawable", context.packageName)
        holder.centerRewardImg.setImageResource(chipRes)

        holder.chipCountTxt.text = CommonHelper.INSTANCE.formatChip(reward.chipCount)

        holder.mainFgCard.visibility = View.GONE
        holder.topFgImg.visibility = View.GONE
        holder.topBgImg.alpha = 1f
        holder.mainCenterCard.alpha = 1f
        if (reward.isCurrentDay) {
            holder.topBgImg.setImageResource(R.drawable.curr_reward_top_bg)
            holder.titleTxt.text = context.resources.getString(R.string.today)
            holder.centerBgImg.setImageResource(R.drawable.rc_bg)
            holder.mainBgCard.setCardBackgroundColor(context.getColor(R.color.rc_bottom))
            holder.mainCenterCard.strokeColor = context.getColor(R.color.rc_border)
            holder.bottomCard.updateRewardCollection(isCurrDay = true)
        }
        else if (reward.isCollected) {
            holder.topBgImg.setImageResource(R.drawable.next_reward_top_bg)
            holder.titleTxt.text = context.resources.getString(R.string.collected)
            holder.centerBgImg.setImageResource(R.drawable.rnc_bg)
            holder.mainBgCard.setCardBackgroundColor(context.getColor(R.color.rnc_bottom))
            holder.mainCenterCard.strokeColor = context.getColor(R.color.rnc_border)
            holder.bottomCard.updateRewardCollection(isNotCurrDay = true)
            holder.topBgImg.alpha = 0.75f
            holder.mainCenterCard.alpha = 0.75f
            holder.mainFgCard.visibility = View.VISIBLE
            holder.topFgImg.visibility = View.VISIBLE
        }
        else {
            val titleTxt = if (DailyRewardHelper.currDay == position)
                context.resources.getString(R.string.tomorrow)
            else
                context.resources.getString(R.string.day) + " ${position+1}"
            holder.topBgImg.setImageResource(R.drawable.next_reward_top_bg)
            holder.titleTxt.text = titleTxt
            holder.centerBgImg.setImageResource(R.drawable.rnc_bg)
            holder.mainBgCard.setCardBackgroundColor(context.getColor(R.color.rnc_bottom))
            holder.mainCenterCard.strokeColor = context.getColor(R.color.rnc_border)
            holder.bottomCard.updateRewardCollection(isNotCurrDay = true)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<DailyReward>) {
        list = newItems
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size
}
