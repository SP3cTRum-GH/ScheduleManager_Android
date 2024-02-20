package com.example.kotlintest.settingplanner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.kotlintest.R
import com.example.kotlintest.db.TodoList_DTO

class TodoAdapter (val context: Context, var items: ArrayList<TodoList_DTO>) : BaseAdapter() {
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

    fun addAll(data: MutableList<TodoList_DTO>) {
        this.items = ArrayList(data)
        this.notifyDataSetChanged()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var itemView = p1
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.stringlist, p2, false)
        }
        var currentItem = items[p0]

        val plannerName: TextView = itemView!!.findViewById(R.id.plannerName)

        plannerName.text = currentItem.todo

        return itemView
    }

}