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
    //Planner데이터 처리
    fun getAllPlan(idx: Long) {
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

    fun getAllPlanner(){
        val plannerName = ArrayList<PlannerName_DTO>()
        CoroutineScope(Dispatchers.Main).launch {
            val ret = CoroutineScope(Dispatchers.IO).async {
                plannerName.addAll(_db.plannerDao().getAllPlanner())
            }
            ret.await()
            repo._plannerNamelist.value = plannerName
        }
    }

    fun insertPlanner(){

    }
    fun removePlanner(){

    }
}