package com.example.kotlintest.util

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.kotlintest.db.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PlannerLivedata : ViewModel{
    private var _db: AppDatabase
    val repo = DataRepo()

    constructor(context: Context) {
        this._db = AppDatabase.getDatabase(context)
    }

    // 전체 데이터 DB에서 불러오기
    fun loadDataFromDb() {
        CoroutineScope(Dispatchers.Main).launch {
            var home: ArrayList<Home_DTO> = ArrayList()
            var cal: ArrayList<Calendar_DTO> = ArrayList()
            var todo: ArrayList<TodoList_DTO> = ArrayList()
            var todoindex:Long = -1
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("HH : mm")
            val formatted = current.format(formatter).toString().split(" : ")
            val t = formatted[0].toInt() * 60 + formatted[1].toInt()
            val ret = CoroutineScope(Dispatchers.IO).async {
                home.addAll(_db.homeDao().getAllPlanner())
                cal.addAll(_db.calDao().getAllTaskForDate(LocalDate.now().toString()))

                for(i in home){
                    val start = i.starttime
                    val end = i.endtime

                    if(start<=t && t<=end){
                        todoindex = i.index
                        break
                    }
                }
            }

            if(home.isNotEmpty()) {
                todo.addAll(_db.todoDao().getAllTodo(todoindex))
            }
            ret.await()

            repo._homelist.value = home
            repo._todolist.value = todo
        }
    }

    //Planner데이터 처리
    fun getAllPlanner(idx: Long) {
        val home = ArrayList<Home_DTO>()
        val todo = ArrayList<TodoList_DTO>()
        var todoindex: Long = -1
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH : mm")
        val formatted = current.format(formatter).toString().split(" : ")
        val t = formatted[0].toInt() * 60 + formatted[1].toInt()
        CoroutineScope(Dispatchers.Main).launch {
            val ret = CoroutineScope(Dispatchers.IO).async {
                home.addAll(_db.homeDao().getAllPlanner(idx))

                for(i in home){
                    val start = i.starttime
                    val end = i.endtime

                    if(start<=t && t<=end){
                        todoindex = i.index
                        break
                    }
                }

                if(home.isNotEmpty()) {
                    todo.addAll(_db.todoDao().getAllTodo(todoindex))
                }
            }

            ret.await()
            repo._homelist.value = home
            repo._todolist.value = todo
        }
    }
}