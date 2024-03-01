package com.example.kotlintest.settingplanner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlintest.R
import com.example.kotlintest.db.AppDatabase
import com.example.kotlintest.db.PlannerName_DTO
import com.example.kotlintest.db.TodoList_DTO

class PlannerAdapter : RecyclerView.Adapter<PlannerAdapter.PlannerNameViewHolder> {
    lateinit var mContext: Context
    var items: ArrayList<PlannerName_DTO>
    var fm: FragmentManager
    constructor(fm: FragmentManager) {
        this.items = ArrayList()
        this.fm = fm
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlannerAdapter.PlannerNameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.stringlist, parent, false)
        mContext = view.context
        return PlannerNameViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlannerAdapter.PlannerNameViewHolder, position: Int) {
        var currentItem = items[position]

        holder.plannerName.text = currentItem.name

        holder.plannerName.setOnClickListener {
            val selectedItem = items[position]
            val fragment = DetailPlanner(selectedItem)

            fm.beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null).commit()
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

    fun addAll(data: MutableList<PlannerName_DTO>) {
        this.items = ArrayList(data)
        this.notifyDataSetChanged()
    }

    inner class PlannerNameViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val plannerName: TextView = itemView.findViewById(R.id.plannerName)
    }
}