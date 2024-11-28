package com.schedulemanagersp.kotlintest.home

import android.content.Context
import android.graphics.Color
import com.schedulemanagersp.kotlintest.db.Calendar_DTO
import com.schedulemanagersp.kotlintest.db.Home_DTO
import com.schedulemanagersp.kotlintest.db.TodoList_DTO
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet

class HomeSharedAdapter(context: Context,
                        val listAdapter: HomeCalListAdapter,
                        val todoAdapter: HomeTodoAdapter,
                        piechart: PieChart){
    var mContext: Context
    var piechart: PieChart
    lateinit var viewModel: HomeVM

    init {
        mContext = context
        this.piechart = piechart
    }
    constructor(context: Context, piechart: PieChart, viewModel: HomeVM)
            : this(context, HomeCalListAdapter(context,viewModel), HomeTodoAdapter(context,viewModel), piechart){
                this.viewModel = viewModel
            }

    fun setHomelist(home: List<Home_DTO>) {
        viewModel.setPieItems(home)
        updatePieChart()
    }

    fun setCallist(cal: List<Calendar_DTO>) {
        listAdapter.items = cal
        listAdapter.notifyDataSetChanged()
    }

    fun setTodolist(todo: List<TodoList_DTO>) {
        todoAdapter.items = todo
        todoAdapter.notifyDataSetChanged()
    }

    private fun updatePieChart() {
        val dataSet = PieDataSet(viewModel.pieList, "")
        val pieData = PieData(dataSet)
        pieData.setValueTextSize(0f)

        if(viewModel.planitem.value!!.isNotEmpty()) {
            val min = viewModel.planitem.value!![0].starttime

            piechart.rotationAngle = -90f + (min * 0.25f)
        }
        piechart.isRotationEnabled = false
        piechart.data = pieData
        piechart.legend.isEnabled = false

        piechart.setEntryLabelColor(Color.BLACK)
        piechart.invalidate()

    }
}