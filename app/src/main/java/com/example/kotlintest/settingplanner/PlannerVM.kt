package com.example.kotlintest.settingplanner

import android.app.Application
import androidx.lifecycle.*
import com.example.kotlintest.calendar.CalendarVM
import com.example.kotlintest.db.Calendar_DTO
import com.example.kotlintest.db.Home_DTO
import com.example.kotlintest.db.PlannerName_DTO
import com.example.kotlintest.db.TodoList_DTO
import com.example.kotlintest.livedata.Calrepo
import com.example.kotlintest.livedata.Homerepo
import com.example.kotlintest.livedata.PNrepo
import com.example.kotlintest.livedata.Todorepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlannerVM(app:Application): AndroidViewModel(app) {
    private val pnrepo = PNrepo(app)
    private val planrepo = Homerepo(app)
    private val todorepo = Todorepo(app)
    val pnitem : LiveData<List<PlannerName_DTO>>
    val planitem: LiveData<List<Home_DTO>>
    val todoitem: LiveData<List<TodoList_DTO>>
    val planquery = MutableLiveData<Long>()
    val todoquery = MutableLiveData<Long>()

    init {
        pnitem = pnrepo.getAllPlanner()
        planitem = Transformations.switchMap(planquery) { query ->
            planrepo.getAllPlan(query)
        }
        todoitem = Transformations.switchMap(todoquery) { query ->
            todorepo.getAllTodo(query)
        }
    }

    class Factory(val app: Application): ViewModelProvider.Factory{
        override fun <T: ViewModel> create(modelClass: Class<T>):T {
            return PlannerVM(app) as T
        }
    }

    //Planner
    fun getAllPlanner(): LiveData<List<PlannerName_DTO>>{
        return pnitem
    }
    fun insertPlanner(item: PlannerName_DTO){
        CoroutineScope(Dispatchers.IO).launch {
            pnrepo.insertPlanner(item)
        }
    }
    fun removePlanner(item: PlannerName_DTO){
        CoroutineScope(Dispatchers.IO).launch {
            pnrepo.removePlanner(item)
        }
    }

    //Plan
    fun getAllPlan(): LiveData<List<Home_DTO>> {
        return planitem
    }
    fun insertPlan(item: Home_DTO){
        CoroutineScope(Dispatchers.IO).launch {
            planrepo.insertPlan(item)
        }
    }
    fun removePlan(item: Home_DTO){
        CoroutineScope(Dispatchers.IO).launch {
            planrepo.removePlan(item)
        }
    }

    //Todo
    fun getAllTodo(): LiveData<List<TodoList_DTO>>{
        return todoitem
    }
    fun insertTodo(item: TodoList_DTO){
        CoroutineScope(Dispatchers.IO).launch {
            todorepo.insertTodo(item)
        }
    }
    fun removeTodo(item: TodoList_DTO){
        CoroutineScope(Dispatchers.IO).launch {
            todorepo.removeTodo(item)
        }
    }

    fun setPlanQuery(idx: Long){
        planquery.value = idx
    }
    fun setTodoQuery(idx: Long){
        todoquery.value = idx
    }
}