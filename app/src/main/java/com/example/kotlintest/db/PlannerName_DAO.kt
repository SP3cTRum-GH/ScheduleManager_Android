package com.example.kotlintest.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PlannerName_DAO {
    @Query("SELECT * FROM planner")
    fun getAllPlanner(): LiveData<List<PlannerName_DTO>>

    @Insert
    fun insertPlanner(task: PlannerName_DTO)

    @Delete
    fun deletePlanner(obj: PlannerName_DTO)
}