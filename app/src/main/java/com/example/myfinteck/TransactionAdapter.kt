package com.example.myfinteck

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.example.myfinteck.databinding.ItemTransactionBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionAdapter(private val onDeleteClick: (TransactionEntity) -> Unit)
    : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    private var list = listOf<TransactionEntity>()

    fun submitList(newList: List<TransactionEntity>) {
        list = newList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemTransactionBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTransactionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.binding.tvTitle.text = item.title
        holder.binding.tvAmount.text = "₹${item.amount}"
        holder.binding.tvCategory.text = item.category

        // DELETE CLICK
        holder.binding.btnDelete.setOnClickListener {
            onDeleteClick(item)
        }

        val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        holder.binding.tvDate.text = format.format(Date(item.date))

    }
}