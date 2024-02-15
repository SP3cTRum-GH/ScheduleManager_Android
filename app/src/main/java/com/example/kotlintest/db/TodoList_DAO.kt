package com.example.kotlintest.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TodoList_DAO {
    @Query("SELECT * FROM todo WHERE plannerIndex = :index")
    fun getAllTodo(index:Long): List<TodoList_DTO>

    @Insert
    fun insertPlanner(task: TodoList_DTO)

    @Query("DELETE FROM home WHERE name = :name")
    fun deletePlanner(name: String)
}