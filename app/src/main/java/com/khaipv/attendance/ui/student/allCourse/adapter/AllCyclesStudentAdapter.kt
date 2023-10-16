package com.khaipv.attendance.ui.student.allCourse.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.khaipv.attendance.databinding.ItemCyclesBinding
import com.khaipv.attendance.model.OverviewCourseStudentRegister
import com.example.core.utils.setOnSafeClickListener

class AllCyclesStudentAdapter(
    private var onCyclesClick: ((cycles: OverviewCourseStudentRegister) -> Unit)
) :
    ListAdapter<OverviewCourseStudentRegister, AllCyclesStudentAdapter.AllCyclesAdapterHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<OverviewCourseStudentRegister>() {
        override fun areItemsTheSame(
            oldItem: OverviewCourseStudentRegister,
            newItem: OverviewCourseStudentRegister
        ): Boolean {
            return false
        }

        override fun areContentsTheSame(
            oldItem: OverviewCourseStudentRegister,
            newItem: OverviewCourseStudentRegister
        ): Boolean {
            return false
        }
    }

    inner class AllCyclesAdapterHolder(val binding: ItemCyclesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cycles: OverviewCourseStudentRegister) {
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

    override fun onBindViewHolder(holder: AllCyclesStudentAdapter.AllCyclesAdapterHolder, position: Int) {
        holder.bind(getItem(position))
    }

}
