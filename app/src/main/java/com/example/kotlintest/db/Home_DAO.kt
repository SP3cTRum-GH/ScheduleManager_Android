package com.example.kotlintest.db

import androidx.room.*

@Dao
interface Home_DAO {
    @Query("SELECT name FROM home")
    fun getAllPlanner(): List<String>

    @Insert
    fun insertPlanner(task: Home_DTO)

    @Query("DELETE FROM home WHERE name = :name")
    fun deletePlanner(name: String)
}