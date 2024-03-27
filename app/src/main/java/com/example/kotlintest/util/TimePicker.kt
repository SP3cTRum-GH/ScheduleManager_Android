package com.example.kotlintest.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TimePicker
import com.example.kotlintest.R
import com.example.kotlintest.databinding.FragmentTimePickerBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TimePicker() : BottomSheetDialogFragment() {
    private var _binding: FragmentTimePickerBinding? = null
    private val binding get() = _binding!!
    private var startBtn: Button? = null
    private var endBtn: Button? = null
    private var startTime: String = ""
    private var endTime: String = ""
    private var isStart = true

    constructor(startBtn: Button?, endBtn: Button?) : this() {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH : mm")
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

    fun getStartTime():String{
        return startTime
    }
    fun getEndTime():String{
        return endTime
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTimePickerBinding.inflate(inflater, container, false)

        if(!isStart) {
            var txt = this.startBtn?.text.toString().split(" : ")
            binding.timePicker.hour = txt[0].toInt()
            binding.timePicker.minute = txt[1].toInt()
        }

        binding.tpOkbtn.setOnClickListener {
            if(isStart) {
                this.startTime = String.format("%02d", binding.timePicker.hour) +" : " +String.format("%02d", binding.timePicker.minute)
                this.startBtn?.setText(startTime)
                this.endTime = String.format("%02d", binding.timePicker.hour + 1) + " : " +String.format("%02d", binding.timePicker.minute)
                this.endBtn?.setText(endTime)
            } else {
                this.endTime = String.format("%02d", binding.timePicker.hour) +" : " +String.format("%02d", binding.timePicker.minute)
                this.endBtn?.setText(endTime)
            }

            dismiss()
        }

        binding.tpCancelBtn.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
    //메모리 누수 막기위해 뷰가없어질때 바인딩 해제
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}