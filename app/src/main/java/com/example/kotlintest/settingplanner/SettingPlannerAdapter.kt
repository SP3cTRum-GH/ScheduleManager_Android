package com.example.kotlintest.settingplanner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.kotlintest.R
import com.example.kotlintest.db.Home_DTO


class SettingPlannerAdapter : BaseAdapter {
    var items: ArrayList<Home_DTO>
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

    fun addAll(data: MutableList<Home_DTO>) {
        this.items = ArrayList(data)
        this.notifyDataSetChanged()
    }
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var itemView = p1
        if (itemView == null) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.settingplanner_listview, p2, false)
        }

        var currentItem = items[p0]

        val startTime: TextView = itemView!!.findViewById(R.id.spl_startTime)
        val endTime: TextView = itemView.findViewById(R.id.spl_endTime)
        val task: TextView = itemView.findViewById(R.id.spl_task)

        startTime.text = currentItem.starttime
        endTime.text = currentItem.endtime
        task.text = currentItem.task

        return itemView
    }
}