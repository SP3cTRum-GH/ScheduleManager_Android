package com.example.kotlintest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TimePicker
import androidx.core.view.get
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Calendar

class TimePicker : BottomSheetDialogFragment() {
    private var listener: TimePickerListener? = null

    fun setListener(listener: TimePickerListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_time_picker, container, false)
        // Inflate the layout for this fragment
        val ok =  view.findViewById<Button>(R.id.btnok)
        val cancle = view.findViewById<Button>(R.id.btncancle)
        val tp = view.findViewById<TimePicker>(R.id.timePicker)

        ok.setOnClickListener {
            listener?.onTimeSelected(tp.hour.toString(), tp.minute.toString())

            dismiss()
        }
        cancle.setOnClickListener {
            dismiss()
        }
        return view
    }

}