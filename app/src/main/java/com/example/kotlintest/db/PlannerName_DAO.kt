package com.example.kotlintest.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PlannerName_DAO {
    @Query("SELECT * FROM planner")
    fun getAllPlanner(): List<PlannerName_DTO>

    @Insert
    fun insertPlanner(task: PlannerName_DTO)

    @Query("DELETE FROM planner WHERE name = :name")
    fun deletePlanner(name: String)
}