package com.example.kotlintest.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.kotlintest.R
import com.example.kotlintest.db.PlannerName_DTO

class HomeAdapter(val context: Context, var items: ArrayList<PlannerName_DTO>) : BaseAdapter() {

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
    fun addAll(data: List<PlannerName_DTO>) {
        this.items = ArrayList(data)
        this.notifyDataSetChanged()
    }
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var itemView = p1
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.plannerlist, p2, false)
        }
        var currentItem = items[p0]

        val textView: TextView = itemView!!.findViewById(R.id.calText)

        textView.text = currentItem.name


        return itemView
    }
}