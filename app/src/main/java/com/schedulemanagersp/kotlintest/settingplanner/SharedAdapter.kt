package com.schedulemanagersp.kotlintest.settingplanner

import android.content.Context
import android.graphics.Color
import androidx.fragment.app.FragmentManager
import com.schedulemanagersp.kotlintest.db.Home_DTO
import com.schedulemanagersp.kotlintest.util.SwipeHendler
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry


class SharedAdapter(context: Context,
                    val listAdapter: SettingPlannerAdapter,
                    piechart: PieChart, viewModel: PlannerVM
): SwipeHendler.OnItemMoveListener {
    var mContext: Context
    var piechart: PieChart
    var viewModel: PlannerVM
    init {
        mContext = context
        this.piechart = piechart
        this.viewModel = viewModel
    }
    constructor(context: Context, piechart: PieChart, fm: FragmentManager,viewModel: PlannerVM):
            this(context, SettingPlannerAdapter(fm,viewModel), piechart, viewModel) {
                this.viewModel = viewModel
            }

    fun setDatalist(home: List<Home_DTO>) {
        viewModel.sorter(home)
        listAdapter.addAll(home)
        var piItem = viewModel.setPieItems(home)
        updatePieChart(home, piItem)
        listAdapter.notifyDataSetChanged()
    }

//    fun addData(adddata:Home_DTO){
//        data.addData(adddata)
//        setAdapter()
//    }

        private fun updatePieChart(home: List<Home_DTO>, piItem:ArrayList<PieEntry>) {
            val dataSet = PieDataSet(piItem, "")
            val pieData = PieData(dataSet)
            pieData.setValueTextSize(0f)

            if(home.isNotEmpty()) {
                val min = home[0].starttime

                piechart.rotationAngle = -90f + (min * 0.25f)
            }
            piechart.isRotationEnabled = false
            piechart.data = pieData
            piechart.legend.isEnabled = false
            piechart.setEntryLabelColor(Color.BLACK)

            piechart.invalidate()

    }

    override fun swiped(position: Int) {
        val d = listAdapter.items[position]
        viewModel.removePlan(d)
    }

}
