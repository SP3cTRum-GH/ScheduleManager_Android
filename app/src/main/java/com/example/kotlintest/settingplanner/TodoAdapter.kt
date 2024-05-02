package com.example.kotlintest.settingplanner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlintest.R
import com.example.kotlintest.databinding.StringlistBinding
import com.example.kotlintest.db.TodoList_DTO

class TodoAdapter: RecyclerView.Adapter<TodoAdapter.TodoListViewHolder> {
    private var _binding: StringlistBinding? = null
    private val binding get() = _binding!!
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

    fun addAll(data: List<TodoList_DTO>) {
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

    holder.bind(currentItem.todo)
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListViewHolder {
        _binding = StringlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        mContext = binding.root.context
        return TodoListViewHolder(binding)
    }

    inner class TodoListViewHolder(val binding: StringlistBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(todotask: String) {
            binding.item = todotask
        }
    }

}