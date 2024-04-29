package com.example.kotlintest.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlintest.calendar.CalendarVM
import com.example.kotlintest.databinding.FragmentHomeBinding
import com.example.kotlintest.db.TodoList_DTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class Home: Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeVM
    private lateinit var adapter: HomeSharedAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
//        var view = inflater.inflate(R.layout.fragment_home, container, false)
        viewModel = ViewModelProvider(this,HomeVM.Factory(requireActivity().application)).get(HomeVM::class.java)
        adapter = HomeSharedAdapter(requireContext(), binding.homePlannerChart, viewModel)
        binding.homeCalTodoList.adapter = adapter.listAdapter
        binding.homeCalTodoList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.homeCurrentTodoList.adapter = adapter.todoAdapter
        binding.homeCurrentTodoList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        //옵저빙
        viewModel.getAllPlan().observe(viewLifecycleOwner, Observer {
            adapter.setHomelist(it)
        })

        viewModel.todoindex.observe(viewLifecycleOwner, Observer{
            var todoitem = ArrayList<TodoList_DTO>()
            if(it != null) {
                CoroutineScope(Dispatchers.Main).launch {
                    val ret = CoroutineScope(Dispatchers.IO).async {
                        todoitem.addAll(viewModel.getCurrentTodo(it))
                    }
                    ret.await()
                    adapter.setTodolist(todoitem)
                }
            }
            else
                adapter.setTodolist(todoitem)
        })

        viewModel.setCalQuery()
        viewModel.getCurrentTaskForDate().observe(viewLifecycleOwner, Observer {
            adapter.setCallist(it)
        })

        //시간표 변경
        binding.selectPlannerFAB.setOnClickListener{
            val changeplanner = SelectPlannerFragment(viewModel)
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.addToBackStack(null)

            changeplanner.show(transaction,changeplanner.tag)
        }

        return binding.root
    }

    //메모리 누수 막기위해 뷰가없어질때 바인딩 해제
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}