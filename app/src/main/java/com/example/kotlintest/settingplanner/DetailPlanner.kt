package com.example.kotlintest.settingplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlintest.R
import com.example.kotlintest.databinding.FragmentDetailPlannerBinding
import com.example.kotlintest.db.*
import com.example.kotlintest.util.SwipeHendler
import com.github.mikephil.charting.charts.PieChart

class DetailPlanner(val plannerinfo:PlannerName_DTO) : Fragment() {
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
        sharedAdapter = SharedAdapter(requireContext(), plannerinfo.index, binding.dpPlannerChart, parentFragmentManager)
        binding.detailPlanList.adapter = sharedAdapter.getListAdapter()
        binding.detailPlanList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val l = ItemTouchHelper(SwipeHendler(sharedAdapter))
        l.attachToRecyclerView(binding.detailPlanList)

        binding.dpAddPlanBtn.setOnClickListener{
            val fu: (Home_DTO) -> Unit = {data -> sharedAdapter.addData(data)}
            val addSchedule = AddPlan(fu, plannerinfo.index)
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
//            transaction.add(EditPlannerFragment(), EditPlannerFragment().tag).commit()
            addSchedule.show(transaction,addSchedule.tag)
        }

//        listView.setOnItemClickListener { parent, view, position, id ->
//            val selectedItem = parent.getItemAtPosition(position) as Home_DTO
//            val fragment = SettingTodoList(selectedItem)
//            parentFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null).commit()
//        }

        return binding.root
    }
    //메모리 누수 막기위해 뷰가없어질때 바인딩 해제
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}