package com.example.test_room_20191007.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Schedule constructor(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long = 0,
    @ColumnInfo(name = "title_nm") var titleName: String? = null,
    @ColumnInfo(name = "regist_date") var registDate: String? = null,
    @ColumnInfo(name = "detail_nm") var detailName: String? = null
)
