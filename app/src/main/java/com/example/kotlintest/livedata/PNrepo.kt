package com.example.kotlintest.livedata

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.kotlintest.db.AppDatabase
import com.example.kotlintest.db.PlannerName_DTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class PNrepo {
    private var _db: AppDatabase

    constructor(context: Context) {
        this._db = AppDatabase.getDatabase(context)
    }

    fun getAllPlanner(): List<PlannerName_DTO>{
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