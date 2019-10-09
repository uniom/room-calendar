package com.example.test_room_20191007.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.test_room_20191007.R
import com.example.test_room_20191007.data.ScheduleData
import com.example.test_room_20191007.databases.scheduleDB
import com.example.test_room_20191007.fragments.ScheduleFragment
import com.example.test_room_20191007.models.Schedule
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_schedule_edit.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ScheduleEditActivity : AppCompatActivity() {


    var scheduleDataList: List<ScheduleData>? = null
    private var position = 0
    private lateinit var mViewPager: ViewPager

    companion object {
        val KEY_SCHEDULE = "key_schedule"
        val KEY_POSITION = "key_position"
    }
    var scheduleData: ScheduleData? = null

    val errHanler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.d("test","error:" + throwable.localizedMessage)
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_schedule_edit)
//
//        btnSave.setOnClickListener {
//            if ( !inputCheck() ) {
//                Toast.makeText(this, "入力に誤りがあります", Toast.LENGTH_SHORT)
//            } else {
//                regist()
//            }
//        }
//
//        btnDelete.setOnClickListener {
//            deleteSchedule()
//        }
//
//        val schedules = intent.getStringExtra(KEY_SCHEDULE)
//        if ( schedules == null ) {
//            btnDelete.visibility = View.GONE
//            return
//        }
//        scheduleData = Gson().fromJson<ScheduleData>(schedules, ScheduleData::class.java)
//        dateEdit.setText(scheduleData?.date, TextView.BufferType.NORMAL)
//        titleEdit.setText(scheduleData?.title, TextView.BufferType.NORMAL)
//        detailEdit.setText(scheduleData?.detail, TextView.BufferType.NORMAL)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_pager)

        val schedules = intent.getStringExtra(KEY_SCHEDULE)
        position = intent.getIntExtra(KEY_POSITION,0)

        scheduleDataList = Gson().fromJson<List<ScheduleData>>(schedules, object : TypeToken<List<ScheduleData>>() {}.type)
        if ( scheduleDataList == null ) {
            val list = ArrayList<ScheduleData>()
            val scheduleData = ScheduleData( id = 0L, title = "", date = "", detail = "")
            list.add(scheduleData)
            setViewPager(list)
        } else {
            setViewPager(scheduleDataList!!)
        }
    }

    private fun setViewPager(dataList: List<ScheduleData>){
        mViewPager = findViewById(R.id.view_pager)

        val fragmentManager = supportFragmentManager
        mViewPager.apply {
            adapter = object : FragmentStatePagerAdapter(fragmentManager) {
                override fun getItem(position: Int): Fragment {
                    val fragment = ScheduleFragment(dataList.get(position))
                    fragment.arguments = Bundle().apply {
                        putInt(KEY_POSITION, position)
                    }
                    return fragment
                }

                override fun getCount(): Int {
                    return dataList.size
                }

                override fun getPageTitle(position: Int): CharSequence? {
                    return "${position + 1}"
                }

            }
            if ( position != null) setCurrentItem(position)
        }
    }

    private fun inputCheck(): Boolean {
        val dateedit = dateEdit.text.toString()
        val titleedit = titleEdit.text.toString()
        val detailedit = detailEdit.text.toString()
        var checkResult = true

        if ( "".equals(dateedit)) checkResult = false
        if ( "".equals(titleedit)) checkResult = false
        if ( "".equals(detailedit)) checkResult = false

        return checkResult
    }

    fun regist() {
        val titleedit = titleEdit.text.toString()
        val dateedit = dateEdit.text.toString()
        val detailedit = detailEdit.text.toString()
        val schedule = Schedule()
        if( scheduleData != null ) schedule.id = scheduleData!!.id    //追加
        schedule.titleName = titleedit
        schedule.registDate = dateedit
        schedule.detailName = detailedit

        //Main Thread
        GlobalScope.launch(errHanler) {
            //Background
            create(schedule)
            GlobalScope.launch(Dispatchers.Main) {
                //Main Thread
                Toast.makeText(applicationContext, "登録しました", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    suspend fun create(schedule: Schedule) {
        scheduleDB.createSchedule(schedule)
    }

    fun deleteSchedule() {
        val schedule = Schedule()
        if (scheduleData != null ) schedule.id = scheduleData!!.id
        GlobalScope.launch(errHanler) {
            //Background
            delete(schedule)
            GlobalScope.launch(Dispatchers.Main) {
                //Main Thread
                Toast.makeText(applicationContext, "削除しました", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    fun delete(schedule: Schedule) {
        scheduleDB.deleteSchedule(schedule)
    }
}
