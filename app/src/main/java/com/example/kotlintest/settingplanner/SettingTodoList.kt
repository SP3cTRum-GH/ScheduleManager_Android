package com.example.kotlintest.settingplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlintest.R
import com.example.kotlintest.databinding.FragmentSettingTodoListBinding
import com.example.kotlintest.db.*
import com.example.kotlintest.util.SwipeHendler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class SettingTodoList(val plannerinfo: Home_DTO):Fragment(), SwipeHendler.OnItemMoveListener{
    private var _binding: FragmentSettingTodoListBinding? = null
    private val binding get() = _binding!!
    private lateinit var todoDao: TodoList_DAO
    private lateinit var todoAdapter: TodoAdapter
    // Room 데이터베이스 인스턴스 생성
    lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingTodoListBinding.inflate(inflater, container, false)

        db = AppDatabase.getDatabase(requireContext())
        // Room DAO 초기화
        todoDao = db.todoDao()

        todoAdapter = TodoAdapter()
        binding.todoTaskList.adapter = todoAdapter
        binding.todoTaskList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        loadDataFromDb(todoAdapter)

        val l = ItemTouchHelper(SwipeHendler(this))
        l.attachToRecyclerView(binding.todoTaskList)

        binding.addTodoBtn.setOnClickListener{
            val addTodoTask = AddTodoTask(plannerinfo, {loadDataFromDb(todoAdapter)})
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

    private fun loadDataFromDb(adapter: TodoAdapter) {
        val dataList = mutableListOf<TodoList_DTO>() // 데이터 타입에 맞게 수정

        // 백그라운드 스레드에서 실행
        Executors.newSingleThreadExecutor().execute {
            val data = todoDao.getAllTodo(plannerinfo.index) // Room DAO에서 데이터 가져오기

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

    override fun swiped(position: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            var d = todoAdapter.items[position]
//            data.deleteData(position)
            todoAdapter.items.removeAt(position)

            CoroutineScope(Dispatchers.IO).async {
                db.todoDao().deleteTodo(d)
            }
        }
    }
}
