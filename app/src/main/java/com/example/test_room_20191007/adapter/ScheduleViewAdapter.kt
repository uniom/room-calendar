package com.example.test_room_20191007.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.test_room_20191007.R
import com.example.test_room_20191007.activities.ScheduleEditActivity
import com.example.test_room_20191007.data.ScheduleData
import com.example.test_room_20191007.databinding.ListItemBinding
import com.google.gson.Gson

class ScheduleViewAdapter(private val context: Context, private val scheduleDataList: List<ScheduleData>) :
RecyclerView.Adapter<ScheduleViewAdapter.ScheduleViewHolder>() {

    class ScheduleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val listItemBinding: ListItemBinding? = DataBindingUtil.bind(view)
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
        holder.listItemBinding?.scheduleTitle?.text = scheduleDataList[position].title
        holder.listItemBinding?.scheduleDate?.text = scheduleDataList[position].date
        holder.listItemBinding?.scheduleDetail?.text = scheduleDataList[position].detail
        holder.listItemBinding?.scheduleIcon?.setImageResource(R.mipmap.ic_launcher)

        holder.listItemBinding?.scheduleIcon?.setOnClickListener {
            val intent = Intent(context , ScheduleEditActivity::class.java)
            intent.putExtra(ScheduleEditActivity.KEY_SCHEDULE, Gson().toJson(scheduleDataList))
            intent.putExtra(ScheduleEditActivity.KEY_POSITION, position)

            this.context.startActivity(intent)
        }
    }
}
