package com.schedulemanagersp.kotlintest.calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.schedulemanagersp.kotlintest.databinding.TodolistItemBinding
import com.schedulemanagersp.kotlintest.db.Calendar_DTO
import com.schedulemanagersp.kotlintest.util.SwipeHendler

class CalendarAdapter: RecyclerView.Adapter<CalendarAdapter.CalenderViewHolder>, SwipeHendler.OnItemMoveListener{
    private var _binding: TodolistItemBinding? = null
    private val binding get() = _binding!!
    lateinit var mContext: Context
    var items: ArrayList<Calendar_DTO>
    var fm: FragmentManager
    private var viewModel: CalendarVM

    constructor(fm: FragmentManager, viewModel: CalendarVM) {
        this.items = ArrayList()
        this.fm = fm
        this.viewModel = viewModel
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarAdapter.CalenderViewHolder {
        _binding = TodolistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        _binding = FragmentHomeBinding.inflate(inflater, container, false)
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.todolist_item, parent, false)
//        mContext = view.context
        mContext = binding.root.context
        return CalenderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarAdapter.CalenderViewHolder, position: Int) {
        var currentItem = items[position]
        holder.bind(currentItem.task, currentItem.done)

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            currentItem.done = isChecked
            viewModel.updateTaskForDate(currentItem)
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

    fun addAll(data: List<Calendar_DTO>) {
        this.items = ArrayList(data)
        this.notifyDataSetChanged()
    }

    inner class CalenderViewHolder(val binding: TodolistItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(task: String, done: Boolean) {
            binding.task = task
            binding.done = done
        }
        val checkBox: CheckBox = binding.checkBox
    }

    override fun swiped(position: Int) {
        var d = items[position]
        viewModel.removeCalendarTable(d)
    }
}