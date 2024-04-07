package com.example.kotlintest.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.DialogFragment
import com.example.kotlintest.R
import com.example.kotlintest.databinding.FragmentSelectPlannerBinding
import com.example.kotlintest.db.*
import com.example.kotlintest.util.PlannerLivedata
import java.util.concurrent.Executors

class SelectPlannerFragment(val plannerLivedata: PlannerLivedata) : DialogFragment() {
    private var _binding: FragmentSelectPlannerBinding? = null
    private val binding get() = _binding!!
    private lateinit var plannerNameDao: PlannerName_DAO
    private lateinit var adapter: HomeAdapter
    var selectedItem: Long = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSelectPlannerBinding.inflate(inflater, container, false)
        binding.spPlannerList.choiceMode = ListView.CHOICE_MODE_SINGLE

        // Room 데이터베이스 인스턴스 생성
        val db = AppDatabase.getDatabase(requireContext())
        // Room DAO 초기화
        plannerNameDao = db.plannerDao()
        //데이터 가져오기
        var items = ArrayList<PlannerName_DTO>()
        adapter = HomeAdapter(requireContext(), items)
        binding.spPlannerList.adapter = adapter
        loadDataFromDb(adapter)

        binding.spPlannerList.setOnItemClickListener { adapterView, view, i, l ->
            val position = adapterView.getItemAtPosition(i) as PlannerName_DTO
            selectedItem = position.index
        }

        binding.spCancelBtn.setOnClickListener{
            dismiss()
        }

        binding.spSubmitBtn.setOnClickListener{
            plannerLivedata.getAllPlanner(selectedItem)
            dismiss()
        }

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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