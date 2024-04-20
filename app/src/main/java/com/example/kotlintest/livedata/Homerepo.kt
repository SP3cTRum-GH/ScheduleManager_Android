package com.example.kotlintest.livedata

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.kotlintest.db.AppDatabase
import com.example.kotlintest.db.Home_DTO
import com.example.kotlintest.db.TodoList_DTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Homerepo {
    private var _db: AppDatabase

    constructor(context: Context) {
        this._db = AppDatabase.getDatabase(context)
    }

    fun insertPlan(item: Home_DTO){
        _db.homeDao().insertPlan(item)
    }
    fun removePlan(item: Home_DTO){
        _db.homeDao().deletePlanner(item)
        _db.todoDao().deleteAllTodo(item.index)

    }

    fun getAllPlan(idx: Long): List<Home_DTO> {
        return _db.homeDao().getAllPlanner(idx)
    }
}