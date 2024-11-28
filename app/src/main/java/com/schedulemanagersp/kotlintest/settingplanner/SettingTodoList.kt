package com.schedulemanagersp.kotlintest.settingplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.schedulemanagersp.kotlintest.databinding.FragmentSettingTodoListBinding
import com.schedulemanagersp.kotlintest.db.*
import com.schedulemanagersp.kotlintest.util.SwipeHendler

class SettingTodoList(val plannerinfo: Home_DTO,val viewModel: PlannerVM):Fragment(), SwipeHendler.OnItemMoveListener{
    private var _binding: FragmentSettingTodoListBinding? = null
    private val binding get() = _binding!!
    private lateinit var todoAdapter: TodoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingTodoListBinding.inflate(inflater, container, false)

        todoAdapter = TodoAdapter()
        binding.todoTaskList.adapter = todoAdapter
        binding.todoTaskList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewModel.setTodoQuery(plannerinfo.index)
        viewModel.getAllTodo().observe(viewLifecycleOwner, Observer {
            todoAdapter.clear()
            todoAdapter.addAll(it)
        })

        val l = ItemTouchHelper(SwipeHendler(this))
        l.attachToRecyclerView(binding.todoTaskList)

        binding.addTodoBtn.setOnClickListener{
            val addTodoTask = AddTodoTask(plannerinfo, viewModel)
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.addToBackStack(null)
            addTodoTask.show(transaction,addTodoTask.tag)
        }

        return binding.root
    }
    //메모리 누수 막기위해 뷰가없어질때 바인딩 해제
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun swiped(position: Int) {
        var d = todoAdapter.items[position]
        viewModel.removeTodo(d)
    }
}
