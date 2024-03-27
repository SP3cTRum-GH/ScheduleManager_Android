package com.example.kotlintest.settingplanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.kotlintest.R
import com.example.kotlintest.databinding.FragmentAddPlanBinding
import com.example.kotlintest.db.*
import com.example.kotlintest.util.TimePicker
import kotlinx.coroutines.*

class AddPlan(val cb: (data: Home_DTO) -> Unit, val idx:Long) : DialogFragment() {
    private var _binding: FragmentAddPlanBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddPlanBinding.inflate(inflater, container, false)

        val tp = TimePicker(binding.planStarttimeBtn, binding.planEndtimeBtn)

        // 데이터베이스 인스턴스 얻기
        val db = AppDatabase.getDatabase(requireContext())
        // DAO 초기화
        val homeDao = db.homeDao()

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
            CoroutineScope(Dispatchers.Main).launch {
                var ret = CoroutineScope(Dispatchers.IO).async {
                    homeDao.insertPlanner(data)
                }
                ret.await()
                cb(data)
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