package ru.fefu.wsr_connect_mobile.tasks.column_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.*
import ru.fefu.wsr_connect_mobile.tasks.column_list.card_list.CardListAdapter


class ColumnListAdapter(
    private val clickListener: (ColumnItem) -> Unit,
    private val list: List<ColumnItem>
) : ListAdapter<ColumnItem, RecyclerView.ViewHolder>(ItemCallback()) {


    inner class ColumnListHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var binding = ItemTasksColumnBinding.bind(item)

        init {
            binding.item.setOnClickListener {
                val position = adapterPosition
                if (position in list.indices) {
                    clickListener.invoke(list[position])
                }
            }
        }

        fun bind(item: ColumnItem) {
            binding.apply {
                columnTitle.text = item.columnTitle

                recycler.layoutManager = LinearLayoutManager(itemView.context)
                recycler.adapter = CardListAdapter(
                    { },
                    item.cardList
                )
            }
        }
    }

    private class ItemCallback : DiffUtil.ItemCallback<ColumnItem>() {
        override fun areItemsTheSame(oldItem: ColumnItem, newItem: ColumnItem): Boolean =
            true

        override fun areContentsTheSame(oldItem: ColumnItem, newItem: ColumnItem): Boolean =
            true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tasks_column, parent, false)
        return ColumnListHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ColumnListHolder -> {
                val item = list[position]
                holder.bind(item)
            }
        }
    }

    override fun getItemCount(): Int = list.size
}