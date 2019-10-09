package com.example.test_room_20191007.databases

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

import com.example.test_room_20191007.interfaces.ScheduleDao
import com.example.test_room_20191007.models.Schedule


@Dao
@Database(entities = arrayOf(Schedule::class), version = 1)
abstract class ScheduleDataBase : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao

    companion object {
        private var db: ScheduleDataBase? = null

        fun getInstance(context: Context): ScheduleDataBase {
            if (db == null) {
                synchronized(ScheduleDataBase::class) {
                    if (db == null) {
                        db = Room.databaseBuilder(context.applicationContext, ScheduleDataBase::class.java, "schedules.db")
                            .addCallback(object : Callback() {
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    super.onCreate(db)
                                }
                            })
                            .fallbackToDestructiveMigration()
                            .build()
                        db!!.openHelper.setWriteAheadLoggingEnabled(true)
                    }
                }
            }
            return db!!
        }

        fun destroyInstance() {
            db = null
        }

    }
}

