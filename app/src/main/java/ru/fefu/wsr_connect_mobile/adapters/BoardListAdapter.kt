package ru.fefu.wsr_connect_mobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.fefu.wsr_connect_mobile.BASE_URL
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.*
import ru.fefu.wsr_connect_mobile.remote.models.Board
import ru.fefu.wsr_connect_mobile.extensions.formatTo
import ru.fefu.wsr_connect_mobile.extensions.toDate


class BoardListAdapter(
    private val clickListenerUser: (Board) -> Unit,
    private val clickListener: (Board) -> Unit,
    private val optionsMenuClickListener: OptionsMenuClickListener,
) : ListAdapter<Board, RecyclerView.ViewHolder>(ItemCallback()) {

    interface OptionsMenuClickListener {
        fun onOptionsMenuClicked(boardId: Int, boardName: String, view: View)
    }

    inner class BoardListHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var binding = ItemTasksBoardBinding.bind(item)

        init {
            binding.item.setOnClickListener {
                val position = adapterPosition
                if (position in currentList.indices) {
                    clickListener.invoke(currentList[position])
                }
            }

            binding.boardCreator.setOnClickListener {
                val position = adapterPosition
                if (position in currentList.indices) {
                    clickListenerUser.invoke(currentList[position])
                }
            }
        }

        fun bind(item: Board) {
            binding.apply {
                boardName.text = item.boardName
                boardCreateDate.text = item.boardCreateDate.toDate().formatTo("dd MMM yyyy")
                boardUserCount.text = item.boardUserCount
                boardCreator.text = item.boardCreator
                if (item.isAvailable) {
                    boardLock.visibility = View.GONE
                }
                val url = "$BASE_URL${item.imgUrl}"
                val imgView = binding.boardImg

                if (item.imgUrl == null) {
                    imgView.minimumHeight = 300
                    imgView.minimumWidth = 300
                }
                Glide.with(itemView).load(url).error(R.drawable.ic_image_not_supported)
                    .into(imgView)

                binding.item.setOnLongClickListener {
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
            oldItem.boardId == newItem.boardId

        override fun areContentsTheSame(oldItem: Board, newItem: Board): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tasks_board, parent, false)
        return BoardListHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BoardListHolder -> {
                val item = currentList[position]
                holder.bind(item)
            }
        }
    }
}