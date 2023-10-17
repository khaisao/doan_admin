package com.khaipv.attendance.ui.admin.allCourse.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.khaipv.attendance.databinding.ItemCyclesBinding
import com.khaipv.attendance.model.OverViewCourseHaveShedule
import com.example.core.utils.setOnSafeClickListener

class AllCyclesAdminAdapter(
    private var onCyclesClick: ((cycles: OverViewCourseHaveShedule) -> Unit)
) :
    ListAdapter<OverViewCourseHaveShedule, AllCyclesAdminAdapter.AllCyclesAdapterHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<OverViewCourseHaveShedule>() {
        override fun areItemsTheSame(
            oldItem: OverViewCourseHaveShedule,
            newItem: OverViewCourseHaveShedule
        ): Boolean {
            return false
        }

        override fun areContentsTheSame(
            oldItem: OverViewCourseHaveShedule,
            newItem: OverViewCourseHaveShedule
        ): Boolean {
            return false
        }
    }

    inner class AllCyclesAdapterHolder(val binding: ItemCyclesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cycles: OverViewCourseHaveShedule) {
            binding.tvCyclesName.text = cycles.cyclesDes
            binding.root.setOnSafeClickListener {
                onCyclesClick.invoke(cycles)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllCyclesAdapterHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCyclesBinding.inflate(inflater, parent, false)
        return AllCyclesAdapterHolder(binding)
    }

    override fun onBindViewHolder(holder: AllCyclesAdminAdapter.AllCyclesAdapterHolder, position: Int) {
        holder.bind(getItem(position))
    }

}
