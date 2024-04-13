package com.example.kotlintest.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.example.kotlintest.databinding.FragmentSelectPlannerBinding
import com.example.kotlintest.db.*
import com.example.kotlintest.livedata.PlannerLivedata

class SelectPlannerFragment(val plannerLivedata: PlannerLivedata) : DialogFragment() {
    private var _binding: FragmentSelectPlannerBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: HomeAdapter
    var selectedItem: Long = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSelectPlannerBinding.inflate(inflater, container, false)
        binding.spPlannerList.choiceMode = ListView.CHOICE_MODE_SINGLE

        plannerLivedata.repo._plannerNamelist.observe(viewLifecycleOwner, Observer {
            adapter.clear()
            adapter.addAll(it)
        })

        //데이터 가져오기
        var items = ArrayList<PlannerName_DTO>()
        adapter = HomeAdapter(requireContext(), items)
        binding.spPlannerList.adapter = adapter
        plannerLivedata.getAllPlanner()

        binding.spPlannerList.setOnItemClickListener { adapterView, view, i, l ->
            val position = adapterView.getItemAtPosition(i) as PlannerName_DTO
            selectedItem = position.index
        }

        binding.spCancelBtn.setOnClickListener{
            dismiss()
        }

        binding.spSubmitBtn.setOnClickListener{
            plannerLivedata.getAllPlan(selectedItem)
            dismiss()
        }

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    override fun toggle() {
//        view.findViewById<Button>(R.id.cancel).visibility = View.VISIBLE
//        view.findViewById<Button>(R.id.save).visibility = View.VISIBLE
//        view.invalidate()
//    }
}