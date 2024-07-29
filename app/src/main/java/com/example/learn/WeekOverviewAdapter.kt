package com.example.learn

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WeekOverviewAdapter(private val weekOverviewList: List<WeekOverviewEntry>) :
    RecyclerView.Adapter<WeekOverviewAdapter.WeekOverviewViewHolder>() {

    class WeekOverviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewWeek: TextView = itemView.findViewById(R.id.textViewWeek)
        val textViewTotal: TextView = itemView.findViewById(R.id.textViewTotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekOverviewViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_week_overview, parent, false)
        return WeekOverviewViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WeekOverviewViewHolder, position: Int) {
        val currentItem = weekOverviewList[position]
        holder.textViewWeek.text = "Week: ${currentItem.week}"
        holder.textViewTotal.text = "Total Push-ups: ${currentItem.total}"
    }

    override fun getItemCount() = weekOverviewList.size
}
