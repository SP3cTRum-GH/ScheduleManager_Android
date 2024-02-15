package com.example.kotlintest.settingplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import com.example.kotlintest.R
import com.example.kotlintest.calendar.AddTask
import com.example.kotlintest.calendar.CalendarAdapter
import com.example.kotlintest.db.*
import com.github.mikephil.charting.charts.PieChart
import java.time.LocalDate
import java.util.concurrent.Executors

class DetailPlanner(val plannerinfo:PlannerName_DTO) : Fragment() {
    private lateinit var sharedAdapter: SharedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_detail_planner, container, false)
        val btnAdd = view.findViewById<Button>(R.id.addSchedule)
        val listView = view.findViewById<ListView>(R.id.detailListView)
        val piechart = view.findViewById<PieChart>(R.id.detailPieChart)

        piechart.centerText = plannerinfo.name
        piechart.setCenterTextSize(20f)

        // 데이터 가져오기
        sharedAdapter = SharedAdapter(requireContext(), plannerinfo.index, piechart)
        listView.adapter = sharedAdapter.getListAdapter()

        btnAdd.setOnClickListener{
            val fu: (Home_DTO) -> Unit = {data -> sharedAdapter.addData(data)}
            val addSchedule = AddPlanner(fu, plannerinfo.index)
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
//            transaction.add(EditPlannerFragment(), EditPlannerFragment().tag).commit()
            addSchedule.show(transaction,addSchedule.tag)
        }


        return view
    }
}