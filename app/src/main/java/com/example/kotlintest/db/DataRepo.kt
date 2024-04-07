package com.example.kotlintest.db

import androidx.lifecycle.MutableLiveData
import com.github.mikephil.charting.data.PieEntry
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DataRepo(var _homelist: MutableLiveData<ArrayList<Home_DTO>>,
               var _callist: MutableLiveData<ArrayList<Calendar_DTO>>,
               var _todolist: MutableLiveData<ArrayList<TodoList_DTO>>) {

    constructor() : this(MutableLiveData(ArrayList()), MutableLiveData(ArrayList()), MutableLiveData(ArrayList()))

}