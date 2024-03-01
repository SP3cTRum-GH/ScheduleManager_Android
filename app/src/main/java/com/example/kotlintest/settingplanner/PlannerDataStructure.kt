package com.example.kotlintest.settingplanner

import com.example.kotlintest.db.Home_DTO
import com.example.kotlintest.home.Home
import com.github.mikephil.charting.data.PieEntry

class PlannerDataStructure {
    private var datalist: ArrayList<Home_DTO>
    private var blankdatalist: ArrayList<Home_DTO>
    private var pieList: ArrayList<PieEntry>//1min angle 0.25

    constructor(arg_list:ArrayList<Home_DTO>) {
        datalist = ArrayList(arg_list)
        sorter(datalist)
        blankdatalist = ArrayList(datalist)
        pieList = ArrayList()
        addBlank()
    }

    fun addData(data: Home_DTO) {
        datalist.add(data)
        sorter(datalist)
        blankdatalist = ArrayList(datalist)
        pieList = ArrayList()
        addBlank()
    }

    fun deleteData(data: Int) {
        datalist.removeAt(data)
        blankdatalist = ArrayList(datalist)
        pieList.clear()
        addBlank()
    }

    private fun makeBlank(i1: Int, i2: Int) {
        val end = blankdatalist[i1].endtime.split(" : ")
        val t1 = end[0].toInt() * 60 + end[1].toInt()

        val start = blankdatalist[i2].starttime.split(" : ")
        val t2 = start[0].toInt() * 60 + start[1].toInt()

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
            val end = i.endtime.split(" : ")
            var t1 = end[0].toInt() * 60 + end[1].toInt()

            val start = i.starttime.split(" : ")
            val t2 = start[0].toInt() * 60 + start[1].toInt()

            if(t2 >= t1) t1 += 1440
            val persent = (t1-t2)/1440f*100

            pieList.add(PieEntry(persent, i.task))
        }
        
    }

    private fun sorter(sortinglist:ArrayList<Home_DTO>) {
        sortinglist.sortBy { it.starttime }
    }

    fun getBlankdataList(): ArrayList<Home_DTO> {
        return blankdatalist
    }

    fun getDatalist(): ArrayList<Home_DTO> {
        return datalist
    }

    fun getPieList(): ArrayList<PieEntry> {
        return pieList
    }
}