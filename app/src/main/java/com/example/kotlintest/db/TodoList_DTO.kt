package com.example.kotlintest.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo")
data class TodoList_DTO (
    @PrimaryKey(autoGenerate = true) val index: Long = 0,
    @ColumnInfo val todo: String,
    @ColumnInfo val plannerIndex: Long
)