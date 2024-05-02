package com.example.kotlintest.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.kotlintest.db.AppDatabase
import com.example.kotlintest.db.TodoList_DTO


class Todorepo {
    private var _db: AppDatabase

    constructor(context: Context) {
        this._db = AppDatabase.getDatabase(context)
    }

    fun getAllTodo(item: Long): LiveData<List<TodoList_DTO>>{
        return _db.todoDao().getAllTodo(item)
    }

    fun getAllTodoList(item: Long): List<TodoList_DTO>{
        return _db.todoDao().getAllTodoList(item)
    }

    fun updateTodo(item:TodoList_DTO){
        _db.todoDao().update(item)
    }

    fun insertTodo(item: TodoList_DTO){
        _db.todoDao().insertTodo(item)
    }

    fun removeTodo(item: TodoList_DTO){
        _db.todoDao().deleteTodo(item)
    }

}