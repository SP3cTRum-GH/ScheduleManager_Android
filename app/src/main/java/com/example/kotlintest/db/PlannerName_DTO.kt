package com.example.kotlintest.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "planner")
data class PlannerName_DTO (
    @PrimaryKey(autoGenerate = true) val index: Long = 0,
    @ColumnInfo val name: String
)