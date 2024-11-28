package com.schedulemanagersp.kotlintest.settingplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.schedulemanagersp.kotlintest.databinding.FragmentDetailPlannerBinding
import com.schedulemanagersp.kotlintest.db.*
import com.schedulemanagersp.kotlintest.util.SwipeHendler

class DetailPlanner(val plannerinfo:PlannerName_DTO, val viewModel: PlannerVM) : Fragment() {
    private var _binding: FragmentDetailPlannerBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedAdapter: SharedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailPlannerBinding.inflate(inflater, container, false)

        binding.dpPlannerChart.centerText = plannerinfo.name
        binding.dpPlannerChart.setCenterTextSize(20f)

        // 데이터 가져오기
        sharedAdapter = SharedAdapter(requireContext(), binding.dpPlannerChart, parentFragmentManager, viewModel)
        binding.detailPlanList.adapter = sharedAdapter.listAdapter
        binding.detailPlanList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        viewModel.setPlanQuery(plannerinfo.index)
        viewModel.getAllPlan().observe(viewLifecycleOwner, Observer {
            sharedAdapter.setDatalist(it)
        })

        val l = ItemTouchHelper(SwipeHendler(sharedAdapter))
        l.attachToRecyclerView(binding.detailPlanList)

//      일정추가
        binding.dpAddPlanBtn.setOnClickListener{
            val addSchedule = AddPlan(plannerinfo.index,viewModel)
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
//            transaction.add(EditPlannerFragment(), EditPlannerFragment().tag).commit()
            addSchedule.show(transaction,addSchedule.tag)
        }

        return binding.root
    }
    //메모리 누수 막기위해 뷰가없어질때 바인딩 해제
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}