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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TimePicker() : BottomSheetDialogFragment() {
    private var startBtn: Button? = null
    private var endBtn: Button? = null
    private var isStart = true

    constructor(startBtn: Button?, endBtn: Button?) : this() {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("hh : mm")
        val formatted = current.format(formatter)
        val endformatted = current.plusHours(1).format(formatter)

        this.startBtn = startBtn
        this.endBtn = endBtn

        startBtn?.setText(formatted.toString())
        endBtn?.setText(endformatted.toString())
    }

    fun setFlag(isStart: Boolean) {
        this.isStart = isStart
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
        if(!isStart) {
            var txt = this.startBtn?.text.toString().split(" : ")
            tp.hour = txt[0].toInt()
            tp.minute = txt[1].toInt()
        }

        ok.setOnClickListener {
            if(isStart) {
                this.startBtn?.setText(tp.hour.toString() +" : " +tp.minute.toString())
            } else {
                this.endBtn?.setText(tp.hour.toString() +" : " +tp.minute.toString())
            }

            dismiss()
        }

        cancle.setOnClickListener {
            dismiss()
        }

        return view
    }

}