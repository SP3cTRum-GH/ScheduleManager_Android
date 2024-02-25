package com.example.kotlintest.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TodoList_DAO {
    @Query("SELECT * FROM todo WHERE plannerIndex = :index")
    fun getAllTodo(index:Long): List<TodoList_DTO>

    @Insert
    fun insertPlanner(task: TodoList_DTO)

    @Update
    fun update(done: TodoList_DTO)

    @Query("DELETE FROM todo WHERE todo = :name")
    fun deletePlanner(name: String)
}