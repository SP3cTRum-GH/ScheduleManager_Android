package com.example.kotlintest.home

import android.app.Application
import androidx.lifecycle.*
import androidx.room.util.query
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
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeVM(app: Application): AndroidViewModel(app){
    private val pnrepo = PNrepo(app)
    private val calrepo = Calrepo(app)
    private val homerepo = Homerepo(app)
    private val todorepo = Todorepo(app)
    val todoindex: LiveData<Long>
    val calitem: LiveData<List<Calendar_DTO>>
    val planitem: LiveData<List<Home_DTO>>
    val calquery = MutableLiveData<String>()
    val planquery = MutableLiveData<Long>()

    init {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH : mm")
        val formatted = current.format(formatter).toString().split(" : ")
        val t = formatted[0].toInt() * 60 + formatted[1].toInt()

        calitem = Transformations.switchMap(calquery) { query ->
            calrepo.getCurrentTime(query,t)
        }
        planitem = Transformations.switchMap(planquery) { query ->
            homerepo.getAllPlan(query)
        }
        todoindex = Transformations.switchMap(planquery){ query ->
            homerepo.currentTime(query,t)
        }
    }

    class Factory(val app: Application): ViewModelProvider.Factory{
        override fun <T: ViewModel> create(modelClass: Class<T>):T {
            return HomeVM(app) as T
        }
    }

    fun getAllPlanner(): LiveData<List<PlannerName_DTO>>{
        return pnrepo.getAllPlanner()
    }

    fun getCurrentTaskForDate():LiveData<List<Calendar_DTO>>{
        return calitem
    }

    fun updateTask(item: Calendar_DTO) {
        CoroutineScope(Dispatchers.IO).launch {
            calrepo.updateTaskForDate(item)
        }
    }

    fun getAllPlan(): LiveData<List<Home_DTO>> {
        return planitem
    }

    fun getCurrentTodo(item: Long): List<TodoList_DTO>{
        return todorepo.getAllTodoList(item)
    }

    fun updateTodo(item: TodoList_DTO){
        CoroutineScope(Dispatchers.IO).launch {
            todorepo.updateTodo(item)
        }
    }

    fun setCalQuery(){
        calquery.value = LocalDate.now().toString()
    }

    fun setPlanQuery(idx: Long){
        planquery.value = idx
    }
}