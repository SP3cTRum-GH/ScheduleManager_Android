package com.schedulemanagersp.kotlintest.settingplanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.schedulemanagersp.kotlintest.databinding.FragmentAddPlanBinding
import com.schedulemanagersp.kotlintest.db.*
import com.schedulemanagersp.kotlintest.util.TimePicker

class AddPlan(val idx:Long,val viewModel: PlannerVM) : DialogFragment() {
    private var _binding: FragmentAddPlanBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddPlanBinding.inflate(inflater, container, false)

        val tp = TimePicker(binding.planStarttimeBtn, binding.planEndtimeBtn)

        //시작시간
        binding.planStarttimeBtn.setOnClickListener {
            tp.setFlag(true)
            tp.show(parentFragmentManager,"time Picker")
        }

        //종료시간 시작시간보다 앞으로 갈수 없게끔 수정할 것
        binding.planEndtimeBtn.setOnClickListener {
            tp.setFlag(false)
            tp.show(parentFragmentManager,"time Picker")
        }

        binding.planSaveBtn.setOnClickListener{
            val data = Home_DTO(starttime = tp.getStartTime(), endtime = tp.getEndTime(), task = binding.planET.text.toString(), name = idx)
            viewModel.insertPlan(data)
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