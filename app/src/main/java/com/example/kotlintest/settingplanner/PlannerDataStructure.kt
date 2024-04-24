package com.example.kotlintest.settingplanner

import com.example.kotlintest.db.Calendar_DTO
import com.example.kotlintest.db.Home_DTO
import com.example.kotlintest.db.TodoList_DTO
import com.example.kotlintest.home.Home
import com.github.mikephil.charting.data.PieEntry
import java.util.*
import kotlin.collections.ArrayList

class PlannerDataStructure(var datalist: List<Home_DTO>){
    var blankdatalist: ArrayList<Home_DTO>
    var pieList: ArrayList<PieEntry>//1min angle 0.25

    init {
        blankdatalist = ArrayList()
        pieList = ArrayList()
    }
    constructor(): this(ArrayList())

//    fun deleteData(data: Int) {
//        datalist.removeAt(data)
//        blankdatalist = ArrayList(datalist)
//        pieList.clear()
//        addBlank()
//    }

    private fun makeBlank(i1: Int, i2: Int) {
        val t1 = blankdatalist[i1].endtime
//        val t1 = end[0].toInt() * 60 + end[1].toInt()

        val t2 = blankdatalist[i2].starttime
//        val t2 = start[0].toInt() * 60 + start[1].toInt()

        if(t1 - t2 != 0) {
            blankdatalist.add(Home_DTO(starttime = blankdatalist[i1].endtime, endtime = blankdatalist[i2].starttime, task = "", name = -1))
        }
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

    private fun sorter(sortinglist:List<Home_DTO>) {
        Collections.sort(sortinglist, { o1, o2 -> o1.starttime - o2.starttime })
    }

    fun setPieItems() {
        sorter(datalist)
        blankdatalist = ArrayList(datalist)
        pieList = ArrayList()
        addBlank()
    }
}