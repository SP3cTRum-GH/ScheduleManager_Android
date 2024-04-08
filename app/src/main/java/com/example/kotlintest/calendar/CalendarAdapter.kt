package com.example.kotlintest.calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlintest.R
import com.example.kotlintest.databinding.TodolistItemBinding
import com.example.kotlintest.db.AppDatabase
import com.example.kotlintest.db.Calendar_DTO
import com.example.kotlintest.util.CalLivedata
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CalendarAdapter: RecyclerView.Adapter<CalendarAdapter.CalenderViewHolder>{
    private var _binding: TodolistItemBinding? = null
    private val binding get() = _binding!!
    lateinit var mContext: Context
    var items: ArrayList<Calendar_DTO>
    var fm: FragmentManager
    var calLivedata: CalLivedata

    constructor(fm: FragmentManager, calLivedata: CalLivedata) {
        this.items = ArrayList()
        this.fm = fm
        this.calLivedata = calLivedata
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarAdapter.CalenderViewHolder {
        _binding = TodolistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        _binding = FragmentHomeBinding.inflate(inflater, container, false)
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.todolist_item, parent, false)
//        mContext = view.context
        mContext = binding.root.context
        return CalenderViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: CalendarAdapter.CalenderViewHolder, position: Int) {
        var currentItem = items[position]

        holder.checkBox.isChecked = currentItem.done
        holder.textView.text = currentItem.task

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            currentItem.done = !currentItem.done
            calLivedata.updateTaskForDate(currentItem)
        }
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    fun clear() {
        items.clear()
    }

    fun addAll(data: MutableList<Calendar_DTO>) {
        this.items = ArrayList(data)
        this.notifyDataSetChanged()
    }

    inner class CalenderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = binding.checkBox
        val textView: TextView = binding.calText
    }
}