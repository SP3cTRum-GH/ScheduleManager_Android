package com.example.kotlintest.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import com.example.kotlintest.R
import com.example.kotlintest.calendar.CalendarAdapter
import com.example.kotlintest.db.*
import java.util.concurrent.Executors

class EditPlannerFragment(val cb: (Long) -> Unit) : DialogFragment() {
    private lateinit var plannerNameDao: PlannerName_DAO
    private lateinit var adapter: HomeAdapter
    private lateinit var view: View
    var selectedItem: Long = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_planner, container, false)
        val plannerlist = view.findViewById<ListView>(R.id.plannerList)
        plannerlist.choiceMode = ListView.CHOICE_MODE_SINGLE
        val cancel = view.findViewById<Button>(R.id.cancel)
        val save = view.findViewById<Button>(R.id.save)

        // Room 데이터베이스 인스턴스 생성
        val db = AppDatabase.getDatabase(requireContext())
        // Room DAO 초기화
        plannerNameDao = db.plannerDao()
        //데이터 가져오기
        var items = ArrayList<PlannerName_DTO>()
        adapter = HomeAdapter(requireContext(), items)
        plannerlist.adapter = adapter
        loadDataFromDb(adapter)

        plannerlist.setOnItemClickListener { adapterView, view, i, l ->
            val position = adapterView.getItemAtPosition(i) as PlannerName_DTO
            selectedItem = position.index
        }

        cancel.setOnClickListener{
            dismiss()
        }

        save.setOnClickListener{
            cb(selectedItem)
            dismiss()
        }

        return view
    }

    private fun loadDataFromDb(adapter: HomeAdapter) {
        val dataList = mutableListOf<PlannerName_DTO>() // 데이터 타입에 맞게 수정

        // 백그라운드 스레드에서 실행
        Executors.newSingleThreadExecutor().execute {
            val data = plannerNameDao.getAllPlanner() // Room DAO에서 데이터 가져오기

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

//    override fun toggle() {
//        view.findViewById<Button>(R.id.cancel).visibility = View.VISIBLE
//        view.findViewById<Button>(R.id.save).visibility = View.VISIBLE
//        view.invalidate()
//    }
}