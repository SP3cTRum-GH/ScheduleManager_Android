package com.example.kotlintest.settingplanner

import android.content.Context
import android.graphics.Color
import androidx.fragment.app.FragmentManager
import com.example.kotlintest.db.Home_DTO
import com.example.kotlintest.livedata.PlannerLivedata
import com.example.kotlintest.util.SwipeHendler
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet


class SharedAdapter(context: Context,
                    val listAdapter: SettingPlannerAdapter,
                    piechart: PieChart, plannerLivedata: PlannerLivedata
): SwipeHendler.OnItemMoveListener {
    var mContext: Context
    var piechart: PieChart
    var data: PlannerDataStructure
    var plannerLivedata: PlannerLivedata
    init {
        mContext = context
        this.piechart = piechart
        data = PlannerDataStructure()
        this.plannerLivedata = plannerLivedata
    }
    constructor(context: Context, piechart: PieChart, fm: FragmentManager,plannerLivedata: PlannerLivedata):
            this(context, SettingPlannerAdapter(fm,plannerLivedata), piechart, plannerLivedata) {
                this.plannerLivedata = plannerLivedata
            }

    fun setDatalist(home: ArrayList<Home_DTO>) {
        data.datalist = home
        updatePieChart()
        setAdapter()
    }

//    fun addData(adddata:Home_DTO){
//        data.addData(adddata)
//        setAdapter()
//    }

    private fun setAdapter() {
        listAdapter.items = data.datalist
        updatePieChart()
        allNotify()
    }

        private fun allNotify() {
            listAdapter.notifyDataSetChanged()
            piechart.invalidate()
        }

        private fun updatePieChart() {
            data.setPieItems()
            val dataSet = PieDataSet(data.pieList, "")
            val pieData = PieData(dataSet)
            pieData.setValueTextSize(0f)

            if(data.datalist.isNotEmpty()) {
                val min = data.datalist[0].starttime

                piechart.rotationAngle = -90f + (min * 0.25f)
            }
            piechart.data = pieData
            piechart.legend.isEnabled = false

            piechart.setEntryLabelColor(Color.BLACK)
            piechart.invalidate()

    }

    override fun swiped(position: Int) {
        var d = data.datalist[position]
        plannerLivedata.removePlan(d)
        setAdapter()
    }

}
