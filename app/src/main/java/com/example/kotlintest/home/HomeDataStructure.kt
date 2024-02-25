package com.example.kotlintest.home

import com.example.kotlintest.db.Calendar_DTO
import com.example.kotlintest.db.Home_DTO
import com.example.kotlintest.db.TodoList_DTO
import com.github.mikephil.charting.data.PieEntry
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeDataStructure {
    private var homelist: ArrayList<Home_DTO>
    private var callist: ArrayList<Calendar_DTO>
    private var todolist: ArrayList<TodoList_DTO>
    private var homeCalTodoList: ArrayList<Calendar_DTO>
    private var blankdatalist: ArrayList<Home_DTO>
    private var pieList: ArrayList<PieEntry>//1min angle 0.25

    constructor(home:ArrayList<Home_DTO>, calendar:ArrayList<Calendar_DTO>, todo:ArrayList<TodoList_DTO>) {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH : mm")
        val formatted = current.format(formatter).toString().split(" : ")
        val t = formatted[0].toInt() * 60 + formatted[1].toInt()

        homelist = home
        sorter(homelist)
        blankdatalist = ArrayList(homelist)
        callist = calendar
        homeCalTodoList = ArrayList()
        caltodoCurrentTime(callist,t)
        todolist = todo
        pieList = ArrayList()
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

    private fun caltodoCurrentTime(list:ArrayList<Calendar_DTO>,t:Int){
        for(i in list){
            val start = i.starttime.split(" : ")
            val t1 = start[0].toInt() * 60 + start[1].toInt()

            val end = i.endtime.split(" : ")
            var t2 = end[0].toInt() * 60 + end[1].toInt()

            if(t1<=t && t<=t2){
                homeCalTodoList.add(i)
            }
        }
    }

    private fun sorter(sortinglist:ArrayList<Home_DTO>) {
        sortinglist.sortBy { it.starttime }
    }

    fun getPieItems(): ArrayList<Home_DTO>{
        return homelist
    }

    fun getDatalist(): ArrayList<Calendar_DTO> {
        return homeCalTodoList
    }

    fun getTodoList(): ArrayList<TodoList_DTO>{
        return todolist
    }

    fun getPieList(): ArrayList<PieEntry> {
        return pieList
    }
}