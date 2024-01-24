package com.example.kotlintest.calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.example.kotlintest.R
import com.example.kotlintest.db.AppDatabase
import com.example.kotlintest.db.Calendar_DTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CustomAdapter (val context: Context, var items: ArrayList<Calendar_DTO>) : BaseAdapter() {
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(p0: Int): Any {
        return items[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    fun clear() {
        items.clear()
    }

    fun addAll(data: MutableList<Calendar_DTO>) {
        this.items = ArrayList(data)
        this.notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.todolist_item, parent, false)
        }

        var currentItem = items[position]

        val checkBox: CheckBox = itemView!!.findViewById(R.id.checkBox)
        val textView: TextView = itemView.findViewById(R.id.tesk)

        checkBox.isChecked = currentItem.done
        textView.text = currentItem.task
        val db = AppDatabase.getDatabase(context)
        val calDao = db.calDao()

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            currentItem.done = !currentItem.done
            CoroutineScope(Dispatchers.Main).launch {
                val res = async(Dispatchers.IO) {
                    calDao.updateTaskForDate(currentItem)
                }
            }
        }

        return itemView
    }
}