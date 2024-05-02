package com.example.kotlintest.home

import android.app.Application
import androidx.lifecycle.*
import com.example.kotlintest.db.Calendar_DTO
import com.example.kotlintest.db.Home_DTO
import com.example.kotlintest.db.PlannerName_DTO
import com.example.kotlintest.db.TodoList_DTO
import com.example.kotlintest.repository.Calrepo
import com.example.kotlintest.repository.Homerepo
import com.example.kotlintest.repository.PNrepo
import com.example.kotlintest.repository.Todorepo
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

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
    var blankdatalist: ArrayList<Home_DTO>
    var pieList: ArrayList<PieEntry>//1min angle 0.25

    init {
        blankdatalist = ArrayList()
        pieList = ArrayList()
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

    private fun sorter(sortinglist:List<Home_DTO>) {
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
//            var t1 = end[0].toInt() * 60 + end[1].toInt()

            var t2 = i.starttime
//            val t2 = start[0].toInt() * 60 + start[1].toInt()

            if(t2 >= t1) t1 += 1440
            val persent = (t1-t2)/1440f*100

            pieList.add(PieEntry(persent, i.task))
        }
    }

    private fun makeBlank(i1: Int, i2: Int) {
        val t1 = blankdatalist[i1].endtime
//        val t1 = end[0].toInt() * 60 + end[1].toInt()

        val t2 = blankdatalist[i2].starttime
//        val t2 = start[0].toInt() * 60 + start[1].toInt()

        if(t1 - t2 != 0) {
            blankdatalist.add(Home_DTO(starttime = blankdatalist[i1].endtime, endtime = blankdatalist[i2].starttime, task = "", name = -1))
        }
    }
    fun setPieItems(item:List<Home_DTO>) {
        sorter(item)
        blankdatalist = ArrayList(item)
        pieList = ArrayList()
        addBlank()
    }
}