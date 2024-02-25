package com.example.kotlintest.home

import android.content.Context
import android.graphics.Color
import com.example.kotlintest.db.AppDatabase
import com.example.kotlintest.db.Calendar_DTO
import com.example.kotlintest.db.Home_DTO
import com.example.kotlintest.db.TodoList_DTO
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeSharedAdapter{
    var idx: Long = -1
    private var db: AppDatabase
    private var mContext: Context
    private var listAdapter: HomeCalListAdapter
    private var todoAdapter: HomeTodoAdapter
    private var piechart: PieChart
    private lateinit var data: HomeDataStructure

    constructor(context: Context, piechart: PieChart) {
        this.mContext = context
        this.db = AppDatabase.getDatabase(mContext)
        this.piechart = piechart
        this.listAdapter = HomeCalListAdapter(mContext)
        this.todoAdapter = HomeTodoAdapter(mContext)

        loadDataFromDb()
    }

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
                home.addAll(db.homeDao().getAllPlanner(idx))
                cal.addAll(db.calDao().getAllTaskForDate(LocalDate.now().toString()))

                for(i in home){
                    val start = i.starttime.split(" : ")
                    val t1 = start[0].toInt() * 60 + start[1].toInt()

                    val end = i.endtime.split(" : ")
                    var t2 = end[0].toInt() * 60 + end[1].toInt()

                    if(t1<=t && t<=t2){
                        todoindex = i.index
                        break
                    }
                }

                if(home.isNotEmpty()) {
                    todo.addAll(db.todoDao().getAllTodo(todoindex))
                }
            }
            ret.await()
            data = HomeDataStructure(home,cal,todo)
            setAdapter()
        }
    }

    fun getListAdapter() : HomeCalListAdapter {
        return listAdapter
    }

    fun getTodoAdapter() : HomeTodoAdapter {
        return todoAdapter
    }

    private fun setAdapter() {
        val listitem = data.getDatalist()
        val pieitem = data.getPieItems()
        val todoitem = data.getTodoList()

        listAdapter.items = listitem
        todoAdapter.items = todoitem
        val dataSet = PieDataSet(data.getPieList(), "")
        val data = PieData(dataSet)
        data.setValueTextSize(0f)
        if(pieitem.isNotEmpty()) {
            val start = pieitem[0].starttime.split(" : ")
            val min = start[0].toInt() * 60 + start[1].toInt()

            piechart.rotationAngle = -90f + (min * 0.25f)
        }
        piechart.data = data
        piechart.legend.isEnabled = false

        piechart.setEntryLabelColor(Color.BLACK)

        allNotify()
    }

    private fun allNotify() {
        piechart.invalidate()
        listAdapter.notifyDataSetChanged()
        todoAdapter.notifyDataSetChanged()
    }
}