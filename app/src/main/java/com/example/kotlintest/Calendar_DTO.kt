package com.example.kotlintest

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "calendar")
data class Calendar_DTO (
    @PrimaryKey(autoGenerate = true) val index: Long = 0,
    @ColumnInfo(name = "date")val date: String,
    @ColumnInfo(name = "task") val task: String,
    @ColumnInfo(name = "starttime") val starttime: String,
    @ColumnInfo(name = "endtime") val endtime: String,
    @ColumnInfo(name = "done") var done: Boolean = false
)