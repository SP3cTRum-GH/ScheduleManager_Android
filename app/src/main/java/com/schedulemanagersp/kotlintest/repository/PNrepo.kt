package com.schedulemanagersp.kotlintest.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.schedulemanagersp.kotlintest.db.AppDatabase
import com.schedulemanagersp.kotlintest.db.PlannerName_DTO

class PNrepo {
    private var _db: AppDatabase

    constructor(context: Context) {
        this._db = AppDatabase.getDatabase(context)
    }

    fun getAllPlanner(): LiveData<List<PlannerName_DTO>>{
        return _db.plannerDao().getAllPlanner()
    }

    fun insertPlanner(item: PlannerName_DTO){
        _db.plannerDao().insertPlanner(item)
    }
    fun removePlanner(item: PlannerName_DTO){
        _db.plannerDao().deletePlanner(item)
        _db.homeDao().deleteAllplan(item.index)
        var list = _db.homeDao().selectIndex(item.index)
        for(i in list){
            _db.todoDao().deleteAllTodo(i)
        }
    }
}