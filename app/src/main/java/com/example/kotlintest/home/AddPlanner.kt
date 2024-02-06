package com.example.kotlintest.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.kotlintest.R
import com.example.kotlintest.util.TimePicker

//interface ToggleVisibility {
//    fun toggle()
//}

class AddPlanner : Fragment() {
//    var inter: ToggleVisibility
//    constructor(inter: ToggleVisibility){
//        this.inter = inter
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_planner, container, false)

        val btnStart = view.findViewById<Button>(R.id.timeset_start)
        val btnEnd = view.findViewById<Button>(R.id.timeset_end)

        val button = view.findViewById<Button>(R.id.btnSave)
        button.setOnClickListener{
            parentFragmentManager.beginTransaction().remove(this).commit()
//            this.inter.toggle()
        }

        val tp = TimePicker(btnStart, btnEnd)

        //시작시간
        btnStart.setOnClickListener {
            tp.setFlag(true)
            tp.show(parentFragmentManager,"time Picker")
        }

        //종료시간 시작시간보다 앞으로 갈수 없게끔 수정할 것
        btnEnd.setOnClickListener {
            tp.setFlag(false)
            tp.show(parentFragmentManager,"time Picker")
        }


        return view
    }
}