package com.example.kotlintest.settingplanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import com.example.kotlintest.R
import com.example.kotlintest.db.Home_DTO
import com.example.kotlintest.db.TodoList_DTO

class AddTodoTask(val plannerinfo: Home_DTO, val viewModel: PlannerVM) : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_todo_task, container, false)
        val cancel = view.findViewById<Button>(R.id.spCancelBtn)
        val ok = view.findViewById<Button>(R.id.spSubmitBtn)
        val todoTask = view.findViewById<EditText>(R.id.todoTaskET)

        cancel.setOnClickListener{
            dismiss()
        }

        ok.setOnClickListener{
            val task = TodoList_DTO(todo = todoTask.text.toString(), done = false, plannerIndex = plannerinfo.index)
            viewModel.insertTodo(task)
            dismiss()
        }

        return view
    }
}