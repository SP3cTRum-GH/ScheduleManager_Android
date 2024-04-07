package com.example.kotlintest.settingplanner

import android.content.Context
import android.graphics.Color
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.kotlintest.db.AppDatabase
import com.example.kotlintest.db.Home_DTO
import com.example.kotlintest.util.SwipeHendler
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class SharedAdapter :SwipeHendler.OnItemMoveListener {
//    private lateinit var dragStart
    private var db: AppDatabase
    private var mContext: Context
    private var listAdapter: SettingPlannerAdapter
    private var idx: Long
    private var piechart: PieChart
    private var fm: FragmentManager
    private lateinit var data: PlannerDataStructure

    constructor(context: Context, idx: Long, piechart: PieChart, fm: FragmentManager) {
        this.mContext = context
        this.db = AppDatabase.getDatabase(mContext)
        this.idx = idx
        this.piechart = piechart
        this.fm = fm

        this.listAdapter = SettingPlannerAdapter(fm)
        loadDataFromDb()
    }

    private fun loadDataFromDb() {
        CoroutineScope(Dispatchers.Main).launch {
            var tmp: ArrayList<Home_DTO> = ArrayList()
            val ret = CoroutineScope(Dispatchers.IO).async {
                tmp.addAll(db.homeDao().getAllPlanner(idx))
            }
            ret.await()
            data = PlannerDataStructure(tmp)
            setAdapter()
        }
    }
    fun addData(adddata:Home_DTO){
        data.addData(adddata)
        setAdapter()
    }

    fun getListAdapter() : SettingPlannerAdapter {
        return listAdapter
    }

    private fun setAdapter() {
        val listitem = data.getDatalist()
        listAdapter.items = listitem
        val dataSet = PieDataSet(data.getPieList(), "")
        val data = PieData(dataSet)
        data.setValueTextSize(0f)
        if(listitem.isNotEmpty()) {
            val min = listitem[0].starttime
//            val min = start[0].toInt() * 60 + start[1].toInt()

            piechart.rotationAngle = -90f + (min * 0.25f)
        }
        piechart.data = data
        piechart.legend.isEnabled = false

        piechart.setEntryLabelColor(Color.BLACK)

        allNotify()
    }

    private fun allNotify() {
        listAdapter.notifyDataSetChanged()
        piechart.invalidate()
    }

    override fun swiped(position: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            var d = data.getDatalist()[position]
            data.deleteData(position)

            CoroutineScope(Dispatchers.IO).async {
                db.homeDao().deletePlanner(d)
                db.todoDao().deleteAllTodo(d.index)
            }
            setAdapter()
        }
    }
}
