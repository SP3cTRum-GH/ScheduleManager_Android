package com.example.kotlintest.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface Calendar_DAO {
    @Query("SELECT * FROM calendar WHERE date = :date")
    fun getAllTaskForDate(date: String): List<Calendar_DTO>

    @Insert
    fun insertTaskForDate(task: Calendar_DTO)

    @Update
    fun updateTaskForDate(done: Calendar_DTO)
}