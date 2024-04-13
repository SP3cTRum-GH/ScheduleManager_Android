package com.example.kotlintest.livedata

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LiveDataFactory(private val context: Context): ViewModelProvider.Factory{
    //필수
    override fun <T: ViewModel> create(modelClass: Class<T>):T {
        return if(modelClass.isAssignableFrom(PlannerLivedata::class.java)) {
            PlannerLivedata(context) as T
        }
        else if(modelClass.isAssignableFrom(CalLivedata::class.java)) {
            CalLivedata(context) as T
        }
        else {
            throw IllegalArgumentException("Unknown viewModel Class")
        }
    }
}