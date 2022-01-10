package ru.fefu.wsr_connect_mobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.*
import ru.fefu.wsr_connect_mobile.messenger.messages_list.*


class MessagesListAdapter(
    private val clickListener: (MessageItem) -> Unit,
    private val list: List<MessageItem>
) : ListAdapter<MessageItem, RecyclerView.ViewHolder>(ItemCallback()) {


    inner class SimpleMessageListHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var binding = ItemMessengerSimpleMessageBinding.bind(item)

        init {
            binding.card.setOnClickListener {
                val position = adapterPosition
                if (position in list.indices) {
                    clickListener.invoke(list[position])
                }
            }
        }

        fun bind(item: ItemSimpleMessage, position: Int) {
            binding.apply {
                messageBody.text = item.body
                messageTime.text = item.time
                if (item.read) read.visibility = View.VISIBLE
            }
        }
    }

    inner class ImageMessageListHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var binding = ItemMessengerImageMessageBinding.bind(item)

        init {
            binding.card.setOnClickListener {
                val position = adapterPosition
                if (position in list.indices) {
                    clickListener.invoke(list[position])
                }
            }
        }

        fun bind(item: ItemImageMessage, position: Int) {
            binding.apply {
                imageName.text = item.name.uppercase()
                imageSize.text = item.size.uppercase()
                messageTime.text = item.time
                if (item.read) read.visibility = View.VISIBLE
            }
        }
    }

    inner class ReplyMessageListHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var binding = ItemMessengerReplyMessageBinding.bind(item)

        init {
            binding.card.setOnClickListener {
                val position = adapterPosition
                if (position in list.indices) {
                    clickListener.invoke(list[position])
                }
            }
        }

        fun bind(item: ItemReplyMessage, position: Int) {
            binding.apply {
                messageBody.text = item.body
                replyMessageBody.text = item.reply
                replyMessageUserName.text = item.user
                messageTime.text = item.time
                if (item.read) read.visibility = View.VISIBLE
            }
        }
    }

    inner class SimpleMessageOtherListHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var binding = ItemMessengerSimpleMessageOtherBinding.bind(item)

        init {
            binding.card.setOnClickListener {
                val position = adapterPosition
                if (position in list.indices) {
                    clickListener.invoke(list[position])
                }
            }
        }

        fun bind(item: ItemSimpleMessageOther, position: Int) {
            binding.apply {
                messageBody.text = item.body
                messageTime.text = item.time
            }
        }
    }

    inner class ImageMessageOtherListHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var binding = ItemMessengerImageMessageOtherBinding.bind(item)

        init {
            binding.card.setOnClickListener {
                val position = adapterPosition
                if (position in list.indices) {
                    clickListener.invoke(list[position])
                }
            }
        }

        fun bind(item: ItemImageMessageOther, position: Int) {
            binding.apply {
                imageName.text = item.name.uppercase()
                imageSize.text = item.size.uppercase()
                messageTime.text = item.time
            }
        }
    }

    inner class ReplyMessageOtherListHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var binding = ItemMessengerReplyMessageOtherBinding.bind(item)

        init {
            binding.card.setOnClickListener {
                val position = adapterPosition
                if (position in list.indices) {
                    clickListener.invoke(list[position])
                }
            }
        }

        fun bind(item: ItemReplyMessageOther, position: Int) {
            binding.apply {
                messageBody.text = item.body
                replyMessageBody.text = item.reply
                replyMessageUserName.text = item.user
                messageTime.text = item.time
            }
        }
    }

    private class ItemCallback : DiffUtil.ItemCallback<MessageItem>() {
        override fun areItemsTheSame(oldItem: MessageItem, newItem: MessageItem): Boolean =
            true

        override fun areContentsTheSame(oldItem: MessageItem, newItem: MessageItem): Boolean =
            true
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is ItemSimpleMessage -> 0
            is ItemImageMessage -> 1
            is ItemReplyMessage -> 2
            is ItemSimpleMessageOther -> 3
            is ItemImageMessageOther -> 4
            is ItemReplyMessageOther -> 5
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_messenger_simple_message, parent, false)
                SimpleMessageListHolder(view)
            }
            1 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_messenger_image_message, parent, false)
                ImageMessageListHolder(view)
            }
            2 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_messenger_reply_message, parent, false)
                ReplyMessageListHolder(view)
            }
            3 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_messenger_simple_message_other, parent, false)
                SimpleMessageOtherListHolder(view)
            }
            4 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_messenger_image_message_other, parent, false)
                ImageMessageOtherListHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_messenger_reply_message_other, parent, false)
                ReplyMessageOtherListHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SimpleMessageListHolder -> {
                val item = list[position] as ItemSimpleMessage
                holder.bind(item, position)
            }
            is ImageMessageListHolder -> {
                val item = list[position] as ItemImageMessage
                holder.bind(item, position)
            }
            is ReplyMessageListHolder -> {
                val item = list[position] as ItemReplyMessage
                holder.bind(item, position)
            }
            is SimpleMessageOtherListHolder -> {
                val item = list[position] as ItemSimpleMessageOther
                holder.bind(item, position)
            }
            is ImageMessageOtherListHolder -> {
                val item = list[position] as ItemImageMessageOther
                holder.bind(item, position)
            }
            is ReplyMessageOtherListHolder -> {
                val item = list[position] as ItemReplyMessageOther
                holder.bind(item, position)
            }
        }
    }

    override fun getItemCount(): Int = list.size
}