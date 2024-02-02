package com.example.kotlintest.db

import androidx.room.*

@Dao
interface Calendar_DAO {
    @Query("SELECT * FROM calendar WHERE date = :date")
    fun getAllTaskForDate(date: String): List<Calendar_DTO>

    @Insert
    fun insertTaskForDate(task: Calendar_DTO)

    @Update
    fun updateTaskForDate(done: Calendar_DTO)

    @Delete
    fun deletetable(task: Calendar_DTO)
}