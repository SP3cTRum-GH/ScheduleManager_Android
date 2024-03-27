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
import com.example.kotlintest.databinding.FragmentCalendarAddtaskBinding
import com.example.kotlintest.util.TimePicker
import com.example.kotlintest.db.AppDatabase
import com.example.kotlintest.db.Calendar_DTO
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.*

class CalendarAddtask(val cb: () -> Unit) : BottomSheetDialogFragment() {
    private var _binding: FragmentCalendarAddtaskBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val selectedDateStr = arguments?.getString("date") ?: "" // 문자열로부터 날짜 문자열 가져옴
        _binding = FragmentCalendarAddtaskBinding.inflate(inflater, container, false)


        // 데이터베이스 인스턴스 얻기
        val db = AppDatabase.getDatabase(requireContext())
        // DAO 초기화
        val calDao = db.calDao()

        val tp = TimePicker(binding.setStarttimeBtn, binding.setEndtimeBtn)

        binding.allDaysSwitch.setOnCheckedChangeListener { compoundButton, onSwitch ->
            if(onSwitch){
                binding.endTimeLinearLayout.visibility = View.GONE
                binding.startTimeLinearLayout.visibility = View.GONE
            }
            else{
                binding.endTimeLinearLayout.visibility = View.VISIBLE
                binding.startTimeLinearLayout.visibility = View.VISIBLE
            }
        }
        //시작시간
        binding.setStarttimeBtn.setOnClickListener {
            tp.setFlag(true)
            tp.show(parentFragmentManager,"time Picker")
        }

        //종료시간 시작시간보다 앞으로 갈수 없게끔 수정할 것
        binding.setEndtimeBtn.setOnClickListener {
            tp.setFlag(false)
            tp.show(parentFragmentManager,"time Picker")
        }

        //데이터베이스에 일정저장
        binding.calATSaveBtn.setOnClickListener {
            var task:Calendar_DTO
            if(binding.allDaysSwitch.isChecked){
                task = Calendar_DTO(date= selectedDateStr , task = binding.calTaskET.text.toString(), endtime = "23 : 59", starttime = "00 : 00")
            }
            else {
                task = Calendar_DTO(date = selectedDateStr, task = binding.calTaskET.text.toString(), endtime = tp.getEndTime(), starttime = tp.getStartTime()
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
        return binding.root
    }

    //메모리 누수 막기위해 뷰가없어질때 바인딩 해제
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}