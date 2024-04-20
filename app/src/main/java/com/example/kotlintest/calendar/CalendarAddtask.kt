package com.example.kotlintest.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kotlintest.databinding.FragmentCalendarAddtaskBinding
import com.example.kotlintest.util.TimePicker
import com.example.kotlintest.db.Calendar_DTO
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CalendarAddtask(val viewModel: CalendarVM) : BottomSheetDialogFragment() {
    private var _binding: FragmentCalendarAddtaskBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val selectedDateStr = arguments?.getString("date") ?: "" // 문자열로부터 날짜 문자열 가져옴
        _binding = FragmentCalendarAddtaskBinding.inflate(inflater, container, false)

        val tp = TimePicker(binding.setStarttimeBtn, binding.setEndtimeBtn)

        //종일 스위치 켜지면 타임피커 비활성화
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
                task = Calendar_DTO(date= selectedDateStr , task = binding.calTaskET.text.toString(), endtime = 1439, starttime = 0)
            }
            else {
                task = Calendar_DTO(date = selectedDateStr, task = binding.calTaskET.text.toString(), endtime = tp.getEndTime(), starttime = tp.getStartTime()
                )
            }
            viewModel.insertTaskForDate(task)
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