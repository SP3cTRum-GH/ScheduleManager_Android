package com.example.kotlintest.settingplanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.kotlintest.R
import com.example.kotlintest.db.AppDatabase
import com.example.kotlintest.db.Home_DTO
import com.example.kotlintest.db.TodoList_DTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddTodoTask(val plannerinfo: Home_DTO, val cb: () -> Unit) : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_todo_task, container, false)
        val cancel = view.findViewById<Button>(R.id.spCancelBtn)
        val ok = view.findViewById<Button>(R.id.spSubmitBtn)
        val todoTask = view.findViewById<EditText>(R.id.todoTaskET)
        // 데이터베이스 인스턴스 얻기
        val db = AppDatabase.getDatabase(requireContext())
        // DAO 초기화
        val todoDao = db.todoDao()

        cancel.setOnClickListener{
            dismiss()
        }

        ok.setOnClickListener{
            val task = TodoList_DTO(todo = todoTask.text.toString(), done = false, plannerIndex = plannerinfo.index)

            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.IO) {
                    todoDao.insertPlanner(task)
                    cb()
                }
            }
            dismiss()
        }

        return view
    }
}