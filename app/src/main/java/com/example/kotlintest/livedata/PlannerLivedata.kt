package com.example.kotlintest.livedata

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.kotlintest.db.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
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

    fun insertPlanner(item:PlannerName_DTO){
        val tmp = repo._plannerNamelist.value!!
        val res = ArrayList<PlannerName_DTO>()
        res.addAll(tmp)
        res.add(item)
        repo._plannerNamelist.value = res
        CoroutineScope(Dispatchers.IO).async{
            _db.plannerDao().insertPlanner(item)
        }
    }
    fun removePlanner(item:PlannerName_DTO){
        val itList = repo._plannerNamelist.value!!
        itList.remove(item)
        val homeItList = repo._homelist.value!!
        val todoItList = repo._todolist.value!!
        homeItList.clear()
        todoItList.clear()

        CoroutineScope(Dispatchers.IO).async {
            _db.plannerDao().deletePlanner(item)
            _db.homeDao().deleteAllplan(item.index)
            var list = _db.homeDao().selectIndex(item.index)
            for(i in list){
                _db.todoDao().deleteAllTodo(i)
            }
        }
    }
    fun insertPlan(item: Home_DTO){
        val tmp = repo._homelist.value!!
        val res = ArrayList<Home_DTO>()
        res.addAll(tmp)
        res.add(item)
        repo._homelist.value = res
        CoroutineScope(Dispatchers.IO).async {
            _db.homeDao().insertPlan(item)
        }
    }
    fun removePlan(item: Home_DTO){
        val itList = repo._homelist.value!!
        itList.remove(item)
        val todoItList = repo._todolist.value!!
        todoItList.clear()

        CoroutineScope(Dispatchers.IO).async {
            _db.homeDao().deletePlanner(item)
            _db.todoDao().deleteAllTodo(item.index)
        }
    }

    fun getAllTodo(item: Long){
        val todo = ArrayList<TodoList_DTO>()
        CoroutineScope(Dispatchers.Main).launch {
            val ret = CoroutineScope(Dispatchers.IO).async {
                todo.addAll(_db.todoDao().getAllTodo(item))
            }
            ret.await()
            repo._todolist.value = todo
        }
    }
    fun insertTodo(item: TodoList_DTO){
        val tmp = repo._todolist.value!!
        val res = ArrayList<TodoList_DTO>()
        res.addAll(tmp)
        res.add(item)
        repo._todolist.value = res
        CoroutineScope(Dispatchers.IO).async {
            _db.todoDao().insertTodo(item)
        }
    }

    fun removeTodo(item: TodoList_DTO){
        val itList = repo._todolist.value!!
        itList.remove(item)
        repo._todolist.value = itList

        CoroutineScope(Dispatchers.IO).async {
            _db.todoDao().deleteTodo(item)
        }
    }
}