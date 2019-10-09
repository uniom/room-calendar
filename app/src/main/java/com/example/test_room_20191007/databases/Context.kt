package com.example.test_room_20191007.databases

import android.content.Context
import com.example.test_room_20191007.interfaces.ScheduleDao

val Context.scheduleDB: ScheduleDao get() = ScheduleDataBase.getInstance(applicationContext).scheduleDao()
