package com.khaipv.attendance.ui.teacher.editSchedule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.khaipv.attendance.databinding.ItemCyclesBinding
import com.example.core.utils.setOnSafeClickListener
import com.khaipv.attendance.model.Classroom

class AllClassroomTeacherAdapter(
    private var onClassroomClick: ((classroom: Classroom) -> Unit)
) :
    ListAdapter<Classroom, AllClassroomTeacherAdapter.AllCyclesAdapterHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<Classroom>() {
        override fun areItemsTheSame(
            oldItem: Classroom,
            newItem: Classroom
        ): Boolean {
            return false
        }

        override fun areContentsTheSame(
            oldItem: Classroom,
            newItem: Classroom
        ): Boolean {
            return false
        }
    }

    inner class AllCyclesAdapterHolder(val binding: ItemCyclesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(classroom: Classroom) {
            binding.tvCyclesName.text = classroom.name
            binding.root.setOnSafeClickListener {
                onClassroomClick.invoke(classroom)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllCyclesAdapterHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCyclesBinding.inflate(inflater, parent, false)
        return AllCyclesAdapterHolder(binding)
    }

    override fun onBindViewHolder(holder: AllClassroomTeacherAdapter.AllCyclesAdapterHolder, position: Int) {
        holder.bind(getItem(position))
    }

}
