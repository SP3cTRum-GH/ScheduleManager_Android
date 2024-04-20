package com.example.kotlintest.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface Home_DAO {
    @Query("SELECT * FROM home WHERE name = :name")
    fun getAllPlanner(name:Long): List<Home_DTO>

    @Query("SELECT * FROM home")
    fun getAllPlanner(): List<Home_DTO>

    @Insert
    fun insertPlan(task: Home_DTO)

//    @Query("DELETE FROM home WHERE index = :index")
    @Delete
    fun deletePlanner(obj: Home_DTO)

    @Query("DELETE FROM home WHERE name = :planner")
    fun deleteAllplan(planner: Long)

    @Query("SELECT `index` FROM home WHERE name = :planner")
    fun selectIndex(planner: Long): List<Long>
}