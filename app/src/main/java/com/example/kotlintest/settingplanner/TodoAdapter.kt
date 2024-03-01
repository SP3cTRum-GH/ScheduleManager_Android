package com.example.kotlintest.settingplanner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlintest.R
import com.example.kotlintest.db.Home_DTO
import com.example.kotlintest.db.TodoList_DTO
import com.example.kotlintest.util.SwipeHendler

class TodoAdapter: RecyclerView.Adapter<TodoAdapter.TodoListViewHolder> {
    lateinit var mContext: Context
    var items: ArrayList<TodoList_DTO>
    constructor() {
        this.items = ArrayList()
    }
//    override fun getCount(): Int {
//        return items.size
//    }
//
//    override fun getItem(p0: Int): Any {
//        return items[p0]
//    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    fun clear() {
        items.clear()
    }

    fun addAll(data: MutableList<TodoList_DTO>) {
        this.items = ArrayList(data)
        this.notifyDataSetChanged()
    }

//    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
//        var itemView = p1
//        if (itemView == null) {
//            itemView = LayoutInflater.from(context).inflate(R.layout.stringlist, p2, false)
//        }
//        var currentItem = items[p0]
//
//        val plannerName: TextView = itemView!!.findViewById(R.id.plannerName)
//
//        plannerName.text = currentItem.todo
//
//        return itemView
//    }
override fun onBindViewHolder(holder: TodoListViewHolder, position: Int) {
    var currentItem = items[position]

    holder.todo.text = currentItem.todo
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.stringlist, parent, false)
        mContext = view.context
        return TodoListViewHolder(view)
    }

    inner class TodoListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val todo: TextView = itemView.findViewById(R.id.plannerName)
    }

}