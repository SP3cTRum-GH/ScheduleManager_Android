package com.example.kotlintest

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DAO {
    @Query("SELECT * FROM calendar WHERE date = :date")
    fun getAllTaskForDate(date: String): List<Calendar_DTO>

    @Insert
    fun insertTaskForDate(task: Calendar_DTO)
}