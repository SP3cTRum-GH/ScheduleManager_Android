package com.example.kotlintest.settingplanner

import android.app.Application
import androidx.lifecycle.*
import com.example.kotlintest.db.Home_DTO
import com.example.kotlintest.db.PlannerName_DTO
import com.example.kotlintest.db.TodoList_DTO
import com.example.kotlintest.repository.Homerepo
import com.example.kotlintest.repository.PNrepo
import com.example.kotlintest.repository.Todorepo
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class PlannerVM(app:Application): AndroidViewModel(app) {
    private val pnrepo = PNrepo(app)
    private val planrepo = Homerepo(app)
    private val todorepo = Todorepo(app)
    val pnitem : LiveData<List<PlannerName_DTO>>
    val planitem: LiveData<List<Home_DTO>>
    val todoitem: LiveData<List<TodoList_DTO>>
    val planquery = MutableLiveData<Long>()
    val todoquery = MutableLiveData<Long>()
    var blankdatalist: ArrayList<Home_DTO>
    var pieList: ArrayList<PieEntry>//1min angle 0.25

    init {
        blankdatalist = ArrayList()
        pieList = ArrayList()
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

    fun sorter(sortinglist:List<Home_DTO>){
        Collections.sort(sortinglist, { o1, o2 -> o1.starttime - o2.starttime })
    }
    private fun addBlank() {
        for(i in 0 until blankdatalist.size - 1) {
            makeBlank(i, i + 1)
        }
        sorter(blankdatalist)
        if(blankdatalist.isNotEmpty()) {
            makeBlank(blankdatalist.size - 1, 0)
        }

        for(i in blankdatalist){
            var t1 = i.endtime
            var t2 = i.starttime

            if(t2 >= t1) t1 += 1440
            val persent = (t1-t2)/1440f*100

            pieList.add(PieEntry(persent, i.task))
        }
    }

    private fun makeBlank(i1: Int, i2: Int) {
        val t1 = blankdatalist[i1].endtime
        val t2 = blankdatalist[i2].starttime

        if(t1 - t2 != 0) {
            blankdatalist.add(Home_DTO(starttime = blankdatalist[i1].endtime, endtime = blankdatalist[i2].starttime, task = "", name = -1))
        }
    }
    fun setPieItems(item:List<Home_DTO>): ArrayList<PieEntry> {
        blankdatalist = ArrayList(item)
        pieList = ArrayList()
        addBlank()
        return pieList
    }
}