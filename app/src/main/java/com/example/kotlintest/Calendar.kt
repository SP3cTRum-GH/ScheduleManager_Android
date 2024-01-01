package com.example.kotlintest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.ListView
import androidx.room.Dao
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.Calendar
import java.util.concurrent.Executors

class Calendar : Fragment() {
    private lateinit var adapter: ArrayAdapter<String> // 데이터 타입에 맞게 수정
    private lateinit var calDao: DAO // Room DAO
    private var selectedDate = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_calendar, container, false)
        var btn = view.findViewById<Button>(R.id.add_task)
        var cal = view.findViewById<CalendarView>(R.id.calendar)
        var listView = view.findViewById<ListView>(R.id.task)
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1) // 데이터 타입에 맞게 수정

        listView.adapter = adapter

        // Room 데이터베이스 인스턴스 생성
        val db = AppDatabase.getDatabase(requireContext())

        // Room DAO 초기화
        calDao = db.calDao()

        // 데이터 가져오기
        loadDataFromDb()


        cal.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            selectedDate = dateFormat.format(calendar.time)
            println(selectedDate)
            loadDataFromDb()
        }

        btn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("date", selectedDate)

            val addTask = AddTask()
            addTask.arguments = bundle
            activity?.let { it1 -> addTask.show(it1.supportFragmentManager, addTask.tag) }
        }


        return view
    }

    private fun loadDataFromDb() {
        val dataList = mutableListOf<String>() // 데이터 타입에 맞게 수정

        // 백그라운드 스레드에서 실행
        Executors.newSingleThreadExecutor().execute {
            val data = calDao.getAllTaskForDate(selectedDate) // Room DAO에서 데이터 가져오기

            // 가져온 데이터 처리
            for (item in data) {
                dataList.add(item.task)
            }

            // UI 갱신
            requireActivity().runOnUiThread {
                adapter.clear()
                adapter.addAll(dataList)
            }
        }
    }
}