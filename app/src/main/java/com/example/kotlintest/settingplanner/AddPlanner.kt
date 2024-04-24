package com.example.kotlintest.settingplanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import com.example.kotlintest.databinding.FragmentAddPlannerBinding
import com.example.kotlintest.db.PlannerName_DTO

class AddPlanner(val viewModel: PlannerVM) : DialogFragment() {
    private var _binding: FragmentAddPlannerBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddPlannerBinding.inflate(inflater, container, false)

        binding.apCancelBtn.setOnClickListener{
            dismiss()
        }

        binding.apSubmitBtn.setOnClickListener{
            val plannerName = PlannerName_DTO(name = binding.plannerNameET.text.toString())
            viewModel.insertPlanner(plannerName)
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