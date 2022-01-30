package ru.fefu.wsr_connect_mobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.ItemTasksColumnBinding
import ru.fefu.wsr_connect_mobile.remote.models.Card
import ru.fefu.wsr_connect_mobile.remote.models.Column


class ColumnListAdapter(
    private val clickListenerCardCreator: (Card) -> Unit,
    private val clickListenerCardDetail: (Card) -> Unit,
    private val clickListenerColumn: (Column) -> Unit,
    private val columnActionsMenuClickListener: OptionsMenuClickListener,
) : ListAdapter<Column, RecyclerView.ViewHolder>(ItemCallback()) {

    interface OptionsMenuClickListener {
        fun onOptionsMenuClicked(
            columnId: Int,
            columnTitle: String,
            view: View
        )
    }

    inner class ColumnListHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var binding = ItemTasksColumnBinding.bind(item)

        init {
            binding.addCardBtn.setOnClickListener {
                val position = adapterPosition
                if (position in currentList.indices) {
                    clickListenerColumn.invoke(currentList[position])
                }
            }
        }

        fun bind(item: Column) {
            binding.apply {
                columnTitle.text = item.columnTitle

                columnActionsBtn.setOnClickListener {
                    columnActionsMenuClickListener.onOptionsMenuClicked(
                        item.columnId, item.columnTitle, it)

                }

                val adapter = CardListAdapter(clickListenerCardCreator,clickListenerCardDetail)
                recycler.layoutManager = LinearLayoutManager(itemView.context)
                recycler.adapter = adapter
                adapter.submitList(item.cards)
            }
        }
    }

    private class ItemCallback : DiffUtil.ItemCallback<Column>() {
        override fun areItemsTheSame(oldItem: Column, newItem: Column): Boolean =
            oldItem.columnId == newItem.columnId

        override fun areContentsTheSame(oldItem: Column, newItem: Column): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tasks_column, parent, false)
        return ColumnListHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ColumnListHolder -> {
                val item = currentList[position]
                holder.bind(item)
            }
        }
    }
}