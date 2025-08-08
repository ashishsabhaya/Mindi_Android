package com.keylogic.mindi.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.keylogic.mindi.custom.StrokeTextView
import com.keylogic.mindi.helper.CommonHelper
import com.keylogic.mindi.R
import com.keylogic.mindi.enums.GameOptions

class GameOptionAdapter(
    private var list: List<GameOptions>,
    private val onItemClick: (GameOptions) -> Unit
) : RecyclerView.Adapter<GameOptionAdapter.GameOptionHolder>() {

    class GameOptionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconImg: ImageView = itemView.findViewById(R.id.go_option_icon_img)
        val nameTxt: StrokeTextView = itemView.findViewById(R.id.go_option_name_txt)
        val optionCons: ConstraintLayout = itemView.findViewById(R.id.go_center_cons)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameOptionHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.game_option_item_layout, parent, false)
        return GameOptionHolder(view)
    }

    override fun onBindViewHolder(holder: GameOptionHolder, position: Int) {
        val option = list[position]

        holder.nameTxt.text = option.oName
        val res = getResource(option)
        holder.iconImg.setImageResource(res)

        CommonHelper.INSTANCE.setScaleOnTouch(holder.optionCons, onclick = {
            onItemClick(option)
            holder.iconImg.setImageResource(getResource(option))
        })
    }

    fun getResource(option: GameOptions): Int {
        return if (option == GameOptions.SOUND)
            if (CommonHelper.isSoundEnabled) R.drawable.ic_go_sound else R.drawable.ic_go_stop_sound
        else if (option == GameOptions.BACKGROUND_MUSIC)
            if (CommonHelper.isMusicEnabled) R.drawable.ic_go_music else R.drawable.ic_go_stop_music
        else if (option == GameOptions.VIBRATE)
            if (CommonHelper.isVibrationEnabled) R.drawable.ic_go_vibration else R.drawable.ic_go_stop_vibration
        else
            option.res
    }

    override fun getItemCount(): Int = list.size
}
