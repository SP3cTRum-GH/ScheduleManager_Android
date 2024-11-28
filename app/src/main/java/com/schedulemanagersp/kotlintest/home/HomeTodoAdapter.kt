package com.schedulemanagersp.kotlintest.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.schedulemanagersp.kotlintest.databinding.TodolistItemBinding
import com.schedulemanagersp.kotlintest.db.TodoList_DTO

class HomeTodoAdapter : RecyclerView.Adapter<HomeTodoAdapter.HomeTodoViewHolder> {
    private var _binding: TodolistItemBinding? = null
    private val binding get() = _binding!!
    var items: List<TodoList_DTO>
    var mContext: Context
    val viewModel: HomeVM
    constructor(context: Context,viewModel: HomeVM) {
        this.mContext = context
        this.items = ArrayList()
        this.viewModel = viewModel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeTodoAdapter.HomeTodoViewHolder {
        _binding = TodolistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        _binding = FragmentHomeBinding.inflate(inflater, container, false)
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.todolist_item, parent, false)
//        mContext = view.context
        mContext = binding.root.context
        return HomeTodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeTodoAdapter.HomeTodoViewHolder, position: Int) {
        var currentItem = items[position]
        holder.bind(currentItem.todo, currentItem.done)
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            currentItem.done = isChecked
            viewModel.updateTodo(currentItem)
        }
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getItemCount(): Int {
        return items.count()
    }

//    override fun getCount(): Int {
//        return items.size
//    }
//
//    override fun getItem(p0: Int): Any {
//        return items[p0]
//    }
//    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
//        var itemView = p1
//        if (itemView == null) {
//            itemView = LayoutInflater.from(mContext).inflate(R.layout.todolist_item, p2, false)
//        }
//
//        var currentItem = items[p0]
//
//        val checkBox: CheckBox = itemView!!.findViewById(R.id.checkBox)
//        val textView: TextView = itemView.findViewById(R.id.calText)
//
//        checkBox.isChecked = currentItem.done
//        textView.text = currentItem.todo
//        val db = AppDatabase.getDatabase(mContext)
//        val todoDao = db.todoDao()
//
//        checkBox.setOnCheckedChangeListener { _, isChecked ->
//            currentItem.done = !currentItem.done
//            CoroutineScope(Dispatchers.Main).launch {
//                val res = async(Dispatchers.IO) {
//                    todoDao.update(currentItem)
//                }
//            }
//        }

    inner class HomeTodoViewHolder(val binding: TodolistItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(task: String, done: Boolean) {
            binding.task = task
            binding.done = done
        }
        val checkBox: CheckBox = binding.checkBox
    }

}