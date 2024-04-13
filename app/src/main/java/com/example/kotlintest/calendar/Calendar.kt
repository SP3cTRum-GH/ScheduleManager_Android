package com.example.kotlintest.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlintest.databinding.FragmentCalendarBinding
import com.example.kotlintest.livedata.CalLivedata
import com.example.kotlintest.util.SwipeHendler
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import java.util.Calendar
import androidx.lifecycle.Observer

class Calendar(val calLivedata: CalLivedata) : Fragment(), SwipeHendler.OnItemMoveListener {
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private var selectedDate = ""
    private lateinit var adapter: CalendarAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        selectedDate = LocalDate.now().toString()
        // 데이터 가져오기
        adapter = CalendarAdapter(parentFragmentManager,calLivedata)
        binding.calTaskList.adapter = adapter
        binding.calTaskList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        calLivedata.repo._callist.observe(viewLifecycleOwner, Observer {
            adapter.clear()
            adapter.addAll(it)
        })

        val l = ItemTouchHelper(SwipeHendler(this))
        l.attachToRecyclerView(binding.calTaskList)
        //선택한 날에 맞는 일정 불러오기
        binding.calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            selectedDate = dateFormat.format(calendar.time)
            calLivedata.getAllTaskForDate(selectedDate)
        }

        //일정추가 -> Addtask 화면 띄우기
        binding.calAddtaskBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("date", selectedDate)

            val addTask = CalendarAddtask(calLivedata)
            addTask.arguments = bundle
            activity?.let { it1 -> addTask.show(it1.supportFragmentManager, addTask.tag) }
        }


        return binding.root
    }

    //메모리 누수 막기위해 뷰가없어질때 바인딩 해제
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun swiped(position: Int) {
        var d = adapter.items[position]
        calLivedata.removeCalendarTable(d)
    }
}