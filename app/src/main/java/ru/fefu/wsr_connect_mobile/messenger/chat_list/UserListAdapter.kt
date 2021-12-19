package ru.fefu.wsr_connect_mobile.messenger.chat_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.ItemMessengerChatBinding
import ru.fefu.wsr_connect_mobile.databinding.ItemMessengerNewChatBinding


class ChatListAdapter(
    private val clickListener: (MessengerItem) -> Unit,
    private val list: List<MessengerItem>
) : ListAdapter<MessengerItem, RecyclerView.ViewHolder>(ItemCallback()) {


    inner class NewChatListHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var binding = ItemMessengerNewChatBinding.bind(item)

        init {
            binding.item.setOnClickListener {
                val position = adapterPosition
                if (position in list.indices) {
                    clickListener.invoke(list[position] as ItemNewChat)
                }
            }
        }

        fun bind(item: ItemNewChat, position: Int) {
            binding.apply {
                firstName.text = item.firstName
                lastName.text = item.lastName
            }
        }
    }

    inner class ChatListHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var binding = ItemMessengerChatBinding.bind(item)

        init {
            binding.item.setOnClickListener {
                val position = adapterPosition
                if (position in list.indices) {
                    clickListener.invoke(list[position] as ItemChat)
                }
            }
        }

        fun bind(item: ItemChat, position: Int) {
            binding.apply {
                chatName.text = item.chatName
                lastMessage.text = item.lastMessage
                if (item.isPinned) pin.visibility = View.VISIBLE
                if (item.isMute) notificationIndicator.visibility = View.VISIBLE
            }
        }
    }

    private class ItemCallback : DiffUtil.ItemCallback<MessengerItem>() {
        override fun areItemsTheSame(oldItem: MessengerItem, newItem: MessengerItem): Boolean =
            oldItem is ItemNewChat && newItem is ItemNewChat && oldItem == newItem ||
                    oldItem is ItemChat && newItem is ItemChat && oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MessengerItem, newItem: MessengerItem): Boolean =
            oldItem == newItem
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is ItemNewChat -> 0
            is ItemChat -> 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_messenger_new_chat, parent, false)
            NewChatListHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_messenger_chat, parent, false)
            ChatListHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NewChatListHolder) {
            val item = list[position] as ItemNewChat
            holder.bind(item, position)
        } else if (holder is ChatListHolder) {
            val item = list[position] as ItemChat
            holder.bind(item, position)
        }
    }

    override fun getItemCount(): Int = list.size
}