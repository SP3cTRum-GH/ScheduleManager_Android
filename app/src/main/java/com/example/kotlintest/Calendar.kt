package com.example.kotlintest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.Calendar

class Calendar : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_calendar, container, false)
        var btn = view.findViewById<Button>(R.id.add_task)
        var cal = view.findViewById<CalendarView>(R.id.calendar)
        var selectedDate = ""
        cal.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            selectedDate = dateFormat.format(calendar.time)
            println(selectedDate)
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

}