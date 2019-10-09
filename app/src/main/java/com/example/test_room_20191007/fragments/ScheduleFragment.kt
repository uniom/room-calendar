package com.example.test_room_20191007.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.test_room_20191007.R
import com.example.test_room_20191007.activities.ScheduleEditActivity
import com.example.test_room_20191007.data.ScheduleData
import com.example.test_room_20191007.databases.scheduleDB
import com.example.test_room_20191007.models.Schedule
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ScheduleFragment(val scheduleData: ScheduleData) : Fragment() {

    val errHanler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.d("test","error:" + throwable.localizedMessage)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val i = arguments?.getInt(ScheduleEditActivity.KEY_POSITION)


        fra_date_edit.setText(scheduleData?.date, TextView.BufferType.NORMAL)
        fra_title_edit.setText(scheduleData?.title, TextView.BufferType.NORMAL)
        fra_detail_edit.setText(scheduleData?.detail, TextView.BufferType.NORMAL)

        btnSave.setOnClickListener { scheduleRegist() }
        if ( scheduleData == null ) btnDelete.visibility = View.GONE
        btnDelete.setOnClickListener { deleteSchedule() }
    }

    private fun scheduleRegist() {
        inputCheck()
        regist()
    }

    private fun inputCheck(): Boolean {
        val dateedit = fra_date_edit.text.toString()
        val titleedit = fra_title_edit.text.toString()
        val detailedit = fra_detail_edit.text.toString()
        var checkResult = true

        if ( "".equals(dateedit)) checkResult = false
        if ( "".equals(titleedit)) checkResult = false
        if ( "".equals(detailedit)) checkResult = false

        return checkResult
    }

    fun regist() {
        val date = fra_date_edit.text.toString()
        val title = fra_title_edit.text.toString()
        val detaile = fra_detail_edit.text.toString()
        val schedule = Schedule()
        if (scheduleData != null ) schedule.id = scheduleData!!.id
        schedule.titleName = title
        schedule.registDate = date
        schedule.detailName = detaile

        //Main Thread
        GlobalScope.launch(errHanler) {
            //Background
            create(schedule)
            GlobalScope.launch(Dispatchers.Main) {
                //Main Thread
                Toast.makeText(context, "登録しました", Toast.LENGTH_SHORT).show()

                activity!!.finish()
            }
        }
    }

    fun create(schedule: Schedule) {
        activity!!.scheduleDB.createSchedule(schedule)
    }

    fun deleteSchedule() {
        val schedule = Schedule()
        if (scheduleData != null ) schedule.id = scheduleData!!.id
        //Main Thread
        GlobalScope.launch(errHanler) {
            //Background
            delete(schedule)
            GlobalScope.launch(Dispatchers.Main) {
                //Main Thread
                Toast.makeText(activity!!, "削除しました", Toast.LENGTH_SHORT).show()
                activity!!.finish()
            }
        }
    }

    fun delete(schedule: Schedule) {
        activity!!.scheduleDB.deleteSchedule(schedule)
    }

}