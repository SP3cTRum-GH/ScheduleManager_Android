package com.example.kotlintest.livedata

import androidx.lifecycle.MutableLiveData
import com.example.kotlintest.db.Calendar_DTO
import com.example.kotlintest.db.Home_DTO
import com.example.kotlintest.db.PlannerName_DTO
import com.example.kotlintest.db.TodoList_DTO

class DataRepo(var _homelist: MutableLiveData<ArrayList<Home_DTO>>,
               var _callist: MutableLiveData<ArrayList<Calendar_DTO>>,
               var _todolist: MutableLiveData<ArrayList<TodoList_DTO>>,
               var _plannerNamelist: MutableLiveData<ArrayList<PlannerName_DTO>>) {

    constructor() : this(MutableLiveData(ArrayList()), MutableLiveData(ArrayList()), MutableLiveData(ArrayList()),MutableLiveData(ArrayList()))

}