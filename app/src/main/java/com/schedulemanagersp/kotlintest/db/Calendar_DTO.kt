package com.schedulemanagersp.kotlintest.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calendar")
data class Calendar_DTO (
    @PrimaryKey(autoGenerate = true) val index: Int = 0,
    @ColumnInfo(name = "date")val date: String,
    @ColumnInfo(name = "task") val task: String,
    @ColumnInfo(name = "starttime") val starttime: Int,
    @ColumnInfo(name = "endtime") val endtime: Int,
    @ColumnInfo(name = "done") var done: Boolean = false
)