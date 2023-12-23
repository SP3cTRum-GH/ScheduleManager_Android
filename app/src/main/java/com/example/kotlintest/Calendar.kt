package com.example.kotlintest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class Calendar : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_calendar, container, false)

        var btn = view.findViewById<Button>(R.id.add_task)
        btn.setOnClickListener {
            val addTask = AddTask()
            activity?.let { it1 -> addTask.show(it1.supportFragmentManager, addTask.tag) }
        }

        return view
    }

}