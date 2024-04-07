package com.example.kotlintest.settingplanner

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlintest.R
import com.example.kotlintest.databinding.SettingplannerListviewBinding
import com.example.kotlintest.db.Home_DTO


class SettingPlannerAdapter : RecyclerView.Adapter<SettingPlannerAdapter.SettingPlannerViewHolder> {
    private var _binding: SettingplannerListviewBinding? = null
    private val binding get() = _binding!!
    var items: ArrayList<Home_DTO>
    var fm: FragmentManager
    lateinit var mContext: Context

    constructor(fm: FragmentManager) {
        this.items = ArrayList()
        this.fm = fm
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

    fun addAll(data: MutableList<Home_DTO>) {
        this.items = ArrayList(data)
        this.notifyDataSetChanged()
    }

//    @SuppressLint("ClickableViewAccessibility")
//    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
//        var itemView = p1
//        if (itemView == null) {
//            itemView = LayoutInflater.from(mContext).inflate(R.layout.settingplanner_listview, p2, false)
//        }
//
//        var currentItem = items[p0]
//
//        val startTime: TextView = itemView!!.findViewById(R.id.spl_startTime)
//        val endTime: TextView = itemView.findViewById(R.id.spl_endTime)
//        val task: TextView = itemView.findViewById(R.id.spl_task)
//
//        startTime.text = currentItem.starttime
//        endTime.text = currentItem.endtime
//        task.text = currentItem.task
//
////        itemView.setOnTouchListener { view, event ->
////            if(event.action == MotionEvent.ACTION_DOWN) {
////
////            }
////        }
//
//        return itemView
//    }

    override fun onBindViewHolder(holder: SettingPlannerViewHolder, position: Int) {
        var currentItem = items[position]

        holder.startTime.text = String.format("%02d", currentItem.starttime / 60) + " : " + String.format("%02d", currentItem.starttime % 60)
        holder.endTime.text = String.format("%02d", currentItem.endtime / 60) + " : " + String.format("%02d", currentItem.endtime % 60)
        holder.task.text = currentItem.task

        holder.task.setOnClickListener {
            val selectedItem = items[position]
            val fragment = SettingTodoList(selectedItem)

            fm.beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null).commit()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingPlannerViewHolder {
        _binding = SettingplannerListviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        mContext = binding.root.context
        return SettingPlannerViewHolder(binding.root)
    }

    inner class SettingPlannerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val startTime: TextView = binding.splStartTime
        val endTime: TextView = binding.splEndTime
        val task: TextView = binding.splTask
    }
}