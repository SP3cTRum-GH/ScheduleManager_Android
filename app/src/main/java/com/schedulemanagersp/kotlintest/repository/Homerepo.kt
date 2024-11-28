package com.schedulemanagersp.kotlintest.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.schedulemanagersp.kotlintest.db.AppDatabase
import com.schedulemanagersp.kotlintest.db.Home_DTO

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

    fun getAllPlan(idx: Long): LiveData<List<Home_DTO>> {
        return _db.homeDao().getAllPlanner(idx)
    }
    fun currentTime(planner: Long,time: Int) : LiveData<Long> {
        return _db.homeDao().currentTimeIndex(planner,time)
    }
}