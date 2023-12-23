package com.example.kotlintest

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calendar")
data class Calendar_DTO (
    @PrimaryKey val date: String,
    @ColumnInfo(name = "task") val task: String,
    @ColumnInfo(name = "starttime") val starttime: String,
    @ColumnInfo(name = "endtime") val endtime: String
)