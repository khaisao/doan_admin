package com.example.baseproject.ui.admin.allAcc.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.databinding.ItemAccountStudentBinding
import com.example.baseproject.model.AccountStudentResponse
import com.example.core.utils.setOnSafeClickListener

class AccStudentAdapter(
) :
    ListAdapter<AccountStudentResponse, AccStudentAdapter.ConsultantHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<AccountStudentResponse>() {
        override fun areItemsTheSame(
            oldItem: AccountStudentResponse,
            newItem: AccountStudentResponse
        ): Boolean {
            return oldItem.studentId == newItem.studentId
        }

        override fun areContentsTheSame(
            oldItem: AccountStudentResponse,
            newItem: AccountStudentResponse
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class ConsultantHolder(val binding: ItemAccountStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(acc: AccountStudentResponse) {
            binding.root.setOnSafeClickListener {
            }
            binding.tvName.text = acc.studentName
            binding.tvUsername.text = acc.userName

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsultantHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAccountStudentBinding.inflate(inflater, parent, false)
        return ConsultantHolder(binding)
    }

    override fun onBindViewHolder(holder: ConsultantHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
