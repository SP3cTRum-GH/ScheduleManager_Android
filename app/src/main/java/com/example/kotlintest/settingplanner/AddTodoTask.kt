package com.example.kotlintest.settingplanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.kotlintest.R
import com.example.kotlintest.db.Home_DTO
import com.example.kotlintest.db.TodoList_DTO
import com.example.kotlintest.livedata.PlannerLivedata

class AddTodoTask(val plannerinfo: Home_DTO, val plannerLivedata: PlannerLivedata) : DialogFragment() {
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
            plannerLivedata.insertTodo(task)
            dismiss()
        }

        return view
    }
}