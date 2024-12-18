package com.schedulemanagersp.kotlintest.calendar

import android.app.Application
import androidx.lifecycle.*
import com.schedulemanagersp.kotlintest.db.Calendar_DTO
import com.schedulemanagersp.kotlintest.repository.Calrepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CalendarVM(app: Application) : AndroidViewModel(app){
    private val repo = Calrepo(app)
    val item : LiveData<List<Calendar_DTO>>
    val query = MutableLiveData<String>()

    init {
        item = Transformations.switchMap(query) { query ->
            repo.getAllTaskForDate(query)
        }
    }

    class Factory(val app: Application): ViewModelProvider.Factory{
        override fun <T: ViewModel> create(modelClass: Class<T>):T {
            return CalendarVM(app) as T
        }
    }

    fun getAllTaskForDate():LiveData<List<Calendar_DTO>>{
        return item
    }

    fun removeCalendarTable(item: Calendar_DTO) {
        CoroutineScope(Dispatchers.IO).launch {
            repo.removeCalendarTable(item)
        }
    }

    fun insertTaskForDate(item: Calendar_DTO) {
        CoroutineScope(Dispatchers.IO).launch {
            repo.insertTaskForDate(item)
        }
    }

    fun updateTaskForDate(item: Calendar_DTO) {
        CoroutineScope(Dispatchers.IO).launch {
            repo.updateTaskForDate(item)
        }
    }

    fun setQuery(date: String){
        query.value = date
    }
}