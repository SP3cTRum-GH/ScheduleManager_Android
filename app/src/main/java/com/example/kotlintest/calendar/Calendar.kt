package com.example.kotlintest.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.ListView
import com.example.kotlintest.R
import com.example.kotlintest.db.AppDatabase
import com.example.kotlintest.db.Calendar_DAO
import com.example.kotlintest.db.Calendar_DTO
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import java.util.Calendar
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class Calendar : Fragment() {
//    private lateinit var adapter: ArrayAdapter<String> // 데이터 타입에 맞게 수정
    private lateinit var calDao: Calendar_DAO // Room DAO
    private var selectedDate = ""
    private lateinit var adapter: CalendarAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_calendar, container, false)
        var btn = view.findViewById<Button>(R.id.add_task)
        var cal = view.findViewById<CalendarView>(R.id.calendar)
        var listView = view.findViewById<ListView>(R.id.task)

        // Room 데이터베이스 인스턴스 생성
        val db = AppDatabase.getDatabase(requireContext())

        // Room DAO 초기화
        calDao = db.calDao()

        selectedDate = LocalDate.now().toString()
        // 데이터 가져오기
        var items:ArrayList<Calendar_DTO> = ArrayList()
        adapter = CalendarAdapter(requireContext(), items)
        listView.adapter = adapter

        loadDataFromDb(adapter)

        //선택한 날에 맞는 일정 불러오기
        cal.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            selectedDate = dateFormat.format(calendar.time)
            loadDataFromDb(adapter)
        }

        //일정추가 -> Addtask 화면 띄우기
        btn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("date", selectedDate)

            val addTask: AddTask = AddTask {
                loadDataFromDb(adapter)
            }
            addTask.arguments = bundle
            activity?.let { it1 -> addTask.show(it1.supportFragmentManager, addTask.tag) }
        }


        return view
    }

    private fun loadDataFromDb(adapter: CalendarAdapter) {
        val dataList = mutableListOf<Calendar_DTO>()

        // 백그라운드 스레드에서 실행
        Executors.newSingleThreadExecutor().execute {
            val data = calDao.getAllTaskForDate(selectedDate) // Room DAO에서 데이터 가져오기

            // 가져온 데이터 처리
            for (item in data) {
                dataList.add(item)
            }

            // UI 갱신
            requireActivity().runOnUiThread {
                adapter.clear()
                adapter.addAll(dataList)
                adapter.notifyDataSetChanged()
            }
        }
    }
}