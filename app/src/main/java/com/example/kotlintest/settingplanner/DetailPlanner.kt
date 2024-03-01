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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlintest.R
import com.example.kotlintest.calendar.AddTask
import com.example.kotlintest.calendar.CalendarAdapter
import com.example.kotlintest.db.*
import com.example.kotlintest.util.SwipeHendler
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
        val listView = view.findViewById<RecyclerView>(R.id.detailListView)
        val piechart = view.findViewById<PieChart>(R.id.detailPieChart)

        piechart.centerText = plannerinfo.name
        piechart.setCenterTextSize(20f)

        // 데이터 가져오기
        sharedAdapter = SharedAdapter(requireContext(), plannerinfo.index, piechart, parentFragmentManager)
        listView.adapter = sharedAdapter.getListAdapter()
        listView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val l = ItemTouchHelper(SwipeHendler(sharedAdapter))
        l.attachToRecyclerView(listView)

        btnAdd.setOnClickListener{
            val fu: (Home_DTO) -> Unit = {data -> sharedAdapter.addData(data)}
            val addSchedule = AddPlanner(fu, plannerinfo.index)
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
//            transaction.add(EditPlannerFragment(), EditPlannerFragment().tag).commit()
            addSchedule.show(transaction,addSchedule.tag)
        }

//        listView.setOnItemClickListener { parent, view, position, id ->
//            val selectedItem = parent.getItemAtPosition(position) as Home_DTO
//            val fragment = SettingTodoList(selectedItem)
//            parentFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null).commit()
//        }

        return view
    }
}