package ru.fefu.wsr_connect_mobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.*
import ru.fefu.wsr_connect_mobile.remote.models.Column


class ColumnListAdapter(
    private val deleteOnClick: (Column) -> Unit,
    private val editOnClick: (Column) -> Unit,
    private val optionsMenuClickListener: CardListAdapter.OptionsMenuClickListener,
    private val list: List<Column>
) : ListAdapter<Column, RecyclerView.ViewHolder>(ItemCallback()) {


    inner class ColumnListHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var binding = ItemTasksColumnBinding.bind(item)

        init {
            binding.editColumnBtn.setOnClickListener {
                val position = adapterPosition
                if (position in list.indices) {
                    editOnClick.invoke(list[position])
                }
            }
            binding.deleteColumnBtn.setOnClickListener {
                val position = adapterPosition
                if (position in list.indices) {
                    deleteOnClick.invoke(list[position])
                }
            }
        }

        fun bind(item: Column) {
            binding.apply {
                columnTitle.text = item.columnTitle

                recycler.layoutManager = LinearLayoutManager(itemView.context)
                recycler.adapter = CardListAdapter(
                    { },
                    optionsMenuClickListener,
                    item.columnId,
                    item.cards
                )
            }
        }
    }

    private class ItemCallback : DiffUtil.ItemCallback<Column>() {
        override fun areItemsTheSame(oldItem: Column, newItem: Column): Boolean =
            true

        override fun areContentsTheSame(oldItem: Column, newItem: Column): Boolean =
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