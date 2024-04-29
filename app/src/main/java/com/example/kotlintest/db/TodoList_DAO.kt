package com.example.kotlintest.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoList_DAO {
    @Query("SELECT * FROM todo WHERE plannerIndex = :index")
    fun getAllTodo(index:Long): LiveData<List<TodoList_DTO>>

    @Query("SELECT * FROM todo WHERE plannerIndex = :index")
    fun getAllTodoList(index:Long): List<TodoList_DTO>

    @Insert
    fun insertTodo(task: TodoList_DTO)

    @Update
    fun update(done: TodoList_DTO)

    @Delete
    fun deleteTodo(obj: TodoList_DTO)

    @Query("DELETE FROM todo WHERE plannerindex = :planner")
    fun deleteAllTodo(planner:Long)
}