package ru.fefu.wsr_connect_mobile.tasks.board_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.*


class BoardListAdapter(
    private val clickListener: (BoardItem) -> Unit,
    private val list: List<BoardItem>
) : ListAdapter<BoardItem, RecyclerView.ViewHolder>(ItemCallback()) {


    inner class BoardListHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var binding = ItemTasksBoardBinding.bind(item)

        init {
            binding.card.setOnClickListener {
                val position = adapterPosition
                if (position in list.indices) {
                    clickListener.invoke(list[position])
                }
            }
        }

        fun bind(item: BoardItem) {
            binding.apply {
                boardName.text = item.boardName
                boardCreateDate.text = item.boardCreateDate
                boardUserCount.text = item.boardUserCount
                boardCreator.text = item.boardCreator
                if (item.available) {
                    boardLock.visibility = View.GONE
                }
            }
        }
    }

    private class ItemCallback : DiffUtil.ItemCallback<BoardItem>() {
        override fun areItemsTheSame(oldItem: BoardItem, newItem: BoardItem): Boolean =
            true

        override fun areContentsTheSame(oldItem: BoardItem, newItem: BoardItem): Boolean =
            true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tasks_board, parent, false)
        return BoardListHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BoardListHolder -> {
                val item = list[position]
                holder.bind(item)
            }
        }
    }

    override fun getItemCount(): Int = list.size
}