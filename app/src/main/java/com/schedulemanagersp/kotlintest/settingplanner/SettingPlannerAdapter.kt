package com.schedulemanagersp.kotlintest.settingplanner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.schedulemanagersp.kotlintest.R
import com.schedulemanagersp.kotlintest.databinding.SettingplannerListviewBinding
import com.schedulemanagersp.kotlintest.db.Home_DTO


class SettingPlannerAdapter : RecyclerView.Adapter<SettingPlannerAdapter.SettingPlannerViewHolder> {
    private var _binding: SettingplannerListviewBinding? = null
    private val binding get() = _binding!!
    var items: List<Home_DTO>
    var fm: FragmentManager
    var viewModel: PlannerVM
//    lateinit var mContext: Context

    constructor(fm: FragmentManager, viewModel: PlannerVM) {
        this.items = ArrayList()
        this.fm = fm
        this.viewModel = viewModel
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

    fun addAll(data: List<Home_DTO>) {
        this.items = ArrayList(data)
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

        val startTime = String.format("%02d", currentItem.starttime / 60) + " : " + String.format("%02d", currentItem.starttime % 60)
        val endTime = String.format("%02d", currentItem.endtime / 60) + " : " + String.format("%02d", currentItem.endtime % 60)
        val task = currentItem.task

        holder.bind(startTime,endTime,task)

        holder.listArea.setOnClickListener {
            val selectedItem = items[position]
            val fragment = SettingTodoList(selectedItem,viewModel)

            fm.beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null).commit()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingPlannerViewHolder {
        _binding = SettingplannerListviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        mContext = binding.root.context
        return SettingPlannerViewHolder(binding)
    }

    inner class SettingPlannerViewHolder(val binding: SettingplannerListviewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(starttime: String, endtime: String, task: String) {
            binding.starttime = starttime
            binding.endtime = endtime
            binding.task = task
        }
        val listArea: ConstraintLayout = binding.listArea
    }
}