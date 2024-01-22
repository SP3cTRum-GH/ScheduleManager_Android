package com.example.kotlintest

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.*
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

class AddTask(val cb: () -> Unit) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val selectedDateStr = arguments?.getString("date") ?: "" // 문자열로부터 날짜 문자열 가져옴
        var view = inflater.inflate(R.layout.fragment_add_task, container, false)
        var btnStart = view.findViewById<Button>(R.id.timeset_start)
        var btnEnd = view.findViewById<Button>(R.id.timeset_end)
        var btnSave = view.findViewById<Button>(R.id.btn_save)
        var txtTask = view.findViewById<EditText>(R.id.task)

        // 데이터베이스 인스턴스 얻기
        val db = AppDatabase.getDatabase(requireContext())
        // DAO 초기화
        val calDao = db.calDao()

        val tp = TimePicker(btnStart, btnEnd)

        btnStart.setOnClickListener {
            tp.setFlag(true)
            tp.show(parentFragmentManager,"time Picker")
        }

        //종료시간 시작시간보다 앞으로 갈수 없게끔 수정할 것
        btnEnd.setOnClickListener {
            tp.setFlag(false)
            tp.show(parentFragmentManager,"time Picker")
        }

        btnSave.setOnClickListener {
            val task = Calendar_DTO(date= selectedDateStr , task = txtTask.text.toString(), endtime = tp.getEndTime(), starttime = tp.getStartTime())
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