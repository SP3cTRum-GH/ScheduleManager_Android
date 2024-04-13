package com.example.kotlintest.settingplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlintest.databinding.FragmentDetailPlannerBinding
import com.example.kotlintest.db.*
import com.example.kotlintest.livedata.PlannerLivedata
import com.example.kotlintest.util.SwipeHendler

class DetailPlanner(val plannerinfo:PlannerName_DTO, val plannerLivedata: PlannerLivedata) : Fragment() {
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

        plannerLivedata.getAllPlan(plannerinfo.index)
        // 데이터 가져오기
        sharedAdapter = SharedAdapter(requireContext(), binding.dpPlannerChart, parentFragmentManager, plannerLivedata)
        binding.detailPlanList.adapter = sharedAdapter.listAdapter
        binding.detailPlanList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        plannerLivedata.repo._homelist.observe(viewLifecycleOwner, Observer {
            sharedAdapter.setDatalist(it)
        })

        val l = ItemTouchHelper(SwipeHendler(sharedAdapter))
        l.attachToRecyclerView(binding.detailPlanList)

//      일정추가
        binding.dpAddPlanBtn.setOnClickListener{
            val addSchedule = AddPlan(plannerinfo.index,plannerLivedata)
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