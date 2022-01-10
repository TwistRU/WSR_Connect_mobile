package ru.fefu.wsr_connect_mobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.ItemMessengerChatBinding
import ru.fefu.wsr_connect_mobile.remote.models.Chat


class ChatListAdapter(
    private val clickListener: (Chat) -> Unit,
    private val list: List<Chat>
) : ListAdapter<Chat, RecyclerView.ViewHolder>(ItemCallback()) {


    inner class ChatListHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var binding = ItemMessengerChatBinding.bind(item)

        init {
            binding.item.surfaceView.setOnClickListener {
                val position = adapterPosition
                if (position in list.indices) {
                    clickListener.invoke(list[position])
                }
            }
        }

        fun bind(item: Chat, position: Int) {
            binding.apply {
                chatName.text = item.chatName
                lastMessage.text = item.lastMessage?.messageBody
                if (item.isPinned) pin.visibility = View.VISIBLE
                if (item.isMute) notificationIndicator.visibility = View.VISIBLE
            }
        }
    }

    private class ItemCallback : DiffUtil.ItemCallback<Chat>() {
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean =
            oldItem.chatId == newItem.chatId

        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_messenger_chat, parent, false)
        return ChatListHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChatListHolder -> {
                val item = list[position]
                holder.bind(item, position)
            }
        }
    }

    override fun getItemCount(): Int = list.size
}
