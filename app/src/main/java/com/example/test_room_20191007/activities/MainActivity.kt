package com.example.test_room_20191007.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test_room_20191007.R
import com.example.test_room_20191007.adapter.ScheduleViewAdapter
import com.example.test_room_20191007.data.ScheduleData
import com.example.test_room_20191007.databases.scheduleDB
import com.example.test_room_20191007.models.Schedule
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    var scheduleDataList = ArrayList<ScheduleData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //テストデータの生成
//        val date = SimpleDateFormat("yyyy/MM/dd").format(Date())
//        val scheduleDataList = mutableListOf<ScheduleData>()
//        var cnt = 0
//        repeat((0..100).count()) {
//            cnt++
//            scheduleDataList.add(ScheduleData(cnt.toLong(), "テストデータ" + cnt, date.toString(), "detail"))
//        }

        //RecyclerViewにAdapterとLayoutManagerを設定
        findViewById<RecyclerView>(R.id.scheduleRecyclerView).also { recyclerView: RecyclerView ->
            recyclerView.adapter = ScheduleViewAdapter(this, scheduleDataList)
            recyclerView.layoutManager = LinearLayoutManager(this)
        }

        fab_add.setOnClickListener {
//            startActivity(Intent(this, ScheduleEditActivity::class.java))

            val scheduleData = ScheduleData( id = 0L, title = "", date = "", detail = "")
            scheduleDataList.add(scheduleData)
            val intent = Intent(this, ScheduleEditActivity::class.java)
            intent.putExtra(ScheduleEditActivity.KEY_POSITION, scheduleDataList.size)
            intent.putExtra(ScheduleEditActivity.KEY_SCHEDULE, Gson().toJson(scheduleDataList))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        //DBアクセス
        loadSchedule()
    }

    private fun loadSchedule() {
        GlobalScope.launch(errHandler) {
            //background
            val scheduleList = scheduleDB.findAll().toMutableList() as ArrayList<Schedule>

            GlobalScope.launch(Dispatchers.Main) {
//                //UIs
//                val scheduleDataList = mutableListOf<ScheduleData>()
//                for ( schedule in scheduleList ) {
//                    scheduleDataList.add(ScheduleData(schedule.id, schedule.titleName, schedule.registDate, schedule.detailName))
//                }

                //※下記を変更
                scheduleDataList = mutableListOf<ScheduleData>() as ArrayList<ScheduleData>
                for ( schedule in scheduleList ) {
                    scheduleDataList.add(ScheduleData(schedule.id, schedule.titleName, schedule.registDate, schedule.detailName))
                }

                //RecyclerViewにAdapterとLayoutManagerを設定
                findViewById<RecyclerView>(R.id.scheduleRecyclerView).also { recyclerView: RecyclerView ->
                    recyclerView.adapter = ScheduleViewAdapter(applicationContext, scheduleDataList)
                    recyclerView.layoutManager = LinearLayoutManager(applicationContext)
                }
            }
        }
    }

    val errHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.d("coroutine", "error" + throwable.localizedMessage)
    }

    override fun onResume() {
        super.onResume()
        //DBアクセス
        loadSchedule()
    }
}
