package com.example.test_room_20191007.interfaces

import androidx.room.*
import com.example.test_room_20191007.models.Schedule

@Dao
interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createSchedule(schedule: Schedule)

    @Query("SELECT * FROM Schedule ")
    fun findAll(): List<Schedule>

    @Update
    fun updateSchedule(schedule: Schedule)

    @Delete
    fun deleteSchedule(schedule: Schedule)
}
