package com.keylogic.mindi.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.keylogic.mindi.enums.GameMode
import com.keylogic.mindi.helper.CommonHelper
import com.keylogic.mindi.R

class GameModeAdapter(
    private val gameModes: List<GameMode>,
    private val onItemClick: (GameMode) -> Unit
) : RecyclerView.Adapter<GameModeAdapter.GameModeViewHolder>() {

    class GameModeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val modeImg: ImageView = itemView.findViewById(R.id.mode_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameModeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.game_mode_layout, parent, false)
        return GameModeViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameModeViewHolder, position: Int) {
        val gameMode = gameModes[position]
        holder.modeImg.setImageResource(gameMode.resource)
        holder.itemView.scaleX = 1f
        holder.itemView.scaleY = 1f

        CommonHelper.INSTANCE.setScaleOnTouch(holder.itemView, onclick = {
            onItemClick(gameMode)
        })
    }

    override fun getItemCount(): Int = gameModes.size
}
