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
import com.example.kotlintest.db.PlannerName_DTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddPlannerName(val cb: () -> Unit) : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var  view = inflater.inflate(R.layout.fragment_add_planner_name, container, false)
        val cancel = view.findViewById<Button>(R.id.cancel)
        val ok = view.findViewById<Button>(R.id.save)
        val plannerName = view.findViewById<EditText>(R.id.plannerNameET)
        // 데이터베이스 인스턴스 얻기
        val db = AppDatabase.getDatabase(requireContext())
        // DAO 초기화
        val plannerDao = db.plannerDao()

        cancel.setOnClickListener{
            dismiss()
        }

        ok.setOnClickListener{
            val plannerName = PlannerName_DTO(name = plannerName.text.toString())

            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.IO) {
                    plannerDao.insertPlanner(plannerName)
                    cb()
                }
            }
            dismiss()
        }

        return view
    }
}