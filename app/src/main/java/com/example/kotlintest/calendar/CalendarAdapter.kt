package com.example.kotlintest.calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlintest.R
import com.example.kotlintest.db.AppDatabase
import com.example.kotlintest.db.Calendar_DTO
import com.example.kotlintest.db.PlannerName_DTO
import com.example.kotlintest.settingplanner.PlannerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CalendarAdapter: RecyclerView.Adapter<CalendarAdapter.CalenderViewHolder>{
    lateinit var mContext: Context
    var items: ArrayList<Calendar_DTO>
    var fm: FragmentManager
    constructor(fm: FragmentManager) {
        this.items = ArrayList()
        this.fm = fm
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarAdapter.CalenderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todolist_item, parent, false)
        mContext = view.context
        return CalenderViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarAdapter.CalenderViewHolder, position: Int) {
        var currentItem = items[position]

        holder.checkBox.isChecked = currentItem.done
        holder.textView.text = currentItem.task
        val db = AppDatabase.getDatabase(mContext)
        val calDao = db.calDao()

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            currentItem.done = !currentItem.done
            CoroutineScope(Dispatchers.Main).launch {
                val res = async(Dispatchers.IO) {
                    calDao.updateTaskForDate(currentItem)
                }
            }
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

//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        var itemView = convertView
//        if (itemView == null) {
//            itemView = LayoutInflater.from(context).inflate(R.layout.todolist_item, parent, false)
//        }
//
//        var currentItem = items[position]
//
//        val checkBox: CheckBox = itemView!!.findViewById(R.id.checkBox)
//        val textView: TextView = itemView.findViewById(R.id.plannerName)
//
//        checkBox.isChecked = currentItem.done
//        textView.text = currentItem.task
//        val db = AppDatabase.getDatabase(context)
//        val calDao = db.calDao()
//
//        checkBox.setOnCheckedChangeListener { _, isChecked ->
//            currentItem.done = !currentItem.done
//            CoroutineScope(Dispatchers.Main).launch {
//                val res = async(Dispatchers.IO) {
//                    calDao.updateTaskForDate(currentItem)
//                }
//            }
//        }
//
//        return itemView
//    }
    inner class CalenderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView!!.findViewById(R.id.checkBox)
        val textView: TextView = itemView.findViewById(R.id.plannerName)
    }
}