package com.example.kotlintest.settingplanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.kotlintest.R
import com.example.kotlintest.databinding.FragmentAddPlannerBinding
import com.example.kotlintest.db.AppDatabase
import com.example.kotlintest.db.PlannerName_DTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddPlanner(val cb: () -> Unit) : DialogFragment() {
    private var _binding: FragmentAddPlannerBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddPlannerBinding.inflate(inflater, container, false)

        // 데이터베이스 인스턴스 얻기
        val db = AppDatabase.getDatabase(requireContext())
        // DAO 초기화
        val plannerDao = db.plannerDao()

        binding.apCancelBtn.setOnClickListener{
            dismiss()
        }

        binding.apSubmitBtn.setOnClickListener{
            val plannerName = PlannerName_DTO(name = binding.plannerNameET.text.toString())

            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.IO) {
                    plannerDao.insertPlanner(plannerName)
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