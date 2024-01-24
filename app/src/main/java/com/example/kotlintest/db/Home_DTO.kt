package com.example.kotlintest.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "home")
data class Home_DTO (
    @PrimaryKey val id: String, //name+starttime으로 Primarykey세팅
    @ColumnInfo val starttime: String,
    @ColumnInfo val endtime: String,
    @ColumnInfo val task: String,
    @ColumnInfo val name: String
)