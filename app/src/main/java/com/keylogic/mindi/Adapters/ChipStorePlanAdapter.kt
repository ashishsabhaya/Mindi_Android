package com.keylogic.mindi.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.keylogic.mindi.Custom.BackgroundConstraintLayout
import com.keylogic.mindi.Custom.StrokeTextView
import com.keylogic.mindi.Helper.CommonHelper
import com.keylogic.mindi.Models.ChipPlan
import com.keylogic.mindi.R

class ChipStorePlanAdapter(
    private val context: Context,
    private var chipPlanList: List<ChipPlan>,
    private val onItemClick: (ChipPlan) -> Unit
) : RecyclerView.Adapter<ChipStorePlanAdapter.PlanViewHolder>() {

    class PlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bgImg: ImageView = itemView.findViewById(R.id.chip_store_bg_img)
        val chipImg: ImageView = itemView.findViewById(R.id.chip_img)
        val restoreImg: ImageView = itemView.findViewById(R.id.chip_restore_img)
        val chipAmountLinear: LinearLayout = itemView.findViewById(R.id.chip_store_total_amount_linear)
        val removeAdsTxt: StrokeTextView = itemView.findViewById(R.id.chip_store_remove_ads_txt)
        val chipAmoundTxt: StrokeTextView = itemView.findViewById(R.id.total_chip_txt)
        val priceTxt: StrokeTextView = itemView.findViewById(R.id.chip_price_btn_txt)
        val buyBtn: BackgroundConstraintLayout = itemView.findViewById(R.id.chip_price_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chip_store_item_layout, parent, false)
        return PlanViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val plan = chipPlanList[position]

        val resource = when(plan.planIndex) {
            1 -> R.drawable.chip_store_plan2
            2 -> R.drawable.chip_store_plan3
            else -> R.drawable.chip_store_plan1
        }
        holder.chipImg.setImageResource(resource)

        if (plan.isRestore) {
            holder.restoreImg.visibility = View.VISIBLE
            holder.chipAmountLinear.visibility = View.GONE
        }
        else {
            holder.restoreImg.visibility = View.GONE
            holder.chipAmountLinear.visibility = View.VISIBLE
        }

        if (plan.isRemoveAds) {
            holder.bgImg.setImageResource(R.drawable.chip_store_item_bg1)
            holder.removeAdsTxt.visibility = View.VISIBLE
        }
        else {
            holder.bgImg.setImageResource(R.drawable.chip_store_item_bg)
            holder.removeAdsTxt.visibility = View.GONE
        }

        holder.chipAmoundTxt.text = CommonHelper.INSTANCE.getChip(plan.totalChips)
        holder.priceTxt.text = if (plan.isRestore)
            context.resources.getString(R.string.restore)
        else
            plan.planPrice

        CommonHelper.INSTANCE.setScaleOnTouch(holder.buyBtn, onclick = {
            onItemClick(plan)
        })
    }

    fun updateData(newItems: List<ChipPlan>) {
        chipPlanList = newItems
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = chipPlanList.size
}
