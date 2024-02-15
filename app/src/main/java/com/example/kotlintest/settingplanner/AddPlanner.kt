package com.example.kotlintest.settingplanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.kotlintest.R
import com.example.kotlintest.db.*
import com.example.kotlintest.home.HomeAdapter
import com.example.kotlintest.util.TimePicker
import kotlinx.coroutines.*

class AddPlanner(val cb: (data: Home_DTO) -> Unit, val idx:Long) : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_planner, container, false)
        val btnStart = view.findViewById<Button>(R.id.timeset_start)
        val btnEnd = view.findViewById<Button>(R.id.timeset_end)
        val button = view.findViewById<Button>(R.id.btnSave)
        val task = view.findViewById<EditText>(R.id.editTask)
        val tp = TimePicker(btnStart, btnEnd)

        // 데이터베이스 인스턴스 얻기
        val db = AppDatabase.getDatabase(requireContext())
        // DAO 초기화
        val homeDao = db.homeDao()

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

        button.setOnClickListener{
            val data = Home_DTO(starttime = tp.getStartTime(), endtime =tp.getEndTime(), task = task.text.toString(), name = idx)
            CoroutineScope(Dispatchers.Main).launch {
                var ret = CoroutineScope(Dispatchers.IO).async {
                    homeDao.insertPlanner(data)
                }

                ret.await()
                cb(data)
            }
            dismiss()
        }
        return view
    }
}