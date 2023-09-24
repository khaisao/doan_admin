package com.example.baseproject.ui.teacher.listFaceReco.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.R
import com.example.baseproject.databinding.ItemAccountBinding
import com.example.baseproject.databinding.ItemStudentRecoBinding
import com.example.baseproject.model.AllImageProfileStudentForCourse
import com.example.core.utils.loadImage
import com.example.core.utils.setOnSafeClickListener

class ListFaceRecoAdapter(
) :
    ListAdapter<AllImageProfileStudentForCourse, ListFaceRecoAdapter.ListFaceRecoHolder>(
        DiffCallback()
    ) {

    class DiffCallback : DiffUtil.ItemCallback<AllImageProfileStudentForCourse>() {
        override fun areItemsTheSame(
            oldItem: AllImageProfileStudentForCourse,
            newItem: AllImageProfileStudentForCourse
        ): Boolean {
            return oldItem.studentId == newItem.studentId
        }

        override fun areContentsTheSame(
            oldItem: AllImageProfileStudentForCourse,
            newItem: AllImageProfileStudentForCourse
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class ListFaceRecoHolder(val binding: ItemStudentRecoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(student: AllImageProfileStudentForCourse) {
            binding.tvName.text = student.name
            binding.tvEmail.text = student.name
            if (student.isReco) {
                binding.ivCheck.loadImage(R.drawable.ic_check)
            } else {
                binding.ivCheck.loadImage(R.drawable.ic_uncheck)

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListFaceRecoHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemStudentRecoBinding.inflate(inflater, parent, false)
        return ListFaceRecoHolder(binding)
    }

    override fun onBindViewHolder(holder: ListFaceRecoHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
