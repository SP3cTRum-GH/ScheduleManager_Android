package com.example.kotlintest.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.FragmentTransaction
import com.example.kotlintest.R
import com.github.mikephil.charting.charts.PieChart
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Home : Fragment() {
    private var idx: Long = -1
    private lateinit var adapter: HomeSharedAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_home, container, false)
        val pieChart = view.findViewById<PieChart>(R.id.detailPieChart)
        val changePlanner = view.findViewById<FloatingActionButton>(R.id.editChart)
        val callistView = view.findViewById<ListView>(R.id.homeCalTodoList)
        val todolistView = view.findViewById<ListView>(R.id.homeCurrentTodoList)

        adapter = HomeSharedAdapter(requireContext(), pieChart)
        callistView.adapter = adapter.getListAdapter()
        todolistView.adapter = adapter.getTodoAdapter()

        //시간표 변경
        changePlanner.setOnClickListener{
            val changeplanner = EditPlannerFragment({
                adapter.idx = it
                adapter.loadDataFromDb()})
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.addToBackStack(null)

            changeplanner.show(transaction,changeplanner.tag)
        }

        return view
    }

}