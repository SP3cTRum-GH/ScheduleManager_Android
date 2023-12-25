package com.example.kotlintest

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DAO {
    @Query("SELECT * FROM calendar")
    fun getAllTaskForDate(): List<Calendar_DTO>

    @Insert
    fun insertTaskForDate(task: Calendar_DTO)
}