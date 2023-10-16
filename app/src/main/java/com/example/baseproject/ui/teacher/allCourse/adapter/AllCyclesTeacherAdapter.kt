package com.example.baseproject.ui.teacher.allCourse.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.databinding.ItemCyclesBinding
import com.example.baseproject.model.OverviewCourseTeacherAssign
import com.example.core.utils.setOnSafeClickListener

class AllCyclesTeacherAdapter(
    private var onCyclesClick: ((cycles: OverviewCourseTeacherAssign) -> Unit)
) :
    ListAdapter<OverviewCourseTeacherAssign, AllCyclesTeacherAdapter.AllCyclesAdapterHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<OverviewCourseTeacherAssign>() {
        override fun areItemsTheSame(
            oldItem: OverviewCourseTeacherAssign,
            newItem: OverviewCourseTeacherAssign
        ): Boolean {
            return false
        }

        override fun areContentsTheSame(
            oldItem: OverviewCourseTeacherAssign,
            newItem: OverviewCourseTeacherAssign
        ): Boolean {
            return false
        }
    }

    inner class AllCyclesAdapterHolder(val binding: ItemCyclesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cycles: OverviewCourseTeacherAssign) {
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

    override fun onBindViewHolder(holder: AllCyclesTeacherAdapter.AllCyclesAdapterHolder, position: Int) {
        holder.bind(getItem(position))
    }

}
