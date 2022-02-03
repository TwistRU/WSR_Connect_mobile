package ru.fefu.wsr_connect_mobile.adapters

import android.content.Context
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.res.stringResource
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.fefu.wsr_connect_mobile.common.BASE_URL
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.ItemMessengerChatBinding
import ru.fefu.wsr_connect_mobile.extensions.formatTo
import ru.fefu.wsr_connect_mobile.extensions.toDate
import ru.fefu.wsr_connect_mobile.remote.models.Chat
import ru.fefu.wsr_connect_mobile.remote.models.Message
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.SwipeLayout.SwipeListener


class ChatListAdapter(
    private val clickListener: (Chat) -> Unit,
    private val clickListenerEdit: (Chat) -> Unit,
    private val clickListenerMute: (Chat) -> Unit,
    private val clickListenerPin: (Chat) -> Unit,
    private val clickListenerQuit: (Chat) -> Unit,
) : ListAdapter<Chat, RecyclerView.ViewHolder>(ItemCallback()) {

    var selectedItemPos = -1
    var lastItemSelectedPos = -1


    inner class ChatListHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var binding = ItemMessengerChatBinding.bind(item)

        init {

            binding.swipeItem.addSwipeListener(object : SwipeListener {
                override fun onClose(layout: SwipeLayout) {}
                override fun onUpdate(layout: SwipeLayout, leftOffset: Int, topOffset: Int) {}
                override fun onStartOpen(layout: SwipeLayout) {
                    selectedItemPos = adapterPosition
                    if (lastItemSelectedPos != -1 && lastItemSelectedPos != adapterPosition) {
                        notifyItemChanged(lastItemSelectedPos)
                    }
                    lastItemSelectedPos = selectedItemPos
                }

                override fun onOpen(layout: SwipeLayout) {}
                override fun onStartClose(layout: SwipeLayout) {}
                override fun onHandRelease(layout: SwipeLayout, xvel: Float, yvel: Float) {}
            })

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
                if (item.lastMessage != null) {
                    val last = item.lastMessage as Message
                    val text = if (last.imgUrl != null) "image"
                    else last.messageBody
                    val lastMess = "@" + last.creatorName + ": " + text
                    lastMessage.text = lastMess

                    val date = last.createdAt.toDate()
                    lastMessageDate.text = if (DateUtils.isToday(date.toInstant().toEpochMilli())) {
                        " · " + date.formatTo(("HH:mm"))
                    } else {
                        " · " + date.formatTo(("dd MMM"))
                    }
                } else {
                    lastMessage.text = itemView.context.getString(R.string.no_messages)
                }

                val url = "$BASE_URL${item.imgUrl}"
                val imgView = binding.chatImg
                if (item.group) {
                    Glide.with(itemView).load(url).error(R.drawable.ic_no_image).into(imgView)
                } else {
                    Glide.with(itemView).load(url).error(R.drawable.ic_profile).into(imgView)
                }

                if (item.pin) {
                    pinned.visibility = View.VISIBLE
                    pinBtn.setIconResource(R.drawable.ic_star_outline)
                } else {
                    pinned.visibility = View.GONE
                    pinBtn.setIconResource(R.drawable.ic_star_filled)
                }

                if (item.mute) {
                    muted.visibility = View.VISIBLE
                    muteBtn.setIconResource(R.drawable.ic_volume_up)
                } else {
                    muted.visibility = View.GONE
                    muteBtn.setIconResource(R.drawable.ic_volume_off)
                }

                if (item.group && item.mine) {
                    editBtn.visibility = View.VISIBLE
                    editBtn.setOnClickListener {
                        val position = adapterPosition
                        if (position in currentList.indices) {
                            clickListenerEdit.invoke(currentList[position])
                        }
                    }
                } else {
                    editBtn.visibility = View.GONE
                }
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
                holder.bind(currentList[position])
            }
        }
    }
}
