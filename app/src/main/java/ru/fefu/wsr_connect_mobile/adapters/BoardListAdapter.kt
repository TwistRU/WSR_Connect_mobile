package ru.fefu.wsr_connect_mobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.*
import ru.fefu.wsr_connect_mobile.remote.models.Board


class BoardListAdapter(
    private val clickListener: (Board) -> Unit,
    private val optionsMenuClickListener: OptionsMenuClickListener,
    private val list: List<Board>
) : ListAdapter<Board, RecyclerView.ViewHolder>(ItemCallback()) {

    interface OptionsMenuClickListener {
        fun onOptionsMenuClicked(boardId: Int, boardName: String, view: View)
    }

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

        fun bind(item: Board) {
            binding.apply {
                boardName.text = item.boardName
                boardCreateDate.text = item.boardCreateDate
                boardUserCount.text = item.boardUserCount
                boardCreator.text = item.boardCreator
                if (item.isAvailable) {
                    boardLock.visibility = View.GONE
                }
                card.setOnLongClickListener {
                    optionsMenuClickListener.onOptionsMenuClicked(
                        item.boardId,
                        item.boardName,
                        it
                    )
                    return@setOnLongClickListener true
                }
            }
        }
    }

    private class ItemCallback : DiffUtil.ItemCallback<Board>() {
        override fun areItemsTheSame(oldItem: Board, newItem: Board): Boolean =
            true

        override fun areContentsTheSame(oldItem: Board, newItem: Board): Boolean =
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