package com.example.kotlintest.settingplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlintest.calendar.CalendarVM
import com.example.kotlintest.databinding.FragmentSettingPlannerBinding
import com.example.kotlintest.util.SwipeHendler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingPlanner : Fragment(), SwipeHendler.OnItemMoveListener {
    private var _binding: FragmentSettingPlannerBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PlannerAdapter
    private lateinit var viewModel: PlannerVM
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingPlannerBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this,PlannerVM.Factory(requireActivity().application)).get(PlannerVM::class.java)

        adapter = PlannerAdapter(parentFragmentManager,viewModel)
        binding.plannerNameList.adapter = adapter

        binding.plannerNameList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        viewModel.getAllPlanner().observe(viewLifecycleOwner, Observer {
            adapter.clear()
            adapter.addAll(it)
        })

        val l = ItemTouchHelper(SwipeHendler(this))
        l.attachToRecyclerView(binding.plannerNameList)

        binding.addPlannerBtn.setOnClickListener{
            val addPlanner = AddPlanner(viewModel)
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.addToBackStack(null)
            addPlanner.show(transaction,addPlanner.tag)
        }

        //아이템클릭시 세부일정설정창으로 전환
//        listView.setOnItemClickListener { parent, view, position, id ->
//            val selectedItem = parent.getItemAtPosition(position) as PlannerName_DTO
//            val fragment = DetailPlanner(selectedItem)
//            parentFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null).commit()
//        }

        return binding.root
    }
    //메모리 누수 막기위해 뷰가없어질때 바인딩 해제
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun swiped(position: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            var d = adapter.items[position]
            viewModel.removePlanner(d)
        }
    }
}