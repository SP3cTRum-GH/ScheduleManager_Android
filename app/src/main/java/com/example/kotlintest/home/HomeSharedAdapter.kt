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

class HomeSharedAdapter(context: Context,
                        val listAdapter: HomeCalListAdapter,
                        val todoAdapter: HomeTodoAdapter,
                        piechart: PieChart){
    var idx: Long = -1
    var mContext: Context
    var piechart: PieChart
    var data: HomeDataStructure

    init {
        mContext = context
        this.piechart = piechart
        data = HomeDataStructure()
    }
    constructor(context: Context, piechart: PieChart)
            : this(context, HomeCalListAdapter(context), HomeTodoAdapter(context), piechart)

    fun setHomelist(home: ArrayList<Home_DTO>) {
        data.homelist = home
        updatePieChart()
    }

    fun setCallist(cal: ArrayList<Calendar_DTO>) {
        data.callist = cal
        setAdapter()
    }

    fun setTodolist(todo: ArrayList<TodoList_DTO>) {
        data.todolist = todo
        setAdapter()
    }

    private fun updatePieChart() {
        data.setPieItems()
        val dataSet = PieDataSet(data.pieList, "")
        val pieData = PieData(dataSet)
        pieData.setValueTextSize(0f)

        if(data.homelist.isNotEmpty()) {
            val min = data.homelist[0].starttime

            piechart.rotationAngle = -90f + (min * 0.25f)
        }
        piechart.data = pieData
        piechart.legend.isEnabled = false

        piechart.setEntryLabelColor(Color.BLACK)
        piechart.invalidate()

    }
    private fun setAdapter() {
        listAdapter.items = data.callist
        todoAdapter.items = data.todolist

        allNotify()
    }

    private fun allNotify() {
        listAdapter.notifyDataSetChanged()
        todoAdapter.notifyDataSetChanged()
    }
}