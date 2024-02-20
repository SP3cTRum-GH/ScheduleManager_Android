package com.example.kotlintest.settingplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.FragmentTransaction
import com.example.kotlintest.R
import com.example.kotlintest.db.*
import java.util.concurrent.Executors

class SettingTodoList(val plannerinfo: Home_DTO):Fragment(){
    private lateinit var todoDao: TodoList_DAO
    private lateinit var todoAdapter: TodoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_setting_todo_list, container, false)
        val btnAdd = view.findViewById<Button>(R.id.btnAddTodo)
        val listView = view.findViewById<ListView>(R.id.todoTaskList)

        // Room 데이터베이스 인스턴스 생성
        val db = AppDatabase.getDatabase(requireContext())

        // Room DAO 초기화
        todoDao = db.todoDao()

        var items:ArrayList<TodoList_DTO> = ArrayList()
        todoAdapter = TodoAdapter(requireContext(), items)
        listView.adapter = todoAdapter
        loadDataFromDb(todoAdapter)

        btnAdd.setOnClickListener{
            val addTodoTask = AddTodoTask(plannerinfo, {loadDataFromDb(todoAdapter)})
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.addToBackStack(null)
            addTodoTask.show(transaction,addTodoTask.tag)
        }

        return view
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
}
