package com.example.baseproject.ui.admin.allAcc.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.databinding.ItemAccountTeacherBinding
import com.example.baseproject.model.AccountTeacherResponse
import com.example.core.utils.setOnSafeClickListener

class AccTeacherAdapter :
    ListAdapter<AccountTeacherResponse, AccTeacherAdapter.ConsultantHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<AccountTeacherResponse>() {
        override fun areItemsTheSame(
            oldItem: AccountTeacherResponse,
            newItem: AccountTeacherResponse
        ): Boolean {
            return oldItem.teacherId == newItem.teacherId
        }

        override fun areContentsTheSame(
            oldItem: AccountTeacherResponse,
            newItem: AccountTeacherResponse
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class ConsultantHolder(val binding: ItemAccountTeacherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(acc: AccountTeacherResponse) {
            binding.root.setOnSafeClickListener {
            }
            binding.tvName.text = acc.teacherName
            binding.tvUsername.text = acc.userName

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsultantHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAccountTeacherBinding.inflate(inflater, parent, false)
        return ConsultantHolder(binding)
    }

    override fun onBindViewHolder(holder: ConsultantHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
