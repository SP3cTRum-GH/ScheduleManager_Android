package com.example.kotlintest.home

import com.example.kotlintest.db.Calendar_DTO
import com.example.kotlintest.db.Home_DTO
import com.example.kotlintest.db.TodoList_DTO
import com.github.mikephil.charting.data.PieEntry
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeDataStructure(var homelist: ArrayList<Home_DTO>,
                        var callist: ArrayList<Calendar_DTO>,
                        var todolist: ArrayList<TodoList_DTO>,
                        ){
    var blankdatalist: ArrayList<Home_DTO>
    var homeCalTodoList: ArrayList<Calendar_DTO>
    var pieList: ArrayList<PieEntry>//1min angle 0.25

    init {
        blankdatalist = ArrayList()
        homeCalTodoList = ArrayList()
        pieList = ArrayList()
    }
    constructor() : this(ArrayList(),ArrayList(),ArrayList())

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

    private fun caltodoCurrentTime(list:ArrayList<Calendar_DTO>,t:Int){
        for(i in list){
            var t1 = i.starttime
//            val t1 = start[0].toInt() * 60 + start[1].toInt()

            var t2 = i.endtime
//            var t2 = end[0].toInt() * 60 + end[1].toInt()

            if(t1<=t && t<=t2){
                homeCalTodoList.add(i)
            }
        }
    }

    private fun sorter(sortinglist:ArrayList<Home_DTO>) {
        sortinglist.sortBy { it.starttime }
    }

    fun setPieItems() {
        sorter(homelist)
        blankdatalist = ArrayList(homelist)
        pieList = ArrayList()
        addBlank()
    }
}