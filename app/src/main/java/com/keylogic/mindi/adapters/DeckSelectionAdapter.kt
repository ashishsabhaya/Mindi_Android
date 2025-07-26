package com.keylogic.mindi.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.keylogic.mindi.enums.DeckType
import com.keylogic.mindi.helper.CommonHelper
import com.keylogic.mindi.R

class DeckSelectionAdapter(
    private val gameModes: List<DeckType>,
    private val onItemClick: (DeckType) -> Unit
) : RecyclerView.Adapter<DeckSelectionAdapter.GameModeViewHolder>() {

    class GameModeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val modeImg: ImageView = itemView.findViewById(R.id.deck_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameModeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.deck_type_layout, parent, false)
        return GameModeViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameModeViewHolder, position: Int) {
        val gameMode = gameModes[position]
        holder.modeImg.setImageResource(gameMode.resource)

        CommonHelper.INSTANCE.setScaleOnTouch(holder.itemView, onclick = {
            onItemClick(gameMode)
        })
    }

    override fun getItemCount(): Int = gameModes.size
}
