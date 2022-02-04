package ru.fefu.wsr_connect_mobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.fefu.wsr_connect_mobile.common.BASE_URL
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.*
import ru.fefu.wsr_connect_mobile.extensions.formatTo
import ru.fefu.wsr_connect_mobile.extensions.toDate
import ru.fefu.wsr_connect_mobile.remote.models.Message


class MessagesListAdapter(
    private val optionsMenuClickListener: OptionsMenuClickListener,
) : ListAdapter<Message, RecyclerView.ViewHolder>(ItemCallback()) {

    interface OptionsMenuClickListener {
        fun onOptionsMenuClicked(message: Message, view: View)
    }

    var group: Boolean = false


    inner class SimpleMessageListHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var binding = ItemMessengerSimpleMessageBinding.bind(item)

        fun bind(item: Message) {
            binding.apply {
                messageBody.text = item.messageBody
                messageTime.text = item.createdAt.toDate().formatTo("HH:mm")
                if (item.read) read.visibility = View.VISIBLE
                card.setOnLongClickListener {
                    optionsMenuClickListener.onOptionsMenuClicked(item, it)
                    return@setOnLongClickListener true
                }
            }
        }
    }

    inner class ImageMessageListHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var binding = ItemMessengerImageMessageBinding.bind(item)

        fun bind(item: Message) {
            binding.apply {
                messageTime.text = item.createdAt.toDate().formatTo("HH:mm")
                if (item.read) read.visibility = View.VISIBLE
                card.setOnLongClickListener {
                    optionsMenuClickListener.onOptionsMenuClicked(item, it)
                    return@setOnLongClickListener true
                }
                val url = "$BASE_URL${item.imgUrl}"
                val imgView = binding.image
                Glide.with(itemView).load(url).error(R.drawable.ic_no_image).into(imgView)
            }
        }
    }

    inner class ReplyMessageListHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var binding = ItemMessengerReplyMessageBinding.bind(item)

        fun bind(item: Message) {
            binding.apply {
                messageBody.text = item.messageBody
                replyMessageBody.text = item.parentMessage?.messageBody
                replyMessageUserName.text = item.parentMessage?.creatorName
                messageTime.text = item.createdAt.toDate().formatTo("HH:mm")
                if (item.read) read.visibility = View.VISIBLE
                card.setOnLongClickListener {
                    optionsMenuClickListener.onOptionsMenuClicked(item, it)
                    return@setOnLongClickListener true
                }
            }
        }
    }

    inner class SimpleMessageOtherListHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var binding = ItemMessengerSimpleMessageOtherBinding.bind(item)

        fun bind(item: Message) {
            binding.apply {
                messageBody.text = item.messageBody
                messageTime.text = item.createdAt.toDate().formatTo("HH:mm")
                card.setOnLongClickListener {
                    optionsMenuClickListener.onOptionsMenuClicked(item, it)
                    return@setOnLongClickListener true
                }

                if (group) {
                    if (adapterPosition + 1 != currentList.size &&
                        item.creatorId != currentList[adapterPosition + 1].creatorId
                    ) {
                        senderImgContainer.visibility = View.VISIBLE
                        val urlUser = "$BASE_URL${item.creatorImgUrl}"
                        val imgViewUser = binding.senderImg
                        Glide.with(itemView).load(urlUser).error(R.drawable.ic_no_image)
                            .into(imgViewUser)
                    } else {
                        senderImgContainer.visibility = View.INVISIBLE
                    }

                    if (adapterPosition - 1 != -1 &&
                        item.creatorId != currentList[adapterPosition - 1].creatorId
                    ) {
                        senderName.visibility = View.VISIBLE
                        senderName.text = item.creatorName
                        senderName.paint.isUnderlineText = true
                    } else {
                        senderName.visibility = View.GONE
                    }
                }
            }
        }
    }

    inner class ImageMessageOtherListHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var binding = ItemMessengerImageMessageOtherBinding.bind(item)

        fun bind(item: Message) {
            binding.apply {
                messageTime.text = item.createdAt.toDate().formatTo("HH:mm")
                card.setOnLongClickListener {
                    optionsMenuClickListener.onOptionsMenuClicked(item, it)
                    return@setOnLongClickListener true
                }
                val url = "$BASE_URL${item.imgUrl}"
                val imgView = binding.image
                Glide.with(itemView).load(url).error(R.drawable.ic_no_image).into(imgView)

                if (group) {
                    if (adapterPosition + 1 != currentList.size &&
                        item.creatorId != currentList[adapterPosition + 1].creatorId
                    ) {
                        senderImgContainer.visibility = View.VISIBLE
                        val urlUser = "$BASE_URL${item.creatorImgUrl}"
                        val imgViewUser = binding.senderImg
                        Glide.with(itemView).load(urlUser).error(R.drawable.ic_no_image)
                            .into(imgViewUser)
                    } else {
                        senderImgContainer.visibility = View.INVISIBLE
                    }

                    if (adapterPosition - 1 != -1 &&
                        item.creatorId != currentList[adapterPosition - 1].creatorId
                    ) {
                        senderName.visibility = View.VISIBLE
                        senderName.text = item.creatorName
                        senderName.paint.isUnderlineText = true
                    } else {
                        senderName.visibility = View.GONE
                    }
                }
            }
        }
    }

    inner class ReplyMessageOtherListHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var binding = ItemMessengerReplyMessageOtherBinding.bind(item)

        fun bind(item: Message) {
            binding.apply {
                messageBody.text = item.messageBody
                replyMessageBody.text = item.parentMessage?.messageBody
                replyMessageUserName.text = item.parentMessage?.creatorName
                messageTime.text = item.createdAt.toDate().formatTo("HH:mm")
                card.setOnLongClickListener {
                    optionsMenuClickListener.onOptionsMenuClicked(item, it)
                    return@setOnLongClickListener true
                }

                if (group) {
                    if (adapterPosition + 1 != currentList.size &&
                        item.creatorId != currentList[adapterPosition + 1].creatorId
                    ) {
                        senderImgContainer.visibility = View.VISIBLE
                        val urlUser = "$BASE_URL${item.creatorImgUrl}"
                        val imgViewUser = binding.senderImg
                        Glide.with(itemView).load(urlUser).error(R.drawable.ic_no_image)
                            .into(imgViewUser)
                    } else {
                        senderImgContainer.visibility = View.INVISIBLE
                    }

                    if (adapterPosition - 1 != -1 &&
                        item.creatorId != currentList[adapterPosition - 1].creatorId
                    ) {
                        senderName.visibility = View.VISIBLE
                        senderName.text = item.creatorName
                        senderName.paint.isUnderlineText = true
                    } else {
                        senderName.visibility = View.GONE
                    }
                }
            }
        }
    }

    private class ItemCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean =
            oldItem.messageId == newItem.messageId

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean =
            oldItem == newItem
    }

    override fun getItemViewType(position: Int): Int {
        val it = currentList[position]

        return if (it.mine) {
            when {
                it.parentMessage != null -> 2
                it.imgUrl != null -> 1
                else -> 0
            }

        } else {
            when {
                it.parentMessage != null -> 5
                it.imgUrl != null -> 4
                else -> 3
            }
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
                val item = currentList[position]
                holder.bind(item)
            }
            is ImageMessageListHolder -> {
                val item = currentList[position]
                holder.bind(item)
            }
            is ReplyMessageListHolder -> {
                val item = currentList[position]
                holder.bind(item)
            }
            is SimpleMessageOtherListHolder -> {
                val item = currentList[position]
                holder.bind(item)
            }
            is ImageMessageOtherListHolder -> {
                val item = currentList[position]
                holder.bind(item)
            }
            is ReplyMessageOtherListHolder -> {
                val item = currentList[position]
                holder.bind(item)
            }
        }
    }
}