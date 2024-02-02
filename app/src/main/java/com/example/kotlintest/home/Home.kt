package com.example.kotlintest.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.example.kotlintest.R
import com.github.mikephil.charting.charts.LineChart
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Home : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_home, container, false)
        val pieChart = view.findViewById<LineChart>(R.id.PieChart)
        val editChart = view.findViewById<FloatingActionButton>(R.id.editChart)

        //시간표 수정|추가
        editChart.setOnClickListener{
            val editplanner = EditPlannerFragment()
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.addToBackStack(null)
//
//            transaction.add(EditPlannerFragment(), EditPlannerFragment().tag).commit()
            editplanner.show(transaction,editplanner.tag)
        }

        return view
    }

}