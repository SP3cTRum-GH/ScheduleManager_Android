package com.example.kotlintest.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.example.kotlintest.R
import com.example.kotlintest.db.AppDatabase
import com.example.kotlintest.db.TodoList_DTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeTodoAdapter : BaseAdapter {
    var items: ArrayList<TodoList_DTO>
    var mContext: Context

    constructor(context: Context) {
        this.mContext = context
        this.items = ArrayList()
    }

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

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var itemView = p1
        if (itemView == null) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.todolist_item, p2, false)
        }

        var currentItem = items[p0]

        val checkBox: CheckBox = itemView!!.findViewById(R.id.checkBox)
        val textView: TextView = itemView.findViewById(R.id.calText)

        checkBox.isChecked = currentItem.done
        textView.text = currentItem.todo
        val db = AppDatabase.getDatabase(mContext)
        val todoDao = db.todoDao()

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            currentItem.done = !currentItem.done
            CoroutineScope(Dispatchers.Main).launch {
                val res = async(Dispatchers.IO) {
                    todoDao.update(currentItem)
                }
            }
        }

        return itemView
    }
}