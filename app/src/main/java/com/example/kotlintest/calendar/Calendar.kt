package com.example.kotlintest.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlintest.databinding.FragmentCalendarBinding
import com.example.kotlintest.db.AppDatabase
import com.example.kotlintest.db.Calendar_DAO
import com.example.kotlintest.db.Calendar_DTO
import com.example.kotlintest.util.SwipeHendler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import java.util.Calendar
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class Calendar : Fragment(), SwipeHendler.OnItemMoveListener {
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private lateinit var calDao: Calendar_DAO // Room DAO
    private var selectedDate = ""
    private lateinit var adapter: CalendarAdapter
    lateinit var db: AppDatabase
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        // Room 데이터베이스 인스턴스 생성
        db = AppDatabase.getDatabase(requireContext())

        // Room DAO 초기화
        calDao = db.calDao()

        selectedDate = LocalDate.now().toString()
        // 데이터 가져오기
        var items:ArrayList<Calendar_DTO> = ArrayList()
        adapter = CalendarAdapter(parentFragmentManager)
        binding.calTaskList.adapter = adapter
        binding.calTaskList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        loadDataFromDb(adapter)

        val l = ItemTouchHelper(SwipeHendler(this))
        l.attachToRecyclerView(binding.calTaskList)
        //선택한 날에 맞는 일정 불러오기
        binding.calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            selectedDate = dateFormat.format(calendar.time)
            loadDataFromDb(adapter)
        }

        //일정추가 -> Addtask 화면 띄우기
        binding.calAddtaskBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("date", selectedDate)

            val addTask: CalendarAddtask = CalendarAddtask {
                loadDataFromDb(adapter)
            }
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

    private fun loadDataFromDb(adapter: CalendarAdapter) {
        val dataList = mutableListOf<Calendar_DTO>()

        // 백그라운드 스레드에서 실행
        Executors.newSingleThreadExecutor().execute {
            val data = calDao.getAllTaskForDate(selectedDate) // Room DAO에서 데이터 가져오기

            // 가져온 데이터 처리
            for (item in data) {
                dataList.add(item)
            }

            // UI 갱신
            requireActivity().runOnUiThread {
                adapter.clear()
                adapter.addAll(dataList)
                adapter.notifyDataSetChanged()
            }
        }
    }
    override fun swiped(position: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            var d = adapter.items[position]
            adapter.items.removeAt(position)

            CoroutineScope(Dispatchers.IO).async {
                db.calDao().deletetable(d)
            }
        }
    }
}