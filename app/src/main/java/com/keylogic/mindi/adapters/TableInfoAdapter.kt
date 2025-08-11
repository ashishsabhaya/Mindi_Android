package com.keylogic.mindi.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.keylogic.mindi.R
import com.keylogic.mindi.custom.StrokeTextView
import com.keylogic.mindi.models.TableInformation

class TableInfoAdapter(
    private var list: List<TableInformation>
) : RecyclerView.Adapter<TableInfoAdapter.TableInfoHolder>() {

    class TableInfoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTxt: StrokeTextView = itemView.findViewById(R.id.title_txt)
        val subTitleTxt: StrokeTextView = itemView.findViewById(R.id.sub_title_txt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableInfoHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.table_info_item_layout, parent, false)
        return TableInfoHolder(view)
    }

    override fun onBindViewHolder(holder: TableInfoHolder, position: Int) {
        val item = list[position]

        holder.titleTxt.text = item.title
        holder.subTitleTxt.text = item.subTitle
    }

    override fun getItemCount(): Int = list.size
}
