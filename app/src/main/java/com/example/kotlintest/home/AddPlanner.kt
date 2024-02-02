package com.example.kotlintest.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.kotlintest.R

interface ToggleVisibility {
    fun toggle()
}

class AddPlanner : Fragment {
    var inter: ToggleVisibility
    constructor(inter: ToggleVisibility){
        this.inter = inter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_planner, container, false)

        val button = view.findViewById<Button>(R.id.btnSave)
        button.setOnClickListener{
            parentFragmentManager.beginTransaction().remove(this).commit()
            this.inter.toggle()
        }
        return view
    }
}