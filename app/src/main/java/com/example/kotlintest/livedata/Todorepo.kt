package com.example.kotlintest.livedata

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.kotlintest.db.AppDatabase
import com.example.kotlintest.db.TodoList_DTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class Todorepo {
    private var _db: AppDatabase

    constructor(context: Context) {
        this._db = AppDatabase.getDatabase(context)
    }

    fun getAllTodo(item: Long): List<TodoList_DTO>{
        return _db.todoDao().getAllTodo(item)
    }


    fun insertTodo(item: TodoList_DTO){
        _db.todoDao().insertTodo(item)
    }

    fun removeTodo(item: TodoList_DTO){
        _db.todoDao().deleteTodo(item)
    }

}