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
import ru.fefu.wsr_connect_mobile.databinding.ItemMessengerUserChatsBinding
import ru.fefu.wsr_connect_mobile.remote.models.Chat


class ChatListAdapter(
    private val clickListener: (Chat) -> Unit,
    private val clickListenerMute: (Chat) -> Unit,
    private val clickListenerPin: (Chat) -> Unit,
    private val clickListenerQuit: (Chat) -> Unit,
) : ListAdapter<Chat, RecyclerView.ViewHolder>(ItemCallback()) {


    inner class ChatListHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var binding = ItemMessengerUserChatsBinding.bind(item)

        init {
            binding.apply {
                binding.item.setOnClickListener {
                    val position = adapterPosition
                    if (position in currentList.indices) {
                        clickListener.invoke(currentList[position])
                    }
                }
                muteBtn.setOnClickListener {
                    val position = adapterPosition
                    if (position in currentList.indices) {
                        clickListenerMute.invoke(currentList[position])
                    }
                }
                pinBtn.setOnClickListener {
                    val position = adapterPosition
                    if (position in currentList.indices) {
                        clickListenerPin.invoke(currentList[position])
                    }
                }
                quitBtn.setOnClickListener {
                    val position = adapterPosition
                    if (position in currentList.indices) {
                        clickListenerQuit.invoke(currentList[position])
                    }
                }
            }
        }

        fun bind(item: Chat) {
            binding.apply {
                chatName.text = item.chatName
                lastMessage.text = item.lastMessage?.messageBody
                if (item.pin) pin.visibility = View.VISIBLE
                if (item.mute) notificationIndicator.visibility = View.VISIBLE

                val url = "$BASE_URL${item.imgUrl}"
                val imgView = binding.chatImg
                Glide.with(itemView).load(url).error(R.drawable.ic_delete2).into(imgView)
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
            .inflate(R.layout.item_messenger_user_chats, parent, false)
        return ChatListHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChatListHolder -> {
                val item = currentList[position]
                holder.bind(item)
            }
        }
    }
}
