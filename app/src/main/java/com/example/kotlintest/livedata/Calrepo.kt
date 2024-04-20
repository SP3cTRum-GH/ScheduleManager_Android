package com.example.kotlintest.livedata

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kotlintest.db.AppDatabase
import com.example.kotlintest.db.Calendar_DTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class Calrepo {
    private var _db: AppDatabase

    constructor(context: Context) {
        this._db = AppDatabase.getDatabase(context)
    }

    fun removeCalendarTable(item: Calendar_DTO) {
        _db.calDao().deletetable(item)
    }

    fun insertTaskForDate(item: Calendar_DTO) {
        _db.calDao().insertTaskForDate(item)
    }

    fun updateTaskForDate(item: Calendar_DTO) {
        _db.calDao().updateTaskForDate(item)
    }


    fun getAllTaskForDate(date: String) : LiveData<List<Calendar_DTO>> {
        return _db.calDao().getAllTaskForDate(date)
    }
}