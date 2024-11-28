package com.schedulemanagersp.kotlintest.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "home")
data class Home_DTO (
    @PrimaryKey(autoGenerate = true) val index: Long = 0,
    @ColumnInfo val starttime: Int,
    @ColumnInfo val endtime: Int,
    @ColumnInfo val task: String,
    @ColumnInfo val name: Long
)