package com.example.kotlintest.settingplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.FragmentTransaction
import androidx.room.Dao
import com.example.kotlintest.R
import com.example.kotlintest.calendar.CalendarAdapter
import com.example.kotlintest.db.AppDatabase
import com.example.kotlintest.db.Calendar_DTO
import com.example.kotlintest.db.PlannerName_DAO
import com.example.kotlintest.db.PlannerName_DTO
import java.util.concurrent.Executors

class SettingPlanner : Fragment() {
    private lateinit var plannerDao: PlannerName_DAO // Room DAO
    private lateinit var adapter: PlannerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.setting_planner, container, false)

        val add = view.findViewById<Button>(R.id.btnAddPlanner)
        val listView = view.findViewById<ListView>(R.id.plannerNameList)
        // Room 데이터베이스 인스턴스 생성
        val db = AppDatabase.getDatabase(requireContext())

        // Room DAO 초기화
        plannerDao = db.plannerDao()

        var items:ArrayList<PlannerName_DTO> = ArrayList()
        adapter = PlannerAdapter(requireContext(), items)
        listView.adapter = adapter

        loadDataFromDb(adapter)
        add.setOnClickListener{
            val addplannername = AddPlannerName{
                loadDataFromDb(adapter)
            }
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.addToBackStack(null)
            addplannername.show(transaction,addplannername.tag)
        }

        //아이템클릭시 세부일정설정창으로 전환
        listView.setOnItemClickListener { adapterView, view, i, l ->

        }

        return view
    }
    private fun loadDataFromDb(adapter: PlannerAdapter) {
        val dataList = mutableListOf<PlannerName_DTO>() // 데이터 타입에 맞게 수정

        // 백그라운드 스레드에서 실행
        Executors.newSingleThreadExecutor().execute {
            val data = plannerDao.getAllPlanner() // Room DAO에서 데이터 가져오기

            // 가져온 데이터 처리
            for (item in data) {
                dataList.add(item)
            }

            // UI 갱신
            requireActivity().runOnUiThread {
                adapter.clear()
                adapter.addAll(dataList)
                adapter.notifyDataSetChanged()
            }
        }
    }
}