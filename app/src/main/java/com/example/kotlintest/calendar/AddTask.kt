package com.example.kotlintest.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Switch
import com.example.kotlintest.R
import com.example.kotlintest.util.TimePicker
import com.example.kotlintest.db.AppDatabase
import com.example.kotlintest.db.Calendar_DTO
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.*

class AddTask(val cb: () -> Unit) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val selectedDateStr = arguments?.getString("date") ?: "" // 문자열로부터 날짜 문자열 가져옴
        val view = inflater.inflate(R.layout.fragment_add_task, container, false)
        val btnStart = view.findViewById<Button>(R.id.timeset_start)
        val btnEnd = view.findViewById<Button>(R.id.timeset_end)
        val btnSave = view.findViewById<Button>(R.id.btn_save)
        val txtTask = view.findViewById<EditText>(R.id.task)
        val allDayButton = view.findViewById<Switch>(R.id.allDays)
        val startTimeArea = view.findViewById<LinearLayout>(R.id.startTimeLinearLayout)
        val endTimeArea = view.findViewById<LinearLayout>(R.id.endTimeLinearLayout)

        // 데이터베이스 인스턴스 얻기
        val db = AppDatabase.getDatabase(requireContext())
        // DAO 초기화
        val calDao = db.calDao()

        val tp = TimePicker(btnStart, btnEnd)

        allDayButton.setOnCheckedChangeListener { compoundButton, onSwitch ->
            if(onSwitch){
                endTimeArea.visibility = View.GONE
                startTimeArea.visibility = View.GONE
            }
            else{
                endTimeArea.visibility = View.VISIBLE
                startTimeArea.visibility = View.VISIBLE
            }
        }
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

        //데이터베이스에 일정저장
        btnSave.setOnClickListener {
            var task:Calendar_DTO
            if(allDayButton.isChecked){
                task = Calendar_DTO(date= selectedDateStr , task = txtTask.text.toString(), endtime = "23 : 59", starttime = "00 : 00")
            }
            else {
                task = Calendar_DTO(date = selectedDateStr, task = txtTask.text.toString(), endtime = tp.getEndTime(), starttime = tp.getStartTime()
                )
            }
//            CoroutineScope(Dispatchers.Main).launch {
//                val res = async(Dispatchers.IO) {
//                    calDao.insertTaskForDate(task)
//                }
//                println(res)
//            }

            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.IO) {
                    calDao.insertTaskForDate(task)
                    cb()
                }
            }

            dismiss()
        }
        return view
    }
}