package com.example.test_room_20191007.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.test_room_20191007.R
import com.example.test_room_20191007.activities.ScheduleEditActivity
import com.example.test_room_20191007.data.ScheduleData
import com.google.gson.Gson

class ScheduleViewAdapter(private val context: Context, private val scheduleDataList: List<ScheduleData>) :
RecyclerView.Adapter<ScheduleViewAdapter.ScheduleViewHolder>() {

    class ScheduleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iconImageView: ImageView = view.findViewById(R.id.scheduleIcon)
        val titleTextView: TextView = view.findViewById(R.id.scheduleTitle)
        val dateTextView: TextView = view.findViewById(R.id.scheduleDate)
        val detailTextView: TextView = view.findViewById(R.id.scheduleDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder =
        ScheduleViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.list_item,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = scheduleDataList.size

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.iconImageView.setImageResource(R.mipmap.ic_launcher)
        holder.titleTextView.text = scheduleDataList[position].title
        holder.dateTextView.text = scheduleDataList[position].date
        holder.detailTextView.text = scheduleDataList[position].detail

        holder.iconImageView.setOnClickListener {
            val intent = Intent(context , ScheduleEditActivity::class.java)
//            intent.putExtra(ScheduleEditActivity.KEY_SCHEDULE, Gson().toJson(scheduleDataList[position]))
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra(ScheduleEditActivity.KEY_SCHEDULE, Gson().toJson(scheduleDataList))
            intent.putExtra(ScheduleEditActivity.KEY_POSITION, position)

            this.context.startActivity(intent)
        }
    }
}
