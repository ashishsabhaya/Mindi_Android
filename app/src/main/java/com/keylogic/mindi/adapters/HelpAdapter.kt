package com.keylogic.mindi.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.keylogic.mindi.R
import com.keylogic.mindi.custom.StrokeTextView
import com.keylogic.mindi.models.TableInformation

class HelpAdapter(
    private var list: List<String>
) : RecyclerView.Adapter<HelpAdapter.TableInfoHolder>() {

    class TableInfoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTxt: StrokeTextView = itemView.findViewById(R.id.title_txt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableInfoHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.help_item_layout, parent, false)
        return TableInfoHolder(view)
    }

    override fun onBindViewHolder(holder: TableInfoHolder, position: Int) {
        holder.titleTxt.text = list[position]
    }

    override fun getItemCount(): Int = list.size
}
