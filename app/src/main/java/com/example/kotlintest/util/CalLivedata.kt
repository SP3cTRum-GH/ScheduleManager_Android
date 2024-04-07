package com.example.kotlintest.util

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.kotlintest.db.AppDatabase
import com.example.kotlintest.db.Calendar_DTO
import com.example.kotlintest.db.DataRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CalLivedata: ViewModel {
    private var _db: AppDatabase
    val repo = DataRepo()

    constructor(context: Context) {
        this._db = AppDatabase.getDatabase(context)
    }

    // Calendar 데이터 처리
    fun removeCalendarTable(item: Calendar_DTO) {
        val itList = repo._callist.value!!
        itList.remove(item)
        repo._callist.value = itList

        CoroutineScope(Dispatchers.IO).async {
            _db.calDao().deletetable(item)
        }
    }

    fun insertTaskForDate(item: Calendar_DTO) {
        val tmp = repo._callist.value!!
        val res = ArrayList<Calendar_DTO>()
        res.addAll(tmp)
        res.add(item)
        repo._callist.value = res
        CoroutineScope(Dispatchers.IO).async {
            _db.calDao().insertTaskForDate(item)
        }
    }

    fun getAllTaskForDate(date: String) {
        val tasks = ArrayList<Calendar_DTO>()

        CoroutineScope(Dispatchers.Main).launch {
            val ret = CoroutineScope(Dispatchers.IO).async {
                val t = _db.calDao().getAllTaskForDate(date)
                tasks.addAll(t)
            }

            ret.await()
            repo._callist.value = tasks
        }
    }
}