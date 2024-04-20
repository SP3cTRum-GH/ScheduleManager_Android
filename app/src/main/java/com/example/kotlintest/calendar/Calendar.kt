package com.example.kotlintest.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlintest.databinding.FragmentCalendarBinding
import com.example.kotlintest.util.SwipeHendler
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import java.util.Calendar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class Calendar : Fragment() {
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private var selectedDate = ""
    private lateinit var viewModel: CalendarVM
    private lateinit var adapter: CalendarAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this,CalendarVM.Factory(requireActivity().application)).get(CalendarVM::class.java)

        selectedDate = LocalDate.now().toString()
        viewModel.setQuery(selectedDate)
        // 데이터 가져오기
        adapter = CalendarAdapter(parentFragmentManager,viewModel)
        binding.calTaskList.adapter = adapter
        binding.calTaskList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        viewModel.getAllTaskForDate().observe(viewLifecycleOwner, Observer {
            adapter.clear()
            adapter.addAll(it)
        })

        val l = ItemTouchHelper(SwipeHendler(adapter))
        l.attachToRecyclerView(binding.calTaskList)

        //선택한 날에 맞는 일정 불러오기
        binding.calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            selectedDate = dateFormat.format(calendar.time)
            viewModel.setQuery(selectedDate)
        }

        //일정추가 -> Addtask 화면 띄우기
        binding.calAddtaskBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("date", selectedDate)

            val addTask = CalendarAddtask(viewModel)
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

}